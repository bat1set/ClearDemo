package ru.dan_bat.demo.scenes

import de.fabmax.kool.KoolContext
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.scene.Scene
import ru.dan_bat.demo.DemoScene
import ru.dan_bat.demo.menu.DemoMenu

class EmptyDemo : DemoScene("Empty Demo") {
    override fun Scene.setupMainScene(ctx: KoolContext) {
    }

    override fun createMenu(menu: DemoMenu, ctx: KoolContext): UiSurface {
        return menuSurface {
            Text("This is an empty demo scenes") {
                modifier.width(Grow.Std).padding(sizes.gap).alignX(AlignmentX.Center)
            }
            Text("Add your scenes to demos.kt") {
                modifier.width(Grow.Std).padding(sizes.gap).alignX(AlignmentX.Center)
            }
        }
    }
}