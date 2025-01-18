package com.dao0203.gikucampv20.feature.record

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent

@Stable
data class HistoryWithCalenderUiState(
    val showBackGroundDays: Set<LocalDate>,
    val selectedDate: LocalDate,
)

class HistoryWithCalenderViewModel : ViewModel(), KoinComponent {
    val _uiState =
        MutableStateFlow(
            HistoryWithCalenderUiState(
                showBackGroundDays = emptySet(),
                selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            ),
        )
    val uiState = _uiState.asStateFlow()

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }


}
