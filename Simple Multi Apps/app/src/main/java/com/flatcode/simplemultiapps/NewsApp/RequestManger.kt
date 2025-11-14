package com.flatcode.simplemultiapps.NewsApp

import android.content.Context
import android.widget.Toast
import com.flatcode.simplemultiapps.NewsApp.Model.NewsApiResponse
import com.flatcode.simplemultiapps.Unit.DATA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RequestManger(var context: Context) {

    var retrofit = Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    fun getNewsHeadlines(listener: OnFetchDataListener<*>, category: String?, query: String?) {
        val callNewsApi = retrofit.create(CallNewsApi::class.java)
        val call = callNewsApi.callHeadlines("us", category, query, DATA.NEWS_API)
        try {
            call.enqueue(object : Callback<NewsApiResponse?> {
                override fun onResponse(
                    call: Call<NewsApiResponse?>, response: Response<NewsApiResponse?>,
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                    }
                    assert(response.body() != null)
                    listener.onFetchData(response.body()!!.articles, response.message())
                }

                override fun onFailure(call: Call<NewsApiResponse?>, t: Throwable) {
                    //listener.onError(t.getMessage());
                    listener.onError("Request Failed!")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface CallNewsApi {
        @GET("top-headlines")
        fun callHeadlines(
            @Query("country") country: String?,
            @Query("category") category: String?,
            @Query("q") query: String?,
            @Query("apiKey") api_key: String?,
        ): Call<NewsApiResponse>
    }
}