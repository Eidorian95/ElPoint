package com.elpoint.presentation.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elpoint.presentation.state.ForecastState
import com.elpoint.presentation.state.ForecastState.Loading
import com.elpoint.presentation.state.ForecastState.Success
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.fetchForecast()
        setContent {
            ElPointTheme {
                val state = viewModel.state.collectAsState()
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .systemBarsPadding()
                ) {
                    HomeScreen(state)
                }
            }
        }
    }

    @Composable
    private fun HomeScreen(state: State<ForecastState>) {
        when (state.value) {
            Loading -> {
                Text(text = "Loading")
            }

            is Success -> {
                LazyColumn {
                    items((state.value as Success).forecast.timestamps) {
                        Text(text = it.toString())
                    }
                }
            }

            else -> {
                Text(text = "Error")
            }
        }
    }
}