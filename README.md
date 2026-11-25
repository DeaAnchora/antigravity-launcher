# Antigravity Launcher

A lightweight, zero-background Android launcher wrapper that opens `https://antigravity.google.com` in an immersive Chrome Custom Tab with properly themed Light/Dark status bar and navigation bar colors, reusing Chrome's existing Google session without running any background service.

## Features
- Native Light (`#FFFFFF`) and Dark (`#121212`) status bar & gesture navigation bar matching system theme
- Auto-hiding top bar when scrolling (`setUrlBarHidingEnabled(true)`)
- Explicitly targets Chrome (`com.android.chrome`) so existing Google sign-in works seamlessly
- Zero background processes, zero battery drain, no extra permissions required
