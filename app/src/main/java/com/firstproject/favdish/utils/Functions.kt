package com.firstproject.favdish.utils

import android.content.Context
import android.content.ContextWrapper
import java.io.File
import android.widget.Toast

import com.firstproject.favdish.view.activities.MainActivity

import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.firstproject.favdish.view.activities.CameraXApp
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.*


fun getInternalStorage(context: Context, directory: String): File {
//    save to: /data/data/com.firstproject.favdish/app_FavDishImages

    val wrapper = ContextWrapper(context)
    return wrapper.getDir(directory, Context.MODE_PRIVATE)
}

fun saveImage(context: Context, bitmap: Bitmap, directory: String = IMAGE_DIRECTORY) {
    val imageFile = File(
        getInternalStorage(context, directory),
        SimpleDateFormat(IMAGE_FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis()) + ".jpg")

    try {
        val stream: OutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        stream.flush()
        stream.close()

        Log.e(TAG, "Image save to: ${imageFile.absolutePath}")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}