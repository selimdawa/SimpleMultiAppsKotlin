package com.flatcode.simplemultiapps.LiveTV.Service

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ChannelDataService(var ctx: Context) {
    interface OnDataResponse {
        fun onResponse(response: JSONObject)
        fun onError(error: String?)
    }

    fun getChannelData(url: String?, onDataResponse: OnDataResponse) {
        val queue = Volley.newRequestQueue(ctx)
        val objectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response: JSONObject -> onDataResponse.onResponse(response) }) { error: VolleyError ->
            onDataResponse.onError(error.localizedMessage)
        }
        queue.add(objectRequest)
    }
}