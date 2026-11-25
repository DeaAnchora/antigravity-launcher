package com.antigravity.launcher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : Activity() {
    private var launched = false
    private var hasPaused = false
    private var relaunchOnResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            launched = savedInstanceState.getBoolean(STATE_LAUNCHED, false)
            hasPaused = savedInstanceState.getBoolean(STATE_HAS_PAUSED, false)
        }
        if (!launched) {
            launched = true
            hasPaused = false
            launchImmersiveTab()
        } else if (hasPaused) {
            moveTaskToBack(true)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Re-tapping the home screen icon relaunches the Custom Tab cleanly.
        relaunchOnResume = true
    }

    override fun onPause() {
        super.onPause()
        if (launched) {
            hasPaused = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (relaunchOnResume) {
            relaunchOnResume = false
            launched = true
            hasPaused = false
            launchImmersiveTab()
            return
        }
        if (launched && hasPaused) {
            // Move task to background instead of finish() so Custom Tabs minimize (PiP) stays alive.
            moveTaskToBack(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_LAUNCHED, launched)
        outState.putBoolean(STATE_HAS_PAUSED, hasPaused)
    }

    private fun isChromeInstalled(): Boolean {
        return try {
            packageManager.getPackageInfo(CHROME_PKG, 0)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun launchImmersiveTab() {
        val uri = Uri.parse(TARGET_URL)

        val lightColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(Color.WHITE)
            .setSecondaryToolbarColor(Color.WHITE)
            .setNavigationBarColor(Color.WHITE)
            .setNavigationBarDividerColor(Color.TRANSPARENT)
            .build()

        val darkColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(Color.parseColor("#121212"))
            .setSecondaryToolbarColor(Color.parseColor("#121212"))
            .setNavigationBarColor(Color.parseColor("#121212"))
            .setNavigationBarDividerColor(Color.TRANSPARENT)
            .build()

        val customTabs = CustomTabsIntent.Builder()
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .setDefaultColorSchemeParams(lightColors)
            .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, darkColors)
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .setBookmarksButtonEnabled(false)
            .setDownloadButtonEnabled(false)
            .setInstantAppsEnabled(false)
            .build()

        if (isChromeInstalled()) {
            customTabs.intent.setPackage(CHROME_PKG)
        }

        try {
            customTabs.launchUrl(this, uri)
        } catch (_: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            moveTaskToBack(true)
        }
    }

    companion object {
        private const val CHROME_PKG = "com.android.chrome"
        private const val TARGET_URL = "https://antigravity.google.com"
        private const val STATE_LAUNCHED = "state_launched"
        private const val STATE_HAS_PAUSED = "state_has_paused"
    }
}
