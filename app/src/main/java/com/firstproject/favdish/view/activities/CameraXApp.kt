package com.firstproject.favdish.view.activities

import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.firstproject.favdish.databinding.ActivityCameraXappBinding
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.firstproject.favdish.utils.*
import com.firstproject.favdish.view.fragments.AddUpdateFragment


class CameraXApp : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var binding: ActivityCameraXappBinding
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCamera()

        binding.cameraCaptureButton.setOnClickListener {
            binding.cameraMotionLayout.transitionToEnd()

            Handler(Looper.getMainLooper())
                .postDelayed({ takePhoto() }, TAKE_PHOTO_DELAY)
        }

        outputDirectory = getInternalStorage(this, IMAGE_DIRECTORY)
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                IMAGE_FILENAME_FORMAT, Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, msg)

                    val data = Intent()
                    data.putExtra(AddUpdateFragment.IMAGE_URI, savedUri.toString())
                    setResult(RESULT_OK, data)
                    finish()
                }
            })
    }

//    private fun getOutputDirectory(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.let {
//            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
//        return if (mediaDir != null && mediaDir.exists())
//            mediaDir else filesDir
//    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}