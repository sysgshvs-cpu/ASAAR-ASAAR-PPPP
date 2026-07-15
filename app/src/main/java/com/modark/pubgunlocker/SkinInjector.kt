package com.modark.pubgunlocker

import android.content.Context
import com.modark.pubgunlocker.models.SkinData
import com.modark.pubgunlocker.utils.FileUtils
import java.io.File

object SkinInjector {

    private val targetDirectory = "/sdcard/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks"

    val skinsList = listOf(
        SkinData(
            id = "m416_glacier",
            name = "M416 Glacier",
            category = "weapons",
            sourcePath = "skins/m416_glacier.pak",
            targetPath = "$targetDirectory/game_patch_m416.pak"
        ),
        SkinData(
            id = "gargoyle_suit",
            name = "Gargoyle Suit",
            category = "outfits",
            sourcePath = "skins/gargoyle_suit.pak",
            targetPath = "$targetDirectory/game_patch_suit.pak"
        )
    )

    fun injectSkin(context: Context, skinId: String): Boolean {
        val skin = skinsList.find { it.id == skinId } ?: return false
        return FileUtils.copyAssetToTarget(context, skin.sourcePath, skin.targetPath)
    }

    fun removeSkin(skinId: String): Boolean {
        val skin = skinsList.find { it.id == skinId } ?: return false
        return FileUtils.restoreOriginalFile(skin.targetPath)
    }
}
