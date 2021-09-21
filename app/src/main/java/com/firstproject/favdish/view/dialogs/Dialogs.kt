package com.firstproject.favdish.view.dialogs

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.firstproject.favdish.databinding.DialogCustomImageSelectionBinding
import com.firstproject.favdish.view.activities.CameraXApp
import com.firstproject.favdish.view.fragments.AddUpdateFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

fun customImageSelectionDialog(
    fragment: AddUpdateFragment,
    tvCameraClick: ActivityResultLauncher<Intent>,
    tvGalleryClick: ActivityResultLauncher<String>
) {
    val dialog = Dialog(fragment.requireActivity())
    val binding = DialogCustomImageSelectionBinding.inflate(
        LayoutInflater.from(fragment.requireActivity()))
    dialog.setContentView(binding.root)

    binding.tvCamera.setOnClickListener {
        Dexter.withContext(fragment.requireActivity()).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (report.areAllPermissionsGranted()) {
                        tvCameraClick.launch(
                            Intent(fragment.requireActivity(), CameraXApp::class.java)
                        )
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions(fragment.requireActivity())
            }

        }).onSameThread().check()

        dialog.dismiss()
    }

    binding.tvGallery.setOnClickListener {
        Dexter.withContext(fragment.requireActivity())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    tvGalleryClick.launch("image/*")
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        fragment.requireActivity(),
                        "You have denied the storage permission to select image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    showRationalDialogForPermissions(fragment.requireActivity())
                }
            })
            .onSameThread()
            .check()

        dialog.dismiss()
    }

    dialog.show()
}

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