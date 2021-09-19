package com.firstproject.favdish.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ActivityAddUpdateDishBinding
import com.firstproject.favdish.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddUpdateDishBinding

    val startCameraXApp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val uri = intent?.getStringExtra(IMAGE_URI)
            Log.e(TAG, "Receive image: $uri")

            uri.let {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.ivDishImage)

                binding.ivAddDishImage.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
            }
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.e(TAG, "getContent: $it")
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.ivDishImage)

            binding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        binding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddUpdateDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddUpdateDish.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE, // work until API 28
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            startCameraXApp.launch(
                                Intent(this@AddUpdateDishActivity, CameraXApp::class.java))
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        getContent.launch("image/*")
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@AddUpdateDishActivity,
                            "You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        showRationalDialogForPermissions()
                    }
                })
                .onSameThread()
                .check()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog
            .Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature." +
                    " It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS"){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", packageName, null)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL"){dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    companion object {
        private const val TAG = "ups"
        const val IMAGE_URI = "IMAGE_URI"
    }
}

