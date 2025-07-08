package ru.dan_bat.demo

import ru.dan_bat.demo.scenes.fog.FogDemo
import de.fabmax.kool.KoolContext
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.ColorGradient
import de.fabmax.kool.util.MdColor
import ru.dan_bat.demo.scenes.*
import ru.dan_bat.demo.scenes.shapezone.SimpleShapesZone
import kotlin.math.max

object Demos {
    val demoColors = ColorGradient(
        0f to MdColor.AMBER,
        0.2f to MdColor.DEEP_ORANGE,
        0.25f to MdColor.PINK,
        0.5f to MdColor.PURPLE,
        0.7f to MdColor.BLUE,
        1f to MdColor.CYAN,
        n = 512
    )

    val mainCategory = Category("Demos", false, 0f, 1f).apply {
        entry("empty", "Empty Demo") { EmptyDemo() }
        // Add your demo here:
        // entry("my-demo-id", "The name of my demo") { MyDemoClass() }
    }

    val categories = mutableListOf(mainCategory)
    val demos = categories.flatMap { it.entries }.associateBy { it.id }.toMutableMap()

    val defaultDemo = "empty"

    class Category(val title: String, val isHidden: Boolean, val fromColor: Float, val toColor: Float) {
        val entries = mutableListOf<Entry>()

        fun getCategoryColor(f: Float): Color {
            return demoColors.getColor(fromColor + f * (toColor - fromColor))
        }

        fun entry(id: String, title: String, factory: (KoolContext) -> DemoScene) {
            entries += Entry(this, id, title, factory)
        }
    }

    class Entry(
        val category: Category,
        val id: String,
        val title: String,
        val newInstance: (KoolContext) -> DemoScene
    ) {
        val color: Color
            get() {
                if (category.entries.isEmpty() || category.entries.lastIndex == 0) {
                    return category.getCategoryColor(0.5f)
                }
                val catIdx = max(0, category.entries.indexOf(this)).toFloat()
                val gradientF = catIdx / category.entries.lastIndex
                return category.getCategoryColor(gradientF)
            }
    }
}