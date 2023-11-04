package com.w2c.dailyversescompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Verse(val verse: String, val text: String) : Serializable

data class Chapters(val chapter: String, val verses: List<Verse>) : Serializable

data class VersesResponse(val book: String, val chapters: List<Chapters>) : Serializable

data class WallpaperResponse(val version: String, val images: List<String>) : Serializable

@Entity(tableName = "verse")
data class TodayVerse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var date: String = "",
    val verse: String,
    val verseNo: String,
    val chapterNo: String,
    val bookName: String
)