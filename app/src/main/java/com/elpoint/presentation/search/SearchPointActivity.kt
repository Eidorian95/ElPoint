package com.elpoint.presentation.search

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.elpoint.domain.model.PlaceDetails
import com.elpoint.presentation.detail.DetailActivity
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class SearchPointActivity : ComponentActivity() {
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.observeSearchPlaces()
        observeNavigationEvent()
        observeUiEvent()

        enableEdgeToEdge()
        setContent {
            ElPointTheme {
                SearchScreen()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.onLocationPermissionResult(isGranted)
    }

    private fun observeNavigationEvent(){
        lifecycleScope.launch {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    is SearchViewModel.NavigationEvent.ToDetailScreen -> {
                        goToDetailScreen(event.details)
                    }
                }
            }
        }
    }

    private fun goToDetailScreen(details: PlaceDetails) {
        val intent = Intent(this@SearchPointActivity, DetailActivity::class.java).apply {
            putExtra("PLACE_NAME", details.name)
            putExtra("PLACE_LAT", details.latitude)
            putExtra("PLACE_LNG", details.longitude)
        }
        Log.d("PLACE_DETAILS", details.toString())
        startActivity(intent)
    }

    private fun observeUiEvent(){
        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.RequestLocationPermission -> {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }
}


