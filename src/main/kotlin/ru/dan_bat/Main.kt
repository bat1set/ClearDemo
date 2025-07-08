package ru.dan_bat

import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.KoolContext
import ru.dan_bat.demo.DemoLoader

fun main() = KoolApplication(
    KoolConfigJvm(
        renderBackend = KoolConfigJvm.Backend.OPEN_GL,
        windowTitle = "Kool Testbed"
    )
) {
    AppFonts.init()
    demo("empty", ctx)
}

fun demo(startScene: String? = null, ctx: KoolContext) {
    DemoLoader(ctx, startScene)
}