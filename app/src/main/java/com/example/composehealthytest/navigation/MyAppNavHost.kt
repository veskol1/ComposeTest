package com.example.composehealthytest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composehealthytest.constants.Constants.navigateToMainScreen
import com.example.composehealthytest.constants.Constants.navigateToTimelineScreen
import com.example.composehealthytest.screens.MainScreen
import com.example.composehealthytest.screens.TimelineScreen
import com.example.composehealthytest.viewmodels.HealthyViewModel

@Composable
fun MyAppNavHost(
    navController: NavHostController = rememberNavController(),
    healthyViewModel: HealthyViewModel
) {
    NavHost(
        navController = navController,
        startDestination = navigateToMainScreen
    ) {
        composable(route = navigateToMainScreen) {
            MainScreen(healthyViewModel = healthyViewModel, onTimelineClicked = {
                navController.navigate(route = navigateToTimelineScreen)
            })
        }

        composable(
            route = navigateToTimelineScreen,
        ) {
            TimelineScreen(healthyViewModel.getTimeLineData(), onBackPressed = {
                navController.popBackStack()
            })
        }
    }
}