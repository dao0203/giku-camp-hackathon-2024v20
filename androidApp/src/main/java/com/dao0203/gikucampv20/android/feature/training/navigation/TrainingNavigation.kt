package com.dao0203.gikucampv20.android.feature.training.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dao0203.gikucampv20.android.feature.training.DefinitionMenuScreen
import com.dao0203.gikucampv20.android.feature.training.TrainingRestScreen
import com.dao0203.gikucampv20.android.feature.training.TrainingResultScreen
import com.dao0203.gikucampv20.android.feature.training.TrainingWithCameraScreen
import kotlinx.serialization.Serializable

private sealed interface TrainingRoute {
    @Serializable
    data object Base : TrainingRoute

    @Serializable
    data object MenuDefinition : TrainingRoute

    sealed interface TrainingAction : TrainingRoute {
        @Serializable
        data object TrainingWithCamera : TrainingAction

        @Serializable
        data object Rest : TrainingAction
    }

    @Serializable
    data object Result : TrainingRoute
}

fun NavController.navigateToTraining() {
    navigate(TrainingRoute.Base)
}

fun NavController.navigateToMenuDefinition() {
    navigate(TrainingRoute.MenuDefinition)
}

fun NavController.navigateToTrainingWithCamera() {
    navigate(TrainingRoute.TrainingAction.TrainingWithCamera)
}

fun NavController.navigateToRest() {
    navigate(TrainingRoute.TrainingAction.Rest)
}

fun NavController.navigateToResult() {
    navigate(TrainingRoute.Result)
}

fun NavGraphBuilder.trainingNavigation(navController: NavController) {
    navigation<TrainingRoute.Base>(
        startDestination = TrainingRoute.MenuDefinition,
    ) {
        composable<TrainingRoute.MenuDefinition> {
            DefinitionMenuScreen {
                navController.navigateToTrainingWithCamera()
            }
        }
        composable<TrainingRoute.TrainingAction.TrainingWithCamera> {
            TrainingWithCameraScreen(
                navigateToRest = { navController.navigateToRest() },
                navigateToResult = { navController.navigateToResult() },
            )
        }
        composable<TrainingRoute.TrainingAction.Rest> {
            TrainingRestScreen(
                navigateToTrainingWithCamera = { navController.navigateToTrainingWithCamera() },
            )
        }
        composable<TrainingRoute.Result> {
            TrainingResultScreen(
                navigateToMenuDefinition = { navController.navigateToMenuDefinition() },
            )
        }
    }
}
