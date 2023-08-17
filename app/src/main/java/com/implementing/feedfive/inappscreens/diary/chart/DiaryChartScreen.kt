package com.implementing.feedfive.inappscreens.diary.chart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.implementing.feedfive.R
import com.implementing.feedfive.getString
import com.implementing.feedfive.inappscreens.diary.DiaryEvent
import com.implementing.feedfive.inappscreens.diary.viewmodel.DiaryViewModel

@Composable
fun DiaryChartScreen(
    viewModel: DiaryViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = viewModel.uiState
        var monthly by remember { mutableStateOf(true) }
        MonthlyOrYearlyTab {
            viewModel.onEvent(DiaryEvent.ChangeChartEntriesRange(it))
            monthly = it
        }
        MoodCircularBar(entries = state.chartEntries)
        MoodFlowChart(entries = state.chartEntries, monthly)
    }
}

@Composable
fun MonthlyOrYearlyTab(
    onChange: (Boolean) -> Unit
) {
    var selected by remember {
        mutableStateOf(getString(R.string.last_30_days))
    }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        AnimatedTabIndicator(Modifier.tabIndicatorOffset(tabPositions[if (selected == stringResource(R.string.last_30_days)) 0 else 1]))
    }
    LaunchedEffect(true){
        onChange(true)
    }
    TabRow(
        selectedTabIndex = if (selected == stringResource(R.string.last_30_days)) 0 else 1,
        indicator = indicator,
        modifier = Modifier.clip(RoundedCornerShape(14.dp))
    ) {
        Tab(
            text = { Text(stringResource(R.string.last_30_days)) },
            selected = selected == stringResource(R.string.last_30_days),
            onClick = {
                selected = getString(R.string.last_30_days)
                onChange(true)
            },
        )
        Tab(
            text = { Text(stringResource(R.string.last_year)) },
            selected = selected == stringResource(R.string.last_year),
            onClick = {
                selected = getString(R.string.last_year)
                onChange(false)
            }
        )
    }
}


@Composable
fun AnimatedTabIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
    )
}