package com.modark.pubgunlocker.utils

import android.content.Context
import android.os.Environment
import com.modark.pubgunlocker.models.SkinData
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile

class FileUtils(private val context: Context) {

    private val GAME_PACKAGE = "com.tencent.ig"

    fun findPubgPath(): String? {
        val paths = listOf(
            "/storage/emulated/0/Android/data/$GAME_PACKAGE/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks"
        )

        for (path in paths) {
            val dir = File(path)
            if (dir.exists() && dir.isDirectory) {
                return path
            }
        }
        return null
    }

    fun modifyPakFile(gamePath: String, skin: SkinData): Boolean {
        return try {
            val pakFile = File(gamePath, skin.pakName)
            if (!pakFile.exists()) return false

            // عمل نسخة احتياطية أولاً إذا لم تكن موجودة
            backupFile(pakFile)

            val raf = RandomAccessFile(pakFile, "rw")
            val offset = skin.offset.toLong()
            raf.seek(offset)

            val originalValue = skin.originalCode.toByteArray(Charsets.UTF_8)
            val buffer = ByteArray(originalValue.size)
            raf.read(buffer)

            val unlockValue = skin.unlockCode.toByteArray(Charsets.UTF_8)
            raf.seek(offset)
            raf.write(unlockValue)
            raf.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun backupFile(file: File): Boolean {
        return try {
            val backupFile = File("${file.absolutePath}.bak")
            if (backupFile.exists()) return true

            val src = FileInputStream(file).channel
            val dest = FileOutputStream(backupFile).channel
            dest.transferFrom(src, 0, src.size())
            src.close()
            dest.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun restoreOriginal(gamePath: String): Boolean {
        return try {
            val dir = File(gamePath)
            val files = dir.listFiles() ?: return false
            var success = true

            for (file in files) {
                if (file.name.endsWith(".bak")) {
                    val originalFile = File(dir, file.name.substringBeforeLast(".bak"))
                    val src = FileInputStream(file).channel
                    val dest = FileOutputStream(originalFile).channel
                    dest.transferFrom(src, 0, src.size())
                    src.close()
                    dest.close()
                }
            }
            success
        } catch (e: Exception) {
            false
        }
    }
}
