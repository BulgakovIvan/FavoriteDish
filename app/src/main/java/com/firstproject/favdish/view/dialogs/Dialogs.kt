package com.firstproject.favdish.view.dialogs

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

fun showRationalDialogForPermissions(context: Context) {
    AlertDialog
        .Builder(context)
        .setMessage("It Looks like you have turned off permissions required for this feature." +
                " It can be enabled under Application Settings")
        .setPositiveButton("GO TO SETTINGS"){ _, _ ->
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                startActivity(context, intent, null)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
        .setNegativeButton("CANCEL"){dialog, _ ->
            dialog.dismiss()
        }
        .show()
}