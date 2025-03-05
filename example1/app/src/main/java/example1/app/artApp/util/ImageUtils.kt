package example1.app.artApp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun setImageFromByteArray(imageByteArray: ByteArray?, imageView: ImageView) {
        imageByteArray?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun getImageBitmap(imageUri: Uri, context: Context): Bitmap? {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize : Int) : Bitmap {

        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun getImageByteArray(imageUri: Uri, context: Context, maximumSize: Int): ByteArray? {
        val bitmap = getImageBitmap(imageUri, context) ?: return null
        val smallerBitmap = makeSmallerBitmap(bitmap, maximumSize)

        val byteArrayOutputStream = ByteArrayOutputStream()
        smallerBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

}