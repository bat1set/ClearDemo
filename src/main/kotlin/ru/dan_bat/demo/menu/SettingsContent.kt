package ru.dan_bat.demo.menu

import de.fabmax.kool.KoolSystem
import ru.dan_bat.demo.*
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.MsdfFont
import ru.dan_bat.AppFonts

class SettingsContent(val menu: DemoMenu) : Composable {
    override fun UiScope.compose() = Column {
        modifier
            .height(Grow.Std)
            .width(Grow.Std)

        Text("Settings") {
            val titleFont = MsdfFont(AppFonts.bold, sizePts = sizes.largeText.sizePts, glowColor = DemoMenu.titleTextGlowColor)
            modifier
                .width(Grow.Std)
                .height(UiSizes.baseSize)
                .padding(horizontal = UiSizes.hGap)
                .textAlignY(AlignmentY.Center)
                .background(TitleBgRenderer(DemoMenu.titleBgMesh, 0.75f, 0.95f))
                .font(titleFont)
                .textColor(Color.WHITE)
        }

        MenuRow {
            Text("UI size") { labelStyle() }
            ComboBox {
                val uiSizeOptions = Settings.defaultUiSizes.values.toList()
                modifier
                    .width(Grow.Std)
                    .margin(start = sizes.largeGap)
                    .items(uiSizeOptions)
                    .selectedIndex(uiSizeOptions.indexOf(Settings.uiSize.use()))
                    .onItemSelected {
                        Settings.uiSize.set(uiSizeOptions[it])
                    }
            }
        }
        MenuRow {
            Text("Render scale") { labelStyle() }
            ComboBox {
                modifier
                    .width(Grow.Std)
                    .margin(start = sizes.largeGap)
                    .items(renderScales.map { "$it %" })
                    .selectedIndex(renderScales.indexOf(Settings.renderScale.use()))
                    .onItemSelected {
                        Settings.renderScale.set(renderScales[it])
                        KoolSystem.requireContext().renderScale = renderScales[it] / 100f
                    }
            }
        }
        LabeledSwitch("Menu initially expanded", Settings.showMenuOnStartup)
        LabeledSwitch("Debug overlay", Settings.showDebugOverlay)
        LabeledSwitch("Fullscreen", Settings.isFullscreen)

        menu.demoLoader.activeDemo?.let {
            LabeledSwitch("Show demo menu", it.isMenu)
        }
    }

    companion object {
        val renderScales = listOf(
            50,
            67,
            75,
            100
        )
    }
}