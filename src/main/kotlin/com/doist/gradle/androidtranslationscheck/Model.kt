package com.doist.gradle.androidtranslationscheck

import java.io.File

internal data class Mismatch(val language: String, val name: String, val arguments: Set<String>)

internal data class FileMismatches(val file: File, val mismatches: List<Mismatch>)

internal data class TranslationFile(
    val strings: Set<TranslationString>,
    val stringArrays: Set<TranslationStringArray>,
    val plurals: Set<TranslationPlurals>
)

internal data class TranslationString(val name: String, val arguments: Set<String>)

internal data class TranslationStringArray(val name: String, val itemsArguments: List<Set<String>>)

internal data class TranslationPlurals(
    val name: String,
    val itemsArguments: List<Pair<String, Set<String>>>
)
