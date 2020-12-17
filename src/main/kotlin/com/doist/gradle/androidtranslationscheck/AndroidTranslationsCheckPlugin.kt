package com.doist.gradle.androidtranslationscheck

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

@Suppress("unused")
abstract class AndroidTranslationsCheckPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val extension = extensions.create<AndroidTranslationsCheckExtension>(
            "checkAndroidTranslationsConfig",
            this
        )

        val task = tasks.register<AndroidTranslationsCheckTask>("checkAndroidTranslations") {
            argumentRegex.set(extension.argumentRegex)
        }

        tasks
            .matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }
            .configureEach { dependsOn(task) }
    }
}
