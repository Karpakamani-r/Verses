package com.w2c.dailyversescompose.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w2c.dailyversescompose.model.TodayVerse

@Dao
interface DailyVerseDAO {

    @Insert
    fun insert(dailyVerse: TodayVerse): Long

    @Query("SELECT * FROM verse WHERE date = (:date)")
    fun getVerseByDate(date: String): TodayVerse?
}