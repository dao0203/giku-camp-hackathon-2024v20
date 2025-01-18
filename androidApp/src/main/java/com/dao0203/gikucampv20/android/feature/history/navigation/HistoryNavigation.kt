package com.dao0203.gikucampv20.android.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dao0203.gikucampv20.android.feature.history.HistoryWithCalenderScreen
import kotlinx.serialization.Serializable

sealed interface HistoryRoute {
    @Serializable
    data object Base : HistoryRoute

    @Serializable
    data object HistoryWithCalendar : HistoryRoute
}

fun NavController.navigateToRecord() {
    navigate(HistoryRoute.Base)
}

fun NavController.navigateToHistoryWithCalendar() {
    navigate(HistoryRoute.HistoryWithCalendar)
}

fun NavGraphBuilder.historyNavigation(navController: NavController) {
    navigation<HistoryRoute.Base>(
        startDestination = HistoryRoute.HistoryWithCalendar,
    ) {
        composable<HistoryRoute.HistoryWithCalendar> {
            HistoryWithCalenderScreen()
        }
    }
}
