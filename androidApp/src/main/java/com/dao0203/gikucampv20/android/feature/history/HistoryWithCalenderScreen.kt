package com.dao0203.gikucampv20.android.feature.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryWithCalenderScreen() {
    HistoryWithCalenderContent()
}

@Composable
private fun HistoryWithCalenderContent(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Text("History with Calendar")
        }
    }
}
