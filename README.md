## Other languages

[Russian](https://github.com/bat1set/ClearDemo/blob/master/readme/ru_ru.md) [Deutsch](https://github.com/bat1set/ClearDemo/blob/master/readme/de_de.md)

## Description

This project is an adapted version of the demonstration example provided by [kool-engine](https://github.com/kool-engine/kool/tree/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo). The primary difference lies in the inclusion of a modification that allows the use of custom fonts.

## Note

To add your own scene, you need to subclass the `DemoScene` class. After implementing your class, add a new `entry` to your chosen category in the `Demos` file, for example:

```kotlin
entry("empty", "Empty Demo") { EmptyDemo() }
```

If you wish to create your own demo category, you should instantiate a `Category` object, assign it to a variable, and then add that variable to the `categories` list, for example:

```kotlin
val mainCategory = Category("Demos", false, 0f, 1f).apply {
    // your entries
}

val categories = mutableListOf(mainCategory)
```

If you plan to use custom fonts in your scene, you need to define them when calling `addPanelSurface`, for example:

```kotlin
addPanelSurface(
    sizes = Sizes.medium(
        normalText = MsdfFont(AppFonts.regular, 15f),
        largeText = MsdfFont(AppFonts.bold, 20f)
    )
)
```

## License Information

This project **is not an official demo project** of kool-engine. However, it is distributed under the same terms as the main kool-engine project, in accordance with the **Apache License, Version 2.0, January 2004**. Detailed licensing information is available on the [kool-engine main page](https://github.com/kool-engine/kool).

Please note that, due to its independent development, this version may differ from the official demonstrations in both structure and currency.
