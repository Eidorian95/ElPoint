package com.elpoint.presentation.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import com.elpoint.presentation.state.ForecastState.Loading
import com.elpoint.presentation.state.ForecastState.Success
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchForecast()
        enableEdgeToEdge()
        setContent {
            ElPointTheme {
                    val state = viewModel.state.collectAsState()
                    when (val value = state.value) {
                        Loading -> {
                            Text(text = "Loading")
                        }

                        is Success -> {
                            DetailInformationScreen(value.forecast)
                        }

                        else -> {
                            Text(text = "Error")
                        }
                    }
                }
            }
    }
}