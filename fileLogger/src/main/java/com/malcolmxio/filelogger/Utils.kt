package com.malcolmxio.filelogger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @return current timestamp.
 * @param format - timestamp format, for example: "dd.MM.yyyy  HH:mm:ss".
 */
fun getCurrentTimestamp(format: String, locale: Locale): String {
    val now = Date()
    val df = SimpleDateFormat(format, locale)
    return df.format(now)
}

/**
 * Tries to open file with logs with third-party application. If file is empty or does not exist - nothing will be happened.
 */
fun openLogsFile(context: Context, logsDirectoryName: String, logsFileName: String) {
    val absPath = ContextCompat.getExternalFilesDirs(context, null)[0]
    val fileDir = "$absPath" + logsDirectoryName
    val file = File(fileDir, logsFileName)
    val authority = getAuthority(context)

    val uri = FileProvider.getUriForFile(context, authority, file)
    if (file.exists() && file.length() > 0) {
        val mime = context.contentResolver.getType(uri)

        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    } else {
        Log.w("File:", "$logsFileName is empty")
    }
}

/**
 * @return an authority of [LogsFileProvider] contentProvider.
 */
private fun getAuthority(context: Context): String {
    val componentName = ComponentName(context, LogsFileProvider::class.java.name)
    return try {
        val providerInfo = context.packageManager.getProviderInfo(componentName, 0)
        providerInfo.authority
    } catch (e: PackageManager.NameNotFoundException) {
        Log.w("Get authority:", "Failed to get authority for component: $componentName")
        ""
    }
}