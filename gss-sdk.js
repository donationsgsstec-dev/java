
class GSSClient {
  constructor(opts) {
    if (!opts.apiKey || !opts.cfWorkerUrl || !opts.hfEngineUrl) {
      throw new Error('[GSSClient] apiKey, cfWorkerUrl, and hfEngineUrl are required');
    }
    this.apiKey      = opts.apiKey;
    this.cfWorkerUrl = opts.cfWorkerUrl.replace(/\/$/,'');
    this.hfEngineUrl = opts.hfEngineUrl.replace(/\/$/,'');
    this.model       = opts.model || 'llama-3.1-8b-instant';
    this.storageKey  = opts.storageKey || 'gss_jwt';
    this._token      = null;
    if (typeof window !== 'undefined') this._startKeepAlive();
  }
  _loadCachedToken() {
    try {
      const raw = localStorage.getItem(this.storageKey);
      if (!raw) return null;
      const { token, exp } = JSON.parse(raw);
      if (Date.now() / 1000 < exp - 300) return token;
      localStorage.removeItem(this.storageKey);
    } catch {}
    return null;
  }
  _decodeExp(token) {
    try {
      const p = JSON.parse(atob(token.split('.')[1].replace(/-/g,'+').replace(/_/g,'/')));
      return p.exp || 0;
    } catch { return 0; }
  }
  async _lease() {
    const res = await fetch(this.cfWorkerUrl + '/auth/lease', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ api_key: this.apiKey }),
    });
    if (res.status === 403) throw new Error('[GSSClient] Subscription expired or invalid API key');
    if (!res.ok) throw new Error('[GSSClient] Lease failed: ' + res.status);
    const { token } = await res.json();
    const exp = this._decodeExp(token);
    localStorage.setItem(this.storageKey, JSON.stringify({ token, exp }));
    this._token = token;
    return token;
  }
  async _getToken() {
    if (!this._token) this._token = this._loadCachedToken();
    if (!this._token) this._token = await this._lease();
    return this._token;
  }
  async chat(messages, opts = {}) {
    const token = await this._getToken();
    const body = {
      model:       opts.model || this.model,
      messages,
      temperature: opts.temperature ?? 0.7,
      max_tokens:  opts.max_tokens  ?? 1024,
    };
    if (opts.provider) body.provider = opts.provider;
    const res = await fetch(this.hfEngineUrl + '/v1/chat/completions', {
      method: 'POST',
      headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });
    if (res.status === 401 || res.status === 403) {
      localStorage.removeItem(this.storageKey);
      this._token = null;
      return this.chat(messages, opts);
    }
    if (!res.ok) {
      const err = await res.json().catch(() => ({ error: res.statusText }));
      throw new Error('[GSSClient] Chat error ' + res.status + ': ' + (err.error || res.statusText));
    }
    const data = await res.json();
    return data.choices?.[0]?.message?.content ?? '';
  }
  async ping() { return (await fetch(this.hfEngineUrl + '/ping')).json(); }
  async renewToken() {
    localStorage.removeItem(this.storageKey);
    this._token = null;
    return this._lease();
  }
  async _syncKeys() {
    try {
      const token = await this._getToken();
      await fetch(this.cfWorkerUrl + '/auth/keep-alive', {
        method: 'POST',
        headers: { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' },
      });
    } catch (_) {}
  }
  _startKeepAlive() {
    setTimeout(() => this._syncKeys(), 5000);
    setInterval(() => this._syncKeys(), 30 * 60 * 1000);
  }
}
if (typeof module !== 'undefined' && module.exports) module.exports = GSSClient;
else if (typeof window !== 'undefined') window.GSSClient = GSSClient;
