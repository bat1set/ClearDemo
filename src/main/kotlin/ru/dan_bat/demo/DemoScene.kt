package ru.dan_bat.demo

import de.fabmax.kool.Assets
import de.fabmax.kool.KoolContext
import de.fabmax.kool.KoolSystem
import de.fabmax.kool.math.Vec2i
import de.fabmax.kool.modules.gltf.GltfLoadConfig
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.modules.ui2.docking.UiDockable
import de.fabmax.kool.pipeline.MipMapping
import de.fabmax.kool.pipeline.SamplerSettings
import de.fabmax.kool.pipeline.TexFormat
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.util.*
import ru.dan_bat.AppFonts
import ru.dan_bat.demo.menu.DemoMenu
import ru.dan_bat.demo.menu.TitleBgRenderer

abstract class DemoScene(val name: String, val mainScene: Scene = Scene(name)) {
    var demoEntry: Demos.Entry? = null
    var demoState = State.NEW

    protected val resources = ResourceGroup()

    var menuUi: UiSurface? = null
    val scenes = mutableListOf(mainScene)

    val isMenu = mutableStateOf(true)
    val isMenuMinimized = mutableStateOf(false)
    private val titleBgMesh = TitleBgRenderer.BgMesh()

    private val menuDockable = UiDockable(
        name,
        floatingX = UiSizes.baseSize * 2f,
        floatingY = UiSizes.baseSize * 2f,
        floatingWidth = UiSizes.menuWidth,
        floatingHeight = FitContent,
        floatingAlignmentX = AlignmentX.End
    )

    var demoLoader: DemoLoader? = null
    var loadingScreen: LoadingScreen? = null
        set(value) {
            field = value
            value?.loadingText1?.set("Loading $name")
            value?.loadingText2?.set("")
        }

    init {
        resources.releaseWith(mainScene)
        resources.loadInfoCallback = {
            loadingScreen?.loadingText2?.set("${it.name}...")
        }
        mainScene.onRelease { onRelease(KoolSystem.requireContext()) }
    }

    suspend fun showLoadText(text: String, delayFrames: Int = 1) {
        loadingScreen?.let { ls ->
            ls.loadingText2.set(text)
            delayFrames(delayFrames)
        }
    }

    fun checkDemoState(loader: DemoLoader, ctx: KoolContext) {
        if (demoState == State.NEW) {
            demoState = State.LOADING
            launchOnMainThread {
                resources.loadParallel()
                Assets.loadResources(ctx)
                demoState = State.SETUP
            }
        }

        if (demoState == State.SETUP) {
            setupScenes(loader.menu, ctx)
            demoState = State.RUNNING
        }
    }

    private fun setupScenes(menu: DemoMenu, ctx: KoolContext) {
        mainScene.setupMainScene(ctx)
        menuUi = createMenu(menu, ctx)
        menuUi?.let { menu.ui.addNode(it, 0) }
        lateInit(ctx)
    }

    open suspend fun Assets.loadResources(ctx: KoolContext) { }

    abstract fun Scene.setupMainScene(ctx: KoolContext)

    open fun createMenu(menu: DemoMenu, ctx: KoolContext): UiSurface? {
        return null
    }

    open fun lateInit(ctx: KoolContext) { }

    open fun onRelease(ctx: KoolContext) { }

    protected fun hdriGradient(gradient: ColorGradient) = resources.hdriGradient(gradient)
    protected fun hdriImage(path: String, brightness: Float = 1f) = resources.hdriImage(path, brightness)
    protected fun hdriSingleColor(color: Color) = resources.hdriSingleColor(color)
    protected fun model(path: String, config: GltfLoadConfig) = resources.model(path, config)
    protected fun texture2d(
        path: String,
        format: TexFormat = TexFormat.RGBA,
        mipMapping: MipMapping = MipMapping.Full,
        samplerSettings: SamplerSettings = SamplerSettings(),
        resolveSize: Vec2i? = null
    ) = resources.texture2d(path, format, mipMapping, samplerSettings, resolveSize)

