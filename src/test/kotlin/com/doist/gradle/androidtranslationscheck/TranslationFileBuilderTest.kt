package com.doist.gradle.androidtranslationscheck

import groovy.util.GroovyTestCase.assertEquals
import org.junit.Test

class TranslationFileBuilderTest {
    @Test
    fun `translation file is build from text`() {
        val translationFileBuilder = TranslationFileBuilder("""(?:%(?:[1-9]\$)?[sd%])""".toRegex())
        val text = """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>

                <string name="app_name">The app name is %s</string>
            
                <plurals name="feedback_completed">
                    <item quantity="one">%d completed.</item>
                    <item quantity="other">%d completed.</item>
                </plurals>
                
                <string-array name="feedback_emoji" translatable="false">
                    <item>🌻</item>
                    <item>🏆</item>
                    <item>🏆 x %d</item>
                </string-array>

                <string name="error">Oops, there is an error.</string>
            
            </resources>
        """.trimIndent()

        val translationFile = translationFileBuilder.buildFrom(text)

        assertEquals(
            setOf(
                TranslationString("app_name", setOf("%s")),
                TranslationString("error", emptySet())
            ),
            translationFile.strings
        )

        assertEquals(
            setOf(
                TranslationPlurals(
                    "feedback_completed",
                    listOf("one" to setOf("%d"), "other" to setOf("%d"))
                )
            ),
            translationFile.plurals
        )

        assertEquals(
            setOf(
                TranslationStringArray(
                    "feedback_emoji",
                    listOf(emptySet(), emptySet(), setOf("%d"))
                )
            ),
            translationFile.stringArrays
        )
    }
}
