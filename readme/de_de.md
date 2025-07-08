## Beschreibung

Dieses Projekt ist eine angepasste Version des Demo-Beispiels von [kool-engine](https://github.com/kool-engine/kool/tree/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo). Der Hauptunterschied besteht in einer Modifikation, die die Verwendung benutzerdefinierter Schriftarten ermöglicht.

## Hinweis

Um eine eigene Szene hinzuzufügen, müssen Sie eine Unterklasse von `DemoScene` erstellen. Nachdem Sie Ihre Klasse implementiert haben, fügen Sie im `Demos`-Datei einen neuen `entry` in die gewünschte Kategorie ein, zum Beispiel:

```kotlin
entry("empty", "Empty Demo") { EmptyDemo() }
```

Wenn Sie eine eigene Demo-Kategorie erstellen möchten, müssen Sie ein Objekt der Klasse `Category` instanziieren, es einer Variablen zuweisen und diese Variable anschließend der `categories`-Liste hinzufügen, zum Beispiel:

```kotlin
val mainCategory = Category("Demos", false, 0f, 1f).apply {
    // Ihre entries
}

val categories = mutableListOf(mainCategory)
```

Wenn Sie in Ihrer Szene benutzerdefinierte Schriftarten verwenden möchten, müssen diese beim Aufruf von `addPanelSurface` definiert werden, zum Beispiel:

```kotlin
addPanelSurface(
    sizes = Sizes.medium(
        normalText = MsdfFont(AppFonts.regular, 15f),
        largeText = MsdfFont(AppFonts.bold, 20f)
    )
)
```

## Lizenzinformationen

Dieses Projekt ist **kein offizielles Demo-Projekt** von kool-engine. Es wird jedoch unter denselben Bedingungen wie das Hauptprojekt kool-engine veröffentlicht, gemäß der **Apache License, Version 2.0, Januar 2004**. Detaillierte Lizenzinformationen finden Sie auf der [Hauptseite von kool-engine](https://github.com/kool-engine/kool).

Bitte beachten Sie, dass diese Version aufgrund ihrer Unabhängigkeit vom Hauptprojekt sowohl in ihrer Struktur als auch in ihrer Aktualität von den offiziellen Demos abweichen kann.
