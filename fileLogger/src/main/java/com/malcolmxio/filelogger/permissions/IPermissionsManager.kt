package com.malcolmxio.filelogger.permissions

import android.app.Activity
import android.content.Context

/**
 * An interface for helper class to manage permissions requests.
 * Could be implemented by user or use the default implementation: @see [PermissionsManagerImpl].
 */
interface IPermissionsManager {

    /**
     * Use at [Activity.onRequestPermissionsResult] callback.
     */
    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )

    /**
     * Ask for necessary permissions
     */
    fun askForReadWritePermissions(activity: Activity)
}