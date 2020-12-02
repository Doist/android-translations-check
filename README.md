# Android Translations Check Gradle Plugin

This Gradle plugin checks Android project translation strings. 

In Android project strings included in alternative resources (i.e. `/src/main/res/values-pl/`) must 
match default strings located in `/src/main/res/values/`. If the number of arguments or number of
entries in string-array is not the same, your app will likely crash. 

This plugin checks if all translation strings match original ones. It will check strings included 
in:
- `<string name="...">...</>`
- `<plurals name="...">...</>`
- `<string-array name="...">...</>`

## How to use it 👣

Include (but don't apply) the plugin and specify the version in your root `build.gradle.kts` file:

```
plugins {
    id("com.doist.gradle.android-translations-check") version "<latest-version>" apply false
}
```

And include the plugin in all `build.gradle.kts` modules with strings:  
```
plugins {
    id("com.doist.gradle.android-translations-check")
}
```

This plugin adds the `checkAndroidTranslations` Gradle task, so to check all translation strings,
run:
```
./gradlew checkAndroidTranslations
``` 

## Configuration ⚙️

The plugin runs without any configurations but in case you use any custom library for string 
formatting like [Phrase](https://github.com/square/phrase) you can customize arguments regex. To do 
this, include the following in your module `build.gradle.kts` file:
```
checkAndroidTranslationsConfig {
    argumentRegex.set("""(?:%(?:[1-9]\$)?[sd%])|(?:\{[a-z_]+})""")
}
``` 

## Contributing 🤝

Feel free to open an issue or submit a pull request for any bugs/improvements.

## Acknowledgements 🙏
This plugin is based on [kotlin-gradle-plugin-template 🐘](https://github.com/cortinico/kotlin-gradle-plugin-template)
