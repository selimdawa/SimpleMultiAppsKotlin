package com.flatcode.simplemultiapps.utils

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil.load
import com.flatcode.simplemultiapps.livetv.model.Category
import com.flatcode.simplemultiapps.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.Serializable

object VOID {
    fun IntentClear(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        context.startActivity(intent)
    }

    fun Intent1(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        context.startActivity(intent)
    }

    fun IntentExtra(context: Context, c: Class<*>?, key: String?, value: String?) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        context.startActivity(intent)
    }

    fun IntentSerializable(context: Context, c: Class<*>?, key: String?, value: Serializable?) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        context.startActivity(intent)
    }

    fun IntentExtraChannel(context: Context, c: Class<*>?, key: String?, value: Category?) {
        val intent = Intent(context, c)
        intent.putExtra("categoryName", DATA.EMPTY)
        intent.putExtra(key, value)
        context.startActivity(intent)
    }

    fun loadImage(context: Context?, Url: String?, Image: ImageView) {
        try {
            Image.load(Url) {
                placeholder(R.color.image_profile)
                error(R.color.image_profile)
            }
        } catch (e: Exception) {
            Image.setImageResource(R.color.image_profile)
        }
    }

    fun plainTextShareIntent(chooserTitle: String?, text: String?): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        return Intent.createChooser(intent, chooserTitle)
    }

    fun fileShareIntent(chooserTitle: String?, fileName: String?, fileUri: Uri?): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
        intent.clipData = ClipData(fileName, arrayOf("application/pdf"), ClipData.Item(fileUri))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return Intent.createChooser(intent, chooserTitle)
    }

    fun canWriteToDownloadFolder(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            true
        } else ContextCompat.checkSelfPermission(
            context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun readBytesToEnd(inputStream: InputStream): ByteArray {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
        return output.toByteArray()
    }

    fun writeBytesToFile(directory: File?, fileName: String?, fileContent: ByteArray?) {
        val file = File(directory, fileName!!)
        FileOutputStream(file).use { stream -> stream.write(fileContent) }
    }

    fun convertDuration(duration: Long): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}