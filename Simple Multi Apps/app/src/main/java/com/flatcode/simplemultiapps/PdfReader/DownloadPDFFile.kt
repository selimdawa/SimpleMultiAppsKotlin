package com.flatcode.simplemultiapps.PdfReader

import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.flatcode.simplemultiapps.PdfReader.Activity.PdfReaderActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.VOID
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.SSLException

class DownloadPDFFile(activity: PdfReaderActivity) : AsyncTask<String?, Void?, Any?>() {

    private val mainActivityWR: WeakReference<PdfReaderActivity>

    override fun doInBackground(vararg params: String?): Any? {
        val url = strings[0]
        var httpConnection: HttpURLConnection? = null
        return try {
            httpConnection = URL(url).openConnection() as HttpURLConnection
            httpConnection.connect()
            val responseCode = httpConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                VOID.readBytesToEnd(httpConnection.inputStream)
            } else {
                Log.e("DownloadPDFFile", "Error during http request, response code : $responseCode")
                responseCode
            }
        } catch (e: IOException) {
            Log.e("DownloadPDFFile", "Error cannot get file at URL : $url", e)
            e
        } finally {
            httpConnection?.disconnect()
        }
    }

    override fun onPostExecute(result: Any?) {
        val activity = mainActivityWR.get()
        if (activity != null) {
            activity.hideProgressBar()
            when (result) {
                null -> {
                    Toast.makeText(
                        activity,
                        R.string.toast_generic_download_error,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Int -> {
                    Toast.makeText(activity, R.string.toast_http_code_error, Toast.LENGTH_LONG)
                        .show()
                }

                is SSLException -> {
                    Toast.makeText(activity, R.string.toast_ssl_error, Toast.LENGTH_LONG).show()
                }

                is IOException -> {
                    Toast.makeText(
                        activity, R.string.toast_generic_download_error, Toast.LENGTH_LONG
                    ).show()
                }

                is ByteArray -> {
                    activity.saveToFileAndDisplay(result as ByteArray?)
                }
            }
        }
    }

    init {
        mainActivityWR = WeakReference(activity)
    }
}