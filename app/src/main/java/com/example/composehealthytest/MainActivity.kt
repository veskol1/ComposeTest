package com.example.composehealthytest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest.Companion.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.composehealthytest.navigation.MyAppNavHost
import com.example.composehealthytest.repository.HealthyRepository.Companion.HOURS_TO_FETCH
import com.example.composehealthytest.ui.theme.ComposeHealthyTestTheme
import com.example.composehealthytest.viewmodels.HealthyViewModel
import com.example.composehealthytest.workmanager.DownloadDataWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val healthyViewModel: HealthyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            startDownloadDataService()

            ComposeHealthyTestTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavHost(healthyViewModel = healthyViewModel)
                }
            }
        }
    }

    private fun startDownloadDataService() {
        val repeatingRequest = PeriodicWorkRequestBuilder<DownloadDataWorker>(
            HOURS_TO_FETCH,
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DownloadDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}
