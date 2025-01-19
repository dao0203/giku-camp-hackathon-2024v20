package com.dao0203.gikucampv20.android.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.gikucampv20.android.R
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.util.MainPreviews
import com.dao0203.gikucampv20.domain.Training
import com.dao0203.gikucampv20.domain.dummies
import com.dao0203.gikucampv20.feature.record.HistoryWithCalenderUiState
import com.dao0203.gikucampv20.feature.record.HistoryWithCalenderViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryWithCalenderScreen(
    viewModel: HistoryWithCalenderViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryWithCalenderContent(
        uiState = uiState,
        onDayClick = viewModel::changeSelectedDate,
    )
}

private const val CALENDER_KEY = "CALENDER_KEY"

@Composable
private fun HistoryWithCalenderContent(
    uiState: HistoryWithCalenderUiState,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            item(
                key = CALENDER_KEY,
            ) {
                HistoryHorizontalCalender(
                    showBackGroundDays = uiState.showBackGroundDays,
                    showBackGroundSelectedDay = uiState.selectedDate,
                    now = uiState.now,
                    onDayClick = onDayClick,
                )
            }
            item {
                Text(
                    text = stringResource(R.string.history),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                )
            }
            uiState.historiesByMuscleGroup.forEach { (muscleGroup, histories) ->
                item {
                    Text(
                        text = muscleGroup.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                items(
                    histories.size,
                    key = { index -> histories[index].id },
                ) { index ->
                    val history = histories[index]
                    Text(
                        text = history.workoutSet.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryHorizontalCalender(
    showBackGroundDays: Set<LocalDate>,
    showBackGroundSelectedDay: LocalDate?,
    now: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentMonth = now.toJavaLocalDate().yearMonth
    val startMonth = currentMonth.minusMonths(100)
    val endMonth = currentMonth.plusMonths(100)
    val firstDayOfWeek = firstDayOfWeekFromLocale()
    val state =
        rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeek,
        )

    HorizontalCalendar(
        state = state,
        monthHeader = {
            val month = it.yearMonth.month.value
            val year = it.yearMonth.year
            Text(
                text = stringResource(R.string.date, month, year),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        dayContent = {
            Day(
                it,
                showBackGround =
                    showBackGroundDays
                        .any { date ->
                            it.date.toKotlinLocalDate() == date
                        },
                showBackGroundSelected =
                    showBackGroundSelectedDay == it.date.toKotlinLocalDate(),
                onDayClick = onDayClick,
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun Day(
    day: CalendarDay,
    showBackGround: Boolean,
    showBackGroundSelected: Boolean,
    modifier: Modifier = Modifier,
    onDayClick: (LocalDate) -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(50.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color =
                        if (showBackGroundSelected || showBackGround) {
                            if (showBackGroundSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            }
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                )
                .clickable {
                    onDayClick(day.date.toKotlinLocalDate())
                },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color =
                        if (showBackGroundSelected || showBackGround) {
                            if (showBackGround) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                ),
        )
    }
}

@Composable
private fun HistoryItem(
    history: Training.History,
    modifier: Modifier = Modifier,
) {
    Text(
        text = history.workoutSet.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
    )
}

@MainPreviews
@Composable
private fun CalenderPreview() {
    MainTheme {
        HistoryHorizontalCalender(
            showBackGroundDays = setOf(LocalDate(2025, 1, 1)),
            now = LocalDate(2025, 1, 1),
            showBackGroundSelectedDay = LocalDate(2025, 1, 2),
            onDayClick = {},
        )
    }
}

@MainPreviews
@Composable
private fun HistoryWithCalenderContentPreview() {
    MainTheme {
        HistoryWithCalenderContent(
            uiState =
                HistoryWithCalenderUiState.default().copy(
                    historiesByMuscleGroup = Training.History.dummies(),
                ),
            onDayClick = {},
        )
    }
}
