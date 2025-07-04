package com.elpoint.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import com.elpoint.presentation.detail.DetailActivity
import com.elpoint.presentation.search.SearchPointActivity
import com.elpoint.presentation.state.HomeState
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.fetchPoints()
        setContent {
            ElPointTheme {
                    val homeState = viewModel.state.collectAsState()
                    when (val state = homeState.value) {
                        is HomeState.Loading -> {}
                        is HomeState.Success -> {
                            HomeScreen(
                                uiModel = state.points,
                                onPointClick = { goToDetailScreen() },
                                onBackClick = { },
                                onSearchBarClick = { goToSearchScreen() },
                                onSettingsClick = {},
                            )
                        }
                        is HomeState.Error -> {}
                    }
            }
        }
    }

    private fun goToDetailScreen(){
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }

    private fun goToSearchScreen(){
        val intent = Intent(this, SearchPointActivity::class.java)
        startActivity(intent)
    }
}