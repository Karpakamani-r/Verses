package com.w2c.dailyversescompose.retrofit

import com.w2c.dailyversescompose.model.VersesResponse
import com.w2c.dailyversescompose.model.WallpaperResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface Verses {
    @GET("verses/{book}")
    suspend fun getChapterByBook(@Path("book") book: String): VersesResponse

    @GET("verses/images.json")
    suspend fun getWallpapers(): WallpaperResponse
}