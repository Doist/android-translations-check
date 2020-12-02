package com.doist.gradle.androidtranslationscheck

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask
import org.gradle.kotlin.dsl.property
import org.gradle.language.base.plugins.LifecycleBasePlugin

abstract class AndroidTranslationsCheckTask : DefaultTask(), VerificationTask {
    init {
        description = "Checks Android project translation strings."
        group = LifecycleBasePlugin.VERIFICATION_GROUP
    }

    @get:Input
    val argumentRegex: Property<String> = project.objects.property()

    @TaskAction
    fun checkTranslationsAction() {
        val translationFileBuilder = TranslationFileBuilder(argumentRegex.get().toRegex())
        val resDir = project.file("src/main/res")

        val mismatchFinder = MismatchFinder(resDir, translationFileBuilder)
        val mismatches = mismatchFinder.findMismatches()

        if (mismatches.isNotEmpty()) {
            logger.error(buildErrorMessage(mismatches))
            throw GradleException("Translations are broken.")
        }
    }

    private fun buildErrorMessage(mismatches: List<FileMismatches>) =
        mismatches.joinToString("\n") { buildErrorMessage(it) }

    private fun buildErrorMessage(fileMismatches: FileMismatches) = buildString {
        appendln(fileMismatches.file.name)
        appendln("=".repeat(fileMismatches.file.name.length))
        fileMismatches.mismatches.forEach {
            appendln("${it.name} (${it.language}): ${it.arguments}")
        }
    }
}
