package com.dao0203.gikucampv20.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dao0203.gikucampv20.android.feature.history.navigation.historyNavigation
import com.dao0203.gikucampv20.android.feature.history.navigation.navigateToRecord
import com.dao0203.gikucampv20.android.feature.training.navigation.TrainingRoute
import com.dao0203.gikucampv20.android.feature.training.navigation.trainingNavigation
import com.dao0203.gikucampv20.android.ui.MainAppState

@Composable
fun MainNavHost(
    mainAppState: MainAppState,
    modifier: Modifier = Modifier,
) {
    val navController = mainAppState.navController
    NavHost(
        navController = navController,
        startDestination = TrainingRoute.Base,
        modifier = modifier,
    ) {
        trainingNavigation(
            navController,
            navigateToHistory = navController::navigateToRecord,
        )
        historyNavigation(navController)
    }
}
