package com.dao0203.gikucampv20.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
): MainAppState {
    return remember(navController) { MainAppState(navController) }
}

@Stable
class MainAppState(
    val navController: NavHostController,
)
