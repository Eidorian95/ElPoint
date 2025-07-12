package com.elpoint.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.elpoint.domain.model.PlaceDetails
import com.elpoint.presentation.detail.DetailActivity
import com.elpoint.presentation.search.SearchPointActivity
import com.elpoint.presentation.state.HomeState
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.fetchPoints()
        observeNavigationEvents()

        setContent {
            ElPointTheme {
                val homeState = viewModel.state.collectAsState()
                when (val state = homeState.value) {
                    is HomeState.Loading -> {}
                    is HomeState.Success -> {
                        HomeScreen(
                            uiModel = state.points,
                            onPointClick = { viewModel.onPointClicked(it) },
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

    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    is HomeViewModel.HomeNavigationEvents.ToDetailScreen -> {
                        goToDetailScreen(event.details)
                    }
                }
            }
        }
    }

    private fun goToDetailScreen(details: PlaceDetails) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("PLACE_LAT", details.latitude)
        intent.putExtra("PLACE_LNG", details.longitude)
        intent.putExtra("PLACE_NAME", details.name)
        intent.putExtra("SPOT_ID", details.id)
        startActivity(intent)
    }

    private fun goToSearchScreen() {
        val intent = Intent(this, SearchPointActivity::class.java)
        startActivity(intent)
    }
}
