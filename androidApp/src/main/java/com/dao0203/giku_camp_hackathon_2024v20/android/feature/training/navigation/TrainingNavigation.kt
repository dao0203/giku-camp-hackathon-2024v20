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

    sealed interface TrainingAction : TrainingRoute {
        @Serializable
        data class TrainingWithCamera(val trainingId: String) : TrainingAction

        @Serializable
        data object Rest : TrainingAction
    }

    @Serializable
    data class Result(val trainingId: String) : TrainingRoute
}

fun NavController.navigateToTraining() {
    navigate(TrainingRoute.Base)
}

fun NavController.navigateToMenuDefinition() {
    navigate(TrainingRoute.MenuDefinition)
}

fun NavController.navigateToTrainingWithCamera() {
    navigate(TrainingRoute.TrainingAction.TrainingWithCamera("trainingId"))
}

fun NavController.navigateToRest() {
    navigate(TrainingRoute.TrainingAction.Rest)
}

fun NavController.navigateToResult() {
    navigate(TrainingRoute.Result("trainingId"))
}

fun NavGraphBuilder.trainingNavigation(
//    onBackClick: () -> Unit,
) {
    navigation<TrainingRoute.Base>(
        startDestination = TrainingRoute.MenuDefinition,
    ) {
        composable<TrainingRoute.MenuDefinition> {
            DefinitionMenuScreen { }
        }
        composable<TrainingRoute.TrainingAction.TrainingWithCamera> {
            // TODO: Implement
        }
        composable<TrainingRoute.TrainingAction.Rest> {
            // TODO: Implement
        }
        composable<TrainingRoute.Result> {
            // TODO: Implement
        }
    }
}