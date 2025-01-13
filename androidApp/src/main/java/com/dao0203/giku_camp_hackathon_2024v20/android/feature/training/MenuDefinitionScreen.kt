package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.giku_camp_hackathon_2024v20.android.R
import com.dao0203.giku_camp_hackathon_2024v20.domain.TrainingType
import com.dao0203.giku_camp_hackathon_2024v20.feature.training.DefinitionMenuUiState
import com.dao0203.giku_camp_hackathon_2024v20.feature.training.MenuDefinitionViewModel
import com.dao0203.giku_camp_hackathon_2024v20.feature.training.MuscleGroupsUiModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DefinitionMenuScreen(
    onStartTraining: () -> Unit,
) {
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (permissionState.status.isGranted) {
            // Permission is already granted
        } else {
            permissionState.launchPermissionRequest()
        }
    }
    val viewModel = koinViewModel<MenuDefinitionViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    DefinitionMenuScreenContent(
        onStartTraining = {
            viewModel.startTraining()
            onStartTraining()
        },
        onSelectTrainingType = {

            // define the default training menu
            viewModel.changeTrainingType(it)
            viewModel.changeSets("3")
            viewModel.changeReps("10")
            viewModel.changeWeight("20.0")
            viewModel.changeRest("120")

            // scroll to the top of the list
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        },
        onSetsChange = viewModel::changeSets,
        onRepsChange = viewModel::changeReps,
        onWeightChange = viewModel::changeWeight,
        onRestChange = viewModel::changeRest,
        listState = listState,
        uiState = uiState.value,
    )
}

@Composable
private fun DefinitionMenuScreenContent(
    onStartTraining: () -> Unit,
    onSelectTrainingType: (TrainingType) -> Unit,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
    listState: LazyListState,
    uiState: DefinitionMenuUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (uiState.showStartingTrainingButton) {
                ExtendedFloatingActionButton(
                    onClick = onStartTraining,
                    icon = { Icon(Icons.Filled.DateRange, null) },
                    text = { Text(stringResource(R.string.start)) },
                )
            } else {
                ExtendedFloatingActionButton(
                    onClick = { },
                    containerColor = Color(0xFFd3d3d3),
                    icon = { Icon(Icons.Filled.DateRange, null) },
                    text = { Text(stringResource(R.string.start)) },
                )
            }
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            writingTrainingMenu(
                sets = uiState.sets,
                reps = uiState.reps,
                weight = uiState.weight,
                rest = uiState.rest,
                trainingType = uiState.selectedTrainingType,
                onSetsChange = onSetsChange,
                onRepsChange = onRepsChange,
                onWeightChange = onWeightChange,
                onRestChange = onRestChange,
            )
            trainingTypesByGroup(
                muscleGroupsUiModel = uiState.muscleGroupsUiModel,
                onSelectTrainingType = onSelectTrainingType
            )
        }
    }
}

// OutlinedTextField's border thickness can't be changed from parameter
// so we need to create a custom TextField with BasicTextField
private fun LazyListScope.writingTrainingMenu(
    sets: String,
    reps: String,
    weight: String,
    rest: String,
    trainingType: TrainingType?,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
) {
    item {
        Row {
            OutlinedTextField(
                value = sets,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onSetsChange(it)
                    }
                },
                label = { Text(stringResource(R.string.sets)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = reps,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onRepsChange(it)
                    }
                },
                label = { Text(stringResource(R.string.reps)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text(stringResource(R.string.weight)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f),
            )
        }
    }
    item {
        Spacer(modifier = Modifier.height(4.dp))
    }
    item {
        Row {
            OutlinedTextField(
                value = trainingType?.name ?: stringResource(R.string.select_training_type),
                onValueChange = {},
                label = { Text(stringResource(R.string.training_type)) },
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier
                    .weight(2f),
            )
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = rest,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        onRestChange(it)
                    }
                },
                label = { Text(stringResource(R.string.rest)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }

    }
}

private fun LazyListScope.trainingTypesByGroup(
    muscleGroupsUiModel: MuscleGroupsUiModel,
    onSelectTrainingType: (TrainingType) -> Unit,
) {
    muscleGroupsUiModel.trainingTypesByGroup.forEach { (muscleGroup, trainingTypes) ->
        item {
            Text(
                text = muscleGroup.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(trainingTypes) { trainingType ->
            TrainingTypeItem(
                trainingType,
                onSelectTrainingType = onSelectTrainingType,
            )
        }
    }
}

@Composable
private fun TrainingTypeItem(
    trainingType: TrainingType,
    onSelectTrainingType: (TrainingType) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onSelectTrainingType(trainingType) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = trainingType.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = trainingType.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
