package com.w2c.dailyversescompose.work

import android.content.Context
import androidx.work.*
import com.w2c.dailyversescompose.repo.VersesRepository
import com.w2c.dailyversescompose.util.Util
import com.w2c.dailyversescompose.util.Util.getTodayDate
import com.w2c.dailyversescompose.util.showNotification
import java.util.concurrent.TimeUnit

class VerseNotificationWork(val context: Context, params: WorkerParameters) :
    CoroutineWorker(appContext = context, params = params) {

    override suspend fun doWork(): Result {
        return try {
            val repo = VersesRepository(context)
            val todayVerse = repo.getTodayVerse(getTodayDate())
            showNotification(context, todayVerse)
            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun scheduleWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<VerseNotificationWork>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.DAYS
            ).setConstraints(constraints)
                .setInitialDelay(Util.getDifferentMillsToNextDay(), TimeUnit.MILLISECONDS).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "ShowTodayVerse",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
}