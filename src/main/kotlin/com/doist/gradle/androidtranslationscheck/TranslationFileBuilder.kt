package com.doist.gradle.androidtranslationscheck

/**
 * Builds [TranslationFile] from the given [String].
 */
internal class TranslationFileBuilder(
    private val regexArg: Regex
) {
    fun buildFrom(text: String): TranslationFile {
        return TranslationFile(
            text.findTranslationStrings(),
            text.findTranslationStringArrays(),
            text.findPlurals()
        )
    }

    private fun String.findTranslationStrings(): Set<TranslationString> {
        return regexString.findAll(this).mapTo(mutableSetOf()) {
            val (name, string) = it.destructured
            TranslationString(name, string.findArguments())
        }
    }

    private fun String.findTranslationStringArrays(): Set<TranslationStringArray> {
        return regexStringArray.findAll(this).mapTo(mutableSetOf()) {
            val (name, string) = it.destructured
            TranslationStringArray(name, string.findItemsArguments())
        }
    }

    private fun String.findItemsArguments(): List<Set<String>> {
        return regexItem.findAll(this).mapTo(mutableListOf()) {
            it.destructured.component1().findArguments()
        }
    }

    private fun String.findPlurals(): Set<TranslationPlurals> {
        return regexPlurals.findAll(this).mapTo(mutableSetOf()) {
            val (name, string) = it.destructured
            TranslationPlurals(name, string.findQuantityItems())
        }
    }

    private fun String.findQuantityItems(): List<Pair<String, Set<String>>> {
        return regexQuantityItem.findAll(this).mapTo(mutableListOf()) {
            val (name, string) = it.destructured
            Pair(name, string.findArguments())
        }
    }

    private fun String.findArguments(): MutableSet<String> {
        return regexArg.findAll(this).mapTo(mutableSetOf()) {
            it.groupValues.first()
        }
    }

    companion object {
        private val regexOptions = setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)

        private val regexString = "string".toTagRegex()
        private val regexStringArray = "string-array".toTagRegex()
        private val regexPlurals = "plurals".toTagRegex()

        private val regexItem = Regex("""<item>(.*?)</item>""", regexOptions)
        private val regexQuantityItem =
            Regex("""<item quantity="([a-zA-Z0-9_]+)"[^>]*?>(.*?)</item>""", regexOptions)

        // Creates Regex matching a tag with given name.
        private fun String.toTagRegex() =
            Regex("""<$this[^>]name="([a-zA-Z0-9_]+)"[^>]*?>(.*?)</$this>""", regexOptions)
    }
}
