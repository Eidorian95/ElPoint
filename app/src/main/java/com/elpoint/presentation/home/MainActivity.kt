package com.elpoint.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import com.elpoint.presentation.detail.DetailActivity
import com.elpoint.presentation.state.HomeState
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchPoints()
        setContent {
            ElPointTheme {
                Surface {
                    val homeState = viewModel.state.collectAsState()
                    when (val state = homeState.value) {
                        is HomeState.Loading -> {}
                        is HomeState.Success -> {
                            HomeScreen(
                                uiModel = state.points,
                                query = "",
                                onQueryChange = {},
                                onPointClick = { navigateTo() },
                                onBackClick = { }
                            )
                        }
                        is HomeState.Error -> {}

                    }

                }
            }
        }
    }

    private fun navigateTo(){
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}