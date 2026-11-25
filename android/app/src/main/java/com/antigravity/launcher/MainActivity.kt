package com.antigravity.launcher

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchImmersiveTab()
        finish()
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
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .build()

        if (isChromeInstalled()) {
            customTabs.intent.setPackage(CHROME_PKG)
        }

        try {
            customTabs.launchUrl(this, uri)
        } catch (_: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    companion object {
        private const val CHROME_PKG = "com.android.chrome"
        private const val TARGET_URL = "https://antigravity.google.com"
    }
}
