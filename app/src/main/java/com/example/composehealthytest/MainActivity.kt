package com.example.composehealthytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.composehealthytest.navigation.MyAppNavHost
import com.example.composehealthytest.ui.theme.ComposeHealthyTestTheme
import com.example.composehealthytest.viewmodels.HealthyViewModel

class MainActivity : ComponentActivity() {
    private val healthyViewModel: HealthyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
}
