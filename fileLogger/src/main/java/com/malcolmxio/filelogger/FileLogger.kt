package com.malcolmxio.filelogger

import android.content.Context
import androidx.core.content.ContextCompat
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

object FileLogger {

    /**
     * Method writes log to the specified file.
     *
     * @param context - applicationContext in general
     * @param text - text to be written to the log file
     * @param dirName - directory of log file
     * @param fileName - name of log file
     * @param tsFormat - every logged text will be enriched with current formatted timestamp
     */
    fun appendLog(
        context: Context,
        text: String,
        dirName: String,
        fileName: String,
        tsFormat: String
    ) {
        val absPath = ContextCompat.getExternalFilesDirs(context, null)[0]
        val fileDir = "$absPath" + dirName
        val directory = File(fileDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val logFile = File(
            directory,
            fileName
        )
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        try {
            val currentTimeStamp =
                getCurrentTimestamp(tsFormat, Locale("ru"))
            val fileWriter = FileWriter(logFile, true)
            val writer = BufferedWriter(fileWriter)
            writer.append("[$currentTimeStamp]: ")
            writer.append(text)
            writer.newLine()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}