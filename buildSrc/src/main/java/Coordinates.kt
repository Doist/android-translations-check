object PluginCoordinates {
    const val ID = "com.doist.gradle.android-translations-check"
    const val GROUP = "com.doist.gradle"
    const val VERSION = "0.0.2"
    const val IMPLEMENTATION_CLASS = "com.doist.gradle.androidtranslationscheck.AndroidTranslationsCheckPlugin"
}

object PluginBundle {
    const val VCS = "https://github.com/Doist/android-translations-check"
    const val WEBSITE = "https://github.com/Doist/android-translations-check"
    const val DESCRIPTION = "Gradle plugin to check Android translation strings."
    const val DISPLAY_NAME = "Android translations check"
    val TAGS = listOf(
        "plugin",
        "gradle",
        "android",
        "translations"
    )
}
