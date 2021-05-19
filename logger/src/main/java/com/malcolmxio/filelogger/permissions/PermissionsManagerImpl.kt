package com.malcolmxio.filelogger.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.malcolmxio.filelogger.R

/**
 * Default implementation of [IPermissionsManager].
 */
class PermissionsManagerImpl(private val permissionCode: Int = 228) : IPermissionsManager {

    override fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Log.i("PermissionsManager", "Start writing logs")
                    }
                } else {
                    Log.i(
                        "PermissionsManager",
                        "Cannot write logs to file in the case of denied permission"
                    )
                }
            }
        }
    }

    override fun askForReadWritePermissions(activity: Activity) {
        val writePermissionGranted = (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)

        val readPermissionGranted = (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)

        when {
            writePermissionGranted && readPermissionGranted -> {
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                val dialog = AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.ext_storage_write_permission_title))
                    .setMessage(activity.getString(R.string.ext_storage_write_permission_message))
                    .setPositiveButton(
                        activity.getString(R.string.permission_ok_button)
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            permissionCode
                        )
                    }
                    .create()
                dialog.show()
            }
            else -> {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    permissionCode
                )
            }
        }
    }
}