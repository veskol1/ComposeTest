package com.example.composehealthytest.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.composehealthytest.repository.HealthyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class DownloadDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: HealthyRepository
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.composehealthytest.workmanager.DownloadDataWorker"
    }

    override suspend fun doWork(): Result {
        try {
            Log.d("haha","WorkManager: Work request for sync is run")
            repository.refreshData()
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}