package com.firstproject.favdish.utils

import android.content.Context
import android.content.ContextWrapper
import java.io.File
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun getInternalStorage(context: Context, directory: String): File {
//    save to: /data/data/com.firstproject.favdish/app_FavDishImages

    val wrapper = ContextWrapper(context)
    return wrapper.getDir(directory, Context.MODE_PRIVATE)
}

fun saveImage(context: Context, bitmap: Bitmap, directory: String = IMAGE_DIRECTORY) : String {
    val imageFile = File(
        getInternalStorage(context, directory),
        SimpleDateFormat(IMAGE_FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis()) + ".jpg")

    try {
        val stream: OutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        stream.flush()
        stream.close()

//        Log.e(TAG, "Image save to: ${imageFile.absolutePath}")
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return imageFile.absolutePath
}

fun saveImage(context: Context, uri: Uri, directory: String = IMAGE_DIRECTORY) : String {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    return saveImage(context, bitmap, directory)
}