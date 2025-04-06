package com.nomnomapp.nomnom.ui.utilities


import android.graphics.Bitmap

fun resizeBitmap(bitmap: Bitmap, maxSize: Int = 2048): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val scaleFactor = maxOf(width, height).toFloat() / maxSize
    return if (scaleFactor > 1) {
        Bitmap.createScaledBitmap(
            bitmap,
            (width / scaleFactor).toInt(),
            (height / scaleFactor).toInt(),
            true
        )
    } else {
        bitmap
    }
}