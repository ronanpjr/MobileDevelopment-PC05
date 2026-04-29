package com.example.pc05.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pc05.R
import com.example.pc05.data.local.WeatherLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.tab_current),
        stringResource(R.string.tab_history)
    )

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.statusBarsPadding().padding(top = 16.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> CurrentWeatherTab(viewModel)
                1 -> HistoryTab(viewModel)
            }
        }
    }
}

@Composable
fun CurrentWeatherTab(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val latestLog = logs.firstOrNull()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Error -> Text(
                text = stringResource(R.string.error_prefix, (uiState as WeatherUiState.Error).message),
                color = MaterialTheme.colorScheme.error
            )
            else -> {
                latestLog?.let {
                    Text(
                        text = stringResource(R.string.label_temperature, it.temperature),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(R.string.label_windspeed, it.windspeed),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.label_condition, it.weathercode),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } ?: Text(stringResource(R.string.no_data))
            }
        }

        Button(
            onClick = { viewModel.fetchWeather() },
            modifier = Modifier.padding(top = 16.dp),
            enabled = uiState !is WeatherUiState.Loading
        ) {
            Text(stringResource(R.string.fetch_button))
        }
    }
}

@Composable
fun HistoryTab(viewModel: WeatherViewModel) {
    val logs by viewModel.logs.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(logs) { log ->
            WeatherLogItem(log)
            HorizontalDivider()
        }
    }
}

@Composable
fun WeatherLogItem(log: WeatherLog) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }
    val dateString = dateFormat.format(Date(log.fetchedAt))

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = stringResource(R.string.history_item_time, dateString),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(R.string.history_item_temp_wind, log.temperature, log.windspeed),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.history_item_code, log.weathercode),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
