package com.doist.gradle.androidtranslationscheck

import org.gradle.kotlin.dsl.support.listFilesOrdered
import java.io.File

/**
 * Finds all [Mismatch]s in the Android res directory.
 */
internal class MismatchFinder(
    private val resDir: File,
    private val translationFileBuilder: TranslationFileBuilder
) {
    fun findMismatches(): List<FileMismatches> {
        val defaultValuesDir = File(resDir, "values")
        val defaultStringFiles = defaultValuesDir.listFilesOrdered { it.isStringFile() }
        val alternativeValuesDirs = resDir.listFilesOrdered { it.name.startsWith("values-") }

        return defaultStringFiles
            .map { FileMismatches(it, checkTranslationFiles(it, alternativeValuesDirs)) }
            .filter { it.mismatches.isNotEmpty() }
    }

    private fun checkTranslationFiles(
        defaultStringFile: File,
        alternativeValuesDirs: List<File>
    ): List<Mismatch> {
        val mismatches = mutableListOf<Mismatch>()
        val defaultTranslationFile = translationFileBuilder.buildFrom(defaultStringFile.readText())

        for (alternativeValuesDir in alternativeValuesDirs) {
            val languageFile = File(alternativeValuesDir, defaultStringFile.name)

            if (!languageFile.exists()) continue

            val language = alternativeValuesDir.name.removePrefix("values-")
            val languageTranslationFile = translationFileBuilder.buildFrom(languageFile.readText())
            val translationsValidator = TranslationsValidator(
                defaultTranslationFile,
                languageTranslationFile,
                language
            )
            mismatches.addAll(translationsValidator.checkStrings())
            mismatches.addAll(translationsValidator.checkStringArrays())
            mismatches.addAll(translationsValidator.checkPlurals())
        }

        return mismatches
    }

    private fun File.isStringFile() = this.name.startsWith("strings") && this.name.endsWith(".xml")
}
