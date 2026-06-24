package com.flatcode.simplemultiapps.PdfReader

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.flatcode.simplemultiapps.PdfReader.Activity.PdfReaderActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.VOID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.SSLException

class DownloadPDFFile(private val activity: PdfReaderActivity) {

    fun execute(url: String) {
        activity.lifecycleScope.launch {
            val result = doInBackground(url)
            onPostExecute(result)
        }
    }

    private suspend fun doInBackground(url: String): Any = withContext(Dispatchers.IO) {
        var httpConnection: HttpURLConnection? = null
        try {
            httpConnection = URL(url).openConnection() as HttpURLConnection
            httpConnection.connect()
            val responseCode = httpConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                VOID.readBytesToEnd(httpConnection.inputStream)
            } else {
                Log.e("DownloadPDFFile", "Error during http request, response code : $responseCode")
                responseCode
            }
        } catch (e: SSLException) {
            Log.e("DownloadPDFFile", "SSL Error cannot get file at URL : $url", e)
            e
        } catch (e: IOException) {
            Log.e("DownloadPDFFile", "Error cannot get file at URL : $url", e)
            e
        } finally {
            httpConnection?.disconnect()
        }
    }

    private fun onPostExecute(result: Any?) {
        activity.hideProgressBar()
        when (result) {
            is Int -> {
                Toast.makeText(activity, R.string.toast_http_code_error, Toast.LENGTH_LONG).show()
            }

            is SSLException -> {
                Toast.makeText(activity, R.string.toast_ssl_error, Toast.LENGTH_LONG).show()
            }

            is IOException -> {
                Toast.makeText(activity, R.string.toast_generic_download_error, Toast.LENGTH_LONG)
                    .show()
            }

            is ByteArray -> {
                activity.saveToFileAndDisplay(result)
            }

            else -> {
                Toast.makeText(activity, R.string.toast_generic_download_error, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}