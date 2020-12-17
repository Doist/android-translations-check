package com.doist.gradle.androidtranslationscheck

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class AndroidTranslationsCheckPluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.doist.gradle.android-translations-check")

        assert(project.tasks.getByName("checkAndroidTranslations") is AndroidTranslationsCheckTask)
    }
}
