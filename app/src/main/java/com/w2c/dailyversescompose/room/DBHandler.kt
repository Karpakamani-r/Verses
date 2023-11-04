package com.w2c.dailyversescompose.room

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.w2c.dailyversescompose.model.TodayVerse

@Database(entities = [TodayVerse::class], version = 1, exportSchema = false)
abstract class DBHandler : RoomDatabase() {
    abstract fun verseDAO(): DailyVerseDAO

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var INSTANCE: DBHandler

        fun getDatabase(context: Context): DBHandler {
            if (::INSTANCE.isInitialized) {
                return INSTANCE
            }
            synchronized(this) {
                if (::INSTANCE.isInitialized) return INSTANCE
                return Room.databaseBuilder(
                    context,
                    DBHandler::class.java,
                    "VersesDB"
                ).build()
            }
        }
    }

}

