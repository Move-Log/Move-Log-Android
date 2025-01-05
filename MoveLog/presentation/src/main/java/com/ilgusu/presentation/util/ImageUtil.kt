package com.ilgusu.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    private const val MAX_FILE_SIZE_MB = 5
    private const val BYTES_PER_MB = 1920 * 1080
    private const val DEFAULT_COMPRESS_QUALITY = 100

    class ImageSizeExceededException : Exception("Image size exceeds ${MAX_FILE_SIZE_MB}MB limit")

    fun createImageFile(
        context: Context,
        uri: Uri,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = DEFAULT_COMPRESS_QUALITY
    ): File? {
        return try {
            val bitmap = getBitmapFromUri(context, uri)
            val byteArray = convertBitmapToByteArray(bitmap, format, quality)

            if (byteArray.size > MAX_FILE_SIZE_MB * BYTES_PER_MB) {
                val reducedQuality = quality * MAX_FILE_SIZE_MB * BYTES_PER_MB / byteArray.size
                if (reducedQuality > 0) {
                    return createImageFile(context, uri, format, reducedQuality)
                }
                throw ImageSizeExceededException()
            }

            val extension = when (format) {
                Bitmap.CompressFormat.JPEG -> "jpg"
                Bitmap.CompressFormat.PNG -> "png"
                Bitmap.CompressFormat.WEBP -> "webp"
                else -> "png"
            }

            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.$extension")
            FileOutputStream(file).use { fos ->
                fos.write(byteArray)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
                ?: throw IllegalArgumentException("Invalid image URI")
        } ?: throw IllegalArgumentException("Cannot open input stream for URI")
    }

    private fun convertBitmapToByteArray(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat,
        quality: Int
    ): ByteArray {
        return ByteArrayOutputStream().use { stream ->
            bitmap.compress(format, quality, stream)
            stream.toByteArray()
        }
    }
}