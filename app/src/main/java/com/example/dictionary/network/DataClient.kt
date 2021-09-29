package com.example.dictionary.network

import android.util.Log
import com.example.dictionary.ui.DataStatus
import com.example.dictionary.domain.LanguageResponse
import com.google.gson.Gson
import okhttp3.*

object DataClient {

    private val okHttpClient = OkHttpClient()
    private val gson = Gson()
    private const val url = "https://translate.argosopentech.com/translate?"

    fun initRequest(text:String,source: String,target:String) : DataStatus<LanguageResponse>{

        val request = Request.Builder().url("${url}q=$text&source=$source&target=$target").post(FormBody.Builder().build()).build()
        val response = okHttpClient.newCall(request).execute()
        Log.i("testGG",source)
        return if (response.isSuccessful) {
            val res = gson.fromJson(response.body?.string(), LanguageResponse::class.java)
            DataStatus.Success(res)
        } else {
            DataStatus.Error(response.message)
        }
    }
}