package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.DefinitionMenuScreen
import kotlinx.serialization.Serializable

sealed interface TrainingRoute {
    @Serializable
    data object Base : TrainingRoute
    @Serializable
    data object MenuDefinition : TrainingRoute
    @Serializable
    data object TrainingWithCamera : TrainingRoute
}

fun NavController.navigateToTraining() {
    navigate(TrainingRoute.Base)
}

fun NavController.navigateToMenuDefinition() {
    navigate(TrainingRoute.MenuDefinition)
}

fun NavController.navigateToTrainingWithCamera() {
    navigate(TrainingRoute.TrainingWithCamera)
}

fun NavGraphBuilder.trainingNavigation(
//    onBackClick: () -> Unit,
) {
    navigation<TrainingRoute.Base>(
        startDestination = TrainingRoute.MenuDefinition,
    ) {
        composable<TrainingRoute.MenuDefinition> {
           DefinitionMenuScreen {  }
        }
        composable<TrainingRoute.TrainingWithCamera> {
            // TrainingWithCameraScreen()
        }
    }
}