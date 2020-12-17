package com.doist.gradle.androidtranslationscheck

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

@Suppress("UnstableApiUsage")
open class AndroidTranslationsCheckExtension(project: Project) {
    val argumentRegex: Property<String> =
        project.objects.property<String>().convention("""(?:%(?:[1-9]\$)?[sd%])""")
}
