package ru.dan_bat

import de.fabmax.kool.Assets
import de.fabmax.kool.loadBlob
import de.fabmax.kool.loadTexture2d
import de.fabmax.kool.modules.ui2.Sizes
import de.fabmax.kool.pipeline.MipMapping
import de.fabmax.kool.util.*
import kotlinx.serialization.json.Json


object AppFonts {
    lateinit var regular: MsdfFontData
        private set
    lateinit var bold: MsdfFontData
        private set

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Downloads all the fonts necessary for the application.
     * Should be caused asynchronously at the very beginning of the application.
     */
    suspend fun init() {
        regular = loadFontData("fonts/montserrat-regular.json", "fonts/montserrat-regular.png")
        bold = loadFontData("fonts/montserrat-bold.json", "fonts/montserrat-bold.png")
    }

    private suspend fun loadFontData(metaPath: String, texturePath: String): MsdfFontData {
        val meta = Assets.loadBlob(metaPath).getOrThrow().decodeToString()
        val fontMeta = json.decodeFromString<MsdfMeta>(meta)
        val tex = Assets.loadTexture2d(texturePath, mipMapping = MipMapping.Off).getOrThrow()
        return MsdfFontData(tex, fontMeta)
    }
}
