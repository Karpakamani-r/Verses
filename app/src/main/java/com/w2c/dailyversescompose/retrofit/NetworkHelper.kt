package com.w2c.dailyversescompose.retrofit

import com.w2c.dailyversescompose.util.Util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHelper {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getVerses(): Verses {
        return retrofit.create(Verses::class.java)
    }
}