// 2026/10/22 19:09:52 by Aerial Custom Contributors
package app.orchard.tv.wallpaper

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

/** Uses Aerial Views' public preview activity; Back returns to the launcher. */
object AerialViewsBridge {
    private const val PACKAGE = "com.neilturner.aerialviews"
    private val preview = ComponentName(PACKAGE, "$PACKAGE.ui.screensaver.TestActivity")
    val dream = ComponentName(PACKAGE, "$PACKAGE.ui.screensaver.DreamActivity")

    /** The selected system dream already owns idle entry; Home must not start a second player. */
    fun isSystemScreensaverEnabled(context: Context): Boolean = runCatching {
        val resolver = context.contentResolver
        Settings.Secure.getInt(resolver, "screensaver_enabled", 0) != 0 &&
            Settings.Secure.getString(resolver, "screensaver_components")
                ?.split(',')
                ?.any { ComponentName.unflattenFromString(it.trim()) == dream } == true
    }.getOrDefault(false)

    fun available(context: Context): Boolean = runCatching {
        val info = context.packageManager.getActivityInfo(preview, 0)
        info.enabled && info.exported && info.applicationInfo.enabled
    }.getOrDefault(false)

    fun start(activity: Activity): Boolean {
        if (!available(activity)) return false
        return runCatching {
            activity.startActivity(Intent().setComponent(preview))
            true
        }.onFailure { Log.w("AerialViewsBridge", "Could not open screensaver", it) }
            .getOrDefault(false)
    }
}
