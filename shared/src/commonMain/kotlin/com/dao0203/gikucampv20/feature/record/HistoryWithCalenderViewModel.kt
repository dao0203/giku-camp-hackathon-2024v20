package com.dao0203.gikucampv20.feature.record

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.domain.MuscleGroup
import com.dao0203.gikucampv20.domain.Training
import com.dao0203.gikucampv20.domain.byMuscleGroup
import com.dao0203.gikucampv20.repository.TrainingHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
data class HistoryWithCalenderUiState(
    val showBackGroundDays: Set<LocalDate>,
    val selectedDate: LocalDate?,
    val historiesByMuscleGroup: Map<MuscleGroup, List<Training.History>>,
    val now: LocalDate,
) {
    companion object {
        fun default() =
            HistoryWithCalenderUiState(
                showBackGroundDays = emptySet(),
                selectedDate = null,
                historiesByMuscleGroup = emptyMap(),
                now = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            )
    }
}

data class HistoryWithCalenderViewModelState(
    val now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val selectedDate: LocalDate? = null,
)

class HistoryWithCalenderViewModel : ViewModel(), KoinComponent {
    private val trainingHistoryRepository: TrainingHistoryRepository by inject()
    private val vmState = MutableStateFlow(HistoryWithCalenderViewModelState())
    private val historiesByMuscleGroupState =
        vmState
            .map { it.selectedDate }
            .distinctUntilChanged()
            .combine(trainingHistoryRepository.trainingHistory) { selectedDate, histories ->
                if (selectedDate != null) {
                    histories.filter { it.createdAt == selectedDate }.byMuscleGroup()
                } else {
                    emptyMap()
                }
            }
    private val showBackGroundDays =
        trainingHistoryRepository.trainingHistory
            .map { it.map { history -> history.createdAt }.toSet() }
    val uiState =
        combine(
            historiesByMuscleGroupState,
            showBackGroundDays,
            vmState,
        ) { histories, showBackgroundDays, vmState ->
            HistoryWithCalenderUiState(
                showBackGroundDays = showBackgroundDays,
                selectedDate = vmState.selectedDate,
                historiesByMuscleGroup = histories,
                now = vmState.now,
            )
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                HistoryWithCalenderUiState.default(),
            )

    fun changeSelectedDate(date: LocalDate) {
        vmState.update { it.copy(selectedDate = date) }
    }
}
