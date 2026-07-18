package com.flatcode.simplemultiapps.newsapp

import android.content.Context
import android.widget.Toast
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.newsapp.model.NewsApiResponse
import com.flatcode.simplemultiapps.utils.DATA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RequestManager(private val context: Context) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getNewsHeadlines(listener: OnFetchDataListener<NewsApiResponse>, category: String?, query: String?) {
        val callNewsApi: CallNewsApi = retrofit.create(CallNewsApi::class.java)
        val call: Call<NewsApiResponse> = callNewsApi.callHeadlines("us", category, query, DATA.NEWS_API)

        call.enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(
                call: Call<NewsApiResponse>,
                response: Response<NewsApiResponse>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
                    listener.onError("Request Failed or Empty Body")
                    return
                }
                listener.onFetchData(responseBody.articles, response.message())
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                listener.onError(context.getString(R.string.request_failed))
            }
        })
    }

    interface CallNewsApi {
        @GET("top-headlines")
        fun callHeadlines(
            @Query("country") country: String?,
            @Query("category") category: String?,
            @Query("q") query: String?,
            @Query("apiKey") apiKey: String?,
        ): Call<NewsApiResponse>
    }
}