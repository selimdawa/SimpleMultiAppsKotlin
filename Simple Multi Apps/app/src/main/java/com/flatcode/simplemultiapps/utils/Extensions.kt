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
import com.flatcode.simplemultiapps.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

fun Context.intent1(cls: Class<*>, init: Intent.() -> Unit = {}) {
    val intent = Intent(this, cls)
    intent.init()
    startActivity(intent)
}

fun ImageView.loadImage(url: String?) {
    load(url) {
        placeholder(R.color.image_profile)
        error(R.color.image_profile)
    }
}

fun Long.formatDuration(): String {
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}

fun Context.canWriteToDownloadFolder(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        true
    } else ContextCompat.checkSelfPermission(
        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

fun InputStream.readBytesToEnd(): ByteArray {
    val output = ByteArrayOutputStream()
    val buffer = ByteArray(8 * 1024)
    var bytesRead: Int
    while (this.read(buffer).also { bytesRead = it } != -1) {
        output.write(buffer, 0, bytesRead)
    }
    return output.toByteArray()
}

fun File.writeBytesToFile(fileName: String?, fileContent: ByteArray?) {
    val file = File(this, fileName!!)
    FileOutputStream(file).use { stream -> stream.write(fileContent) }
}

fun createPlainTextShareIntent(chooserTitle: String?, text: String?): Intent {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    return Intent.createChooser(intent, chooserTitle)
}

fun createFileShareIntent(chooserTitle: String?, fileName: String?, fileUri: Uri?): Intent {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "application/pdf"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.clipData = ClipData(fileName, arrayOf("application/pdf"), ClipData.Item(fileUri))
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    return Intent.createChooser(intent, chooserTitle)
}
