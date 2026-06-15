# Railway GSS AI API Configuration Guide

## Overview
The Julia AI chat widget now uses environment variables for API configuration instead of hardcoded values. This improves security and makes it easier to manage different environments.

## Required Environment Variables

Add these environment variables in your Railway project settings:

### 1. GSS_AI_API_KEY
- **Value**: `gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ`
- **Description**: API key for GSS AI service authentication

### 2. GSS_AI_CF_WORKER_URL (Optional)
- **Value**: `https://node.gss-tec.com`
- **Description**: Cloudflare Worker URL for GSS service
- **Default**: Uses this value if not set

### 3. GSS_AI_HF_ENGINE_URL (Optional)
- **Value**: `https://Gaston895-AI.hf.space`
- **Description**: Hugging Face engine URL
- **Default**: Uses this value if not set

### 4. GSS_AI_MODEL (Optional)
- **Value**: `llama-3.1-8b-instant`
- **Description**: AI model to use
- **Default**: Uses this value if not set

### 5. GSS_AI_ENABLED (Optional)
- **Value**: `true`
- **Description**: Enable/disable Julia chat widget
- **Default**: `true` if not set

## How to Add Environment Variables on Railway

1. **Go to your Railway project dashboard**
   - URL: https://railway.app/project/[your-project-id]

2. **Navigate to your service**
   - Click on your Java application service

3. **Open Variables tab**
   - Click on the "Variables" tab in the service settings

4. **Add the environment variable**
   - Click "New Variable"
   - Enter variable name: `GSS_AI_API_KEY`
   - Enter value: `gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ`
   - Click "Add"

5. **Redeploy**
   - Railway will automatically redeploy your application with the new environment variable

## Quick Setup (Minimum Required)

For the Julia chat to work, you only need to add this ONE variable:

```
GSS_AI_API_KEY=gss_6VZZ4phctApuTplFG6dZIYj93asdSmXQ
```

The other variables have default values and are optional.

## Verification

After adding the environment variable and redeploying:

1. Open your application: https://pahappa-attendance-system-production.up.railway.app/login
2. Open browser console (F12)
3. Look for these messages:
   - ✅ "Initializing Julia GSS Client with config..."
   - ✅ "Julia GSS Client initialized successfully!"
4. Test the chat by clicking the Julia avatar and sending a message

## Troubleshooting

### Julia chat button doesn't appear
- Check if `GSS_AI_ENABLED` is set to `true`
- Check browser console for errors

### Chat appears but doesn't respond
- Verify `GSS_AI_API_KEY` is correctly set in Railway
- Check browser console for initialization errors
- Look for "Julia GSS Client initialized successfully!" message

### SDK fails to load
- Check network tab in browser console
- Verify the SDK URL is accessible: https://Gaston895-AI.hf.space/sdk/gss-sdk.js
- Check for CORS or network errors

## Security Notes

- ✅ API keys are now stored as environment variables (secure)
- ✅ Keys are not exposed in frontend code
- ✅ Keys are passed from backend to frontend at runtime
- ✅ Different keys can be used for different environments

## Next Steps

1. Add `GSS_AI_API_KEY` environment variable to Railway
2. Wait for automatic redeploy (2-3 minutes)
3. Test Julia chat functionality
4. Monitor console logs for any issues

---
**Created**: 2026-06-14
**Last Updated**: 2026-06-14