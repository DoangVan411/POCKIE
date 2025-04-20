package com.example.pockie.presentation.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment

object PermissionManager {

    //Yêu cầu cấp quyền
    fun requestPermission(
        context: Context,
        permission: String,
        launcher: ActivityResultLauncher<String>
    ) {
        val permissionStatus = ContextCompat.checkSelfPermission(context, permission)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) { // Đã được cấp
            Log.d("Permission", "requestPermission: Permisson ${permission} is granted")
        } else {
            Log.d("Permission", "requestPermission: Permisson ${permission} isn't granted")
            launcher.launch(permission)
        }
    }

    //Xử lí kết quả cấp quyền
    fun handlePermissionResult(
        context: Context,
        permission: String,
        isGranted: Boolean,
    ): Boolean{
        if (isGranted) { // Quyền đã được cấp
            Log.d("Permission", "handlePermissionResult: Permisson ${permission} is granted")
            return true
        }
        else { // Người dùng đã từ chối vĩnh viễn, điều hướng Cài đặt
            Log.d("Permission", "handlePermissionResult: Permisson ${permission} isn't granted")
            val titlePermission = when(permission){
                Manifest.permission.POST_NOTIFICATIONS -> "Notification permission"
                else -> "Camera permission"
            }
            showGoToSettingsDialog(context, titlePermission)
            return false

        }
    }

    // Điều hướng người dùng đến cài đặt
    fun showGoToSettingsDialog(context: Context, titlePermission: String){
        AlertDialog.Builder(context)
            .setTitle("${titlePermission} has been blocked.")
            .setMessage("Please grant permission in app settings to continue using.")
            .setPositiveButton("Open settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            .show()
    }
}