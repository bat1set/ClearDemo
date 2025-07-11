## Описание

Данный проект представляет собой адаптированную версию демонстрационного примера от [kool-engine](https://github.com/kool-engine/kool/tree/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo). Основное отличие заключается в наличии модификации, позволяющей использовать собственные шрифты.

## Примечание

Для добавления собственной сцены, вам необходимо унаследовать класс `DemoScene`, после реализации класса, вам нужно в файле `Demos` добавить новый `entry` в вашу категорию, например:

```kotlin
entry("empty", "Empty Demo") { EmptyDemo() }
```

Если вы хотите сделать свою категорию демо, вам необходимо создать объект класса `Category`, при этом передав его в переменную и после саму переменную добавить в список `categories`, например:
```kotlin
val mainCategory = Category("Demos", false, 0f, 1f).apply {
    // выши entry
}

val categories = mutableListOf(mainCategory)
```

Если вы планируете использовать пользовательские шрифты в сцене, необходимо задать их при вызове `addPanelSurface`, например:

```kotlin
addPanelSurface(
    sizes = Sizes.medium(
        normalText = MsdfFont(AppFonts.regular, 15f),
        largeText = MsdfFont(AppFonts.bold, 20f)
    )
)
```

## Лицензионная информация

Данный проект **не является официальным демо-проектом** kool-engine. Тем не менее, он распространяется на тех же условиях, что и основной проект kool-engine, в соответствии с лицензией **Apache License, Version 2.0, January 2004**. Подробная информация о лицензии доступна на [главной странице kool-engine](https://github.com/kool-engine/kool).

Следует учитывать, что в связи с независимостью от основного проекта, представленная версия может отличаться от официальных демонстраций как по структуре, так и по актуальности.