    protected fun menuSurface(title: String? = null, block: ColumnScope.() -> Unit): UiSurface {
        val accent = demoEntry?.color ?: MdColor.PINK
        val titleTxt = title ?: demoEntry?.title ?: "Demo"

        return WindowSurface(
            menuDockable,
            colors = Colors.singleColorDark(accent, Color("101010d0"))
        ) {
            if (!isMenu.use()) {
                menuDockable.setFloatingBounds(
                    x = UiSizes.baseSize * 2f,
                    y = UiSizes.baseSize * 2f,
                    width = UiSizes.menuWidth,
                    height = FitContent,
                    alignmentX = AlignmentX.End,
                    alignmentY = AlignmentY.Top
                )
                isMenuMinimized.set(false)
                // hide window
                surface.isVisible = false
                return@WindowSurface
            }

            surface.isVisible = true
            surface.sizes = Settings.uiSize.use().sizes

            Column(Grow.Std, Grow.Std) {
                val cornerRadius = sizes.gap

                TitleBar(titleTxt, cornerRadius)

                if (!isMenuMinimized.use()) {
                    ScrollArea(
                        withHorizontalScrollbar = false,
                        containerModifier = { it.background(null) }
                    ) {
                        modifier.width(Grow.Std).margin(top = sizes.smallGap, bottom = sizes.smallGap * 0.5f)
                        Column(width = Grow.Std, block = block)
                    }
                }
            }
        }
    }

    private fun UiScope.TitleBar(titleTxt: String, cornerRadius: Dp) {
        val titleFrom = demoEntry?.category?.fromColor ?: 0f
        val titleTo = demoEntry?.category?.toColor ?: 0.2f

        var isMinimizeHovered by remember(false)

        Box {
            modifier
                .width(Grow.Std)
                .height(UiSizes.baseSize)
                .background(RoundRectBackground(colors.primary, cornerRadius))

            with(menuDockable) { registerDragCallbacks() }

            val titleFont = MsdfFont(AppFonts.bold, sizePts = sizes.largeText.sizePts, glowColor = DemoMenu.titleTextGlowColor)
            Text(titleTxt) {
                val bgRadius = cornerRadius.px + 1f
                val bottomRadius = if (isMenuMinimized.use()) bgRadius else 0f
                modifier
                    .width(Grow.Std)
                    .height(UiSizes.baseSize)
                    .background(TitleBgRenderer(titleBgMesh, titleFrom, titleTo, bgRadius, bottomRadius))
                    .textColor(colors.onPrimary)
                    .font(titleFont)
                    .textAlign(AlignmentX.Center, AlignmentY.Center)
            }

            val minButtonBgColor = if (isMinimizeHovered) MdColor.RED tone 600 else Color.WHITE.withAlpha(0.8f)
            Box {
                modifier
                    .size(sizes.gap * 1.75f, sizes.gap * 1.75f)
                    .align(AlignmentX.End, AlignmentY.Center)
                    .margin(end = sizes.gap * 1.2f)
                    .background(CircularBackground(minButtonBgColor))
                    .zLayer(UiSurface.LAYER_FLOATING)

                Arrow(if (isMenuMinimized.use()) 90f else -90f) {
                    modifier
                        .size(Grow.Std, Grow.Std)
                        .margin(sizes.smallGap * 0.7f)
                        .colors(colors.primaryVariant, Color.WHITE)
                        .onEnter { isMinimizeHovered = true }
                        .onExit { isMinimizeHovered = false }
                        .onClick {
                            isMenuMinimized.toggle()
                            if (isMenuMinimized.value) {
                                menuDockable.setFloatingBounds(height = FitContent)
                            }
                        }
                }
            }
        }
    }

    enum class State {
        NEW,
        LOADING,
        SETUP,
        RUNNING
    }
}