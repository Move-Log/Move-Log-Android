package com.ilgusu.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtil {
    private const val MAX_FILE_SIZE_MB = 5
    private const val BYTES_PER_MB = 1920 * 1080
    private const val DEFAULT_COMPRESS_QUALITY = 100

    class ImageSizeExceededException : Exception("Image size exceeds ${MAX_FILE_SIZE_MB}MB limit")

    fun createImageFile(
        context: Context,
        uri: Uri,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = DEFAULT_COMPRESS_QUALITY,
        degrees: Float = 0F,
    ): File? {
        return try {
            val bitmap = getBitmapFromUri(context, uri)
            val byteArray = convertBitmapToByteArray(
                getRotatedBitmap(bitmap, degrees) ?: bitmap,
                format,
                quality
            )

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
        quality: Int,
    ): ByteArray {
        return ByteArrayOutputStream().use { stream ->
            bitmap.compress(format, quality, stream)
            stream.toByteArray()
        }
    }

    fun getOrientationOfImage(context: Context, uri: Uri): Int {
        val inputStream = context.contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        val orientation =
            exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }
}