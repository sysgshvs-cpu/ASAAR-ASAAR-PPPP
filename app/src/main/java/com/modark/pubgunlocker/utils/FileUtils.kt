package com.modark.pubgunlocker.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {

    fun copyAssetToTarget(context: Context, assetFileName: String, targetPath: String): Boolean {
        return try {
            val targetFile = File(targetPath)
            targetFile.parentFile?.mkdirs()

            context.assets.open(assetFileName).use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun restoreOriginalFile(targetPath: String): Boolean {
        val file = File(targetPath)
        return if (file.exists()) {
            file.delete()
        } else {
            true
        }
    }
}
