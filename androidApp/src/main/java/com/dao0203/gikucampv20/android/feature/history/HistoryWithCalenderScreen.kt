package com.dao0203.gikucampv20.android.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.gikucampv20.android.R
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.util.MainPreviews
import com.dao0203.gikucampv20.feature.record.HistoryWithCalenderUiState
import com.dao0203.gikucampv20.feature.record.HistoryWithCalenderViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.koin.compose.viewmodel.koinViewModel
import java.time.YearMonth

@Composable
fun HistoryWithCalenderScreen(
    viewModel: HistoryWithCalenderViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryWithCalenderContent(
        uiState = uiState,
    )
}

@Composable
private fun HistoryWithCalenderContent(
    uiState: HistoryWithCalenderUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            HistoryHorizontalCalender(
                showBackGroundDays = uiState.showBackGroundDays,
            )
        }
    }
}

@Composable
fun HistoryHorizontalCalender(
    showBackGroundDays: Set<LocalDate>,
    modifier: Modifier = Modifier,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

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
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun Day(
    day: CalendarDay,
    showBackGround: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(50.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color =
                    if (showBackGround) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.Unspecified
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@MainPreviews
@Composable
private fun CalenderPreview() {
    MainTheme {
        HistoryHorizontalCalender(
            showBackGroundDays = setOf(LocalDate(2025, 1, 1)),
        )
    }
}
