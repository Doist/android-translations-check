package com.doist.gradle.androidtranslationscheck

/**
 * Finds [Mismatch]s between given [defaultFile] and [languageFile].
 */
internal class TranslationsValidator(
    private val defaultFile: TranslationFile,
    private val languageFile: TranslationFile,
    private val language: String
) {
    fun checkStrings(): List<Mismatch> {
        val mismatches = mutableListOf<Mismatch>()
        for (string in defaultFile.strings) {
            val languageString = languageFile.strings.find { it.name == string.name } ?: continue
            findStringMismatch(string, languageString)?.let { mismatches.add(it) }
        }

        return mismatches
    }

    fun checkStringArrays(): List<Mismatch> {
        val mismatches = mutableListOf<Mismatch>()
        for (stringArray in defaultFile.stringArrays) {
            val languageStringArray =
                languageFile.stringArrays.find { it.name == stringArray.name } ?: continue
            findStringArrayMismatch(stringArray, languageStringArray)?.let { mismatches.add(it) }
        }
        return mismatches
    }

    fun checkPlurals(): List<Mismatch> {
        val mismatches = mutableListOf<Mismatch>()
        for (plurals in defaultFile.plurals) {
            val languagePlurals = languageFile.plurals.find { it.name == plurals.name } ?: continue
            findPluralsMismatch(plurals, languagePlurals)?.let { mismatches.add(it) }
        }

        return mismatches
    }

    private fun findStringMismatch(
        string: TranslationString,
        languageString: TranslationString
    ): Mismatch? {
        if (string.arguments != languageString.arguments) {
            return Mismatch(
                language,
                string.name,
                string.arguments.difference(languageString.arguments)
            )
        }

        return null
    }

    private fun findStringArrayMismatch(
        defaultStringArray: TranslationStringArray,
        languageStringArray: TranslationStringArray
    ): Mismatch? {
        if (defaultStringArray.itemsArguments.size != languageStringArray.itemsArguments.size) {
            return Mismatch(
                language,
                defaultStringArray.name,
                setOf("String array has different item count")
            )
        }

        for (i in defaultStringArray.itemsArguments.indices) {
            val arguments = defaultStringArray.itemsArguments.get(i)
            val languageArguments = languageStringArray.itemsArguments[i]
            if (arguments != languageArguments) {
                return Mismatch(
                    language,
                    defaultStringArray.name,
                    arguments.difference(languageArguments)
                )
            }
        }

        return null
    }

    private fun findPluralsMismatch(
        plurals: TranslationPlurals,
        languagePlurals: TranslationPlurals
    ): Mismatch? {
        // Compare arguments count from the first source form to all the translation forms.
        val argumentsSize = plurals.itemsArguments.first().second.size
        for ((_, arguments) in languagePlurals.itemsArguments) {
            if (arguments.size != argumentsSize) {
                return Mismatch(
                    language,
                    plurals.name,
                    setOf("Plurals have different item count")
                )
            }
        }

        // Compare argument names from the first source form to all the translation forms.
        val arguments = plurals.itemsArguments.first().second
        for ((_, languageArguments) in languagePlurals.itemsArguments) {
            if (languageArguments != arguments) {
                return Mismatch(
                    language,
                    plurals.name,
                    arguments.difference(languageArguments)
                )
            }
        }

        return null
    }

    private fun <T> Set<T>.difference(set: Set<T>) = (this - set) + (set - this)
}
