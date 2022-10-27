package com.geeklabs.myscanner.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class PermissionUtils(val activity: Activity) {

    private var permissionsList = arrayListOf<String>()

    /*fun isPermissionGranted(permission: String): Boolean {
        val result = activity.checkCallingOrSelfPermission(permission)
        return result == PackageManager.PERMISSION_GRANTED
    }*/

    fun requestAllMandatoryPermissions() {
        permissionsList = ArrayList<String>()
//        addPermissionIfNotGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
//        addPermissionIfNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        addPermissionIfNotGranted(Manifest.permission.CAMERA)
        askRequiredPermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
//        addPermissionIfNotGranted(Manifest.permission.CALL_PHONE)
//        addPermissionIfNotGranted(Manifest.permission.READ_PHONE_STATE)
        addPermissionIfNotGranted(Manifest.permission.RECORD_AUDIO)
    }

    private fun addPermissionIfNotGranted(permission: String) {
        if (ContextCompat.checkSelfPermission(activity, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionsList.add(permission)
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }
    }

    private fun askRequiredPermissions(requestCode: Int) {
        if (permissionsList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity, permissionsList.toTypedArray(),
                requestCode
            )
        }
    }

    fun checkLocationPermissions() : Boolean {
        permissionsList = ArrayList<String>()
        addPermissionIfNotGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        addPermissionIfNotGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        askRequiredPermissions(REQUEST_CODE_LOCATION_PERMISSIONS)
        return permissionsList.isEmpty()
    }

    companion object {
        const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1
        const val REQUEST_CODE_LOCATION_PERMISSIONS = 2
    }
}