package com.elpoint.presentation.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.ui.theme.ElPointTheme
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchMode by viewModel.searchMode.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val selectedPlaceDetails by viewModel.selectedPlaceDetails.collectAsState()
    val focusManager = LocalFocusManager.current

    val mapWeight by animateFloatAsState(
        targetValue = if (searchMode == SearchMode.MAP_FOCUS) 0.8f else 0.2f, label = "mapWeight"
    )
    val resultsWeight by animateFloatAsState(
        targetValue = if (searchMode == SearchMode.MAP_FOCUS) 0.2f else 0.9f,
        label = "resultsWeight"
    )

    ScreenContent(
        searchQuery = searchQuery,
        mapWeight = mapWeight,
        selectedPlaceDetails = selectedPlaceDetails,
        focusManager = focusManager,
        resultsWeight = resultsWeight,
        searchMode = searchMode,
        suggestions = suggestions,
        onQueryChanged = { viewModel.onQueryChanged(it) },
        onSuggestionClicked = { viewModel.onSuggestionClicked(it) },
        onSearchBarFocused =  viewModel::onSearchBarFocused,
        onViewDetailsClicked = { viewModel.onViewDetailsClicked(it) },
        onMapTapped = { lat, lng -> viewModel.onMapTapped(lat, lng) }
    )
}

@Composable
private fun ScreenContent(
    searchQuery: String,
    mapWeight: Float,
    selectedPlaceDetails: PlaceDetails?,
    focusManager: FocusManager,
    resultsWeight: Float,
    searchMode: SearchMode,
    suggestions: List<PlaceSuggestion>,
    onQueryChanged: (String) -> Unit,
    onSuggestionClicked: (PlaceSuggestion) -> Unit,
    onSearchBarFocused: () -> Unit,
    onViewDetailsClicked: (PlaceDetails?) -> Unit,
    onMapTapped: (Double, Double) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.White)
    ) {
        SearchBox(
            query = searchQuery,
            onQueryChanged = { onQueryChanged(it) },
            onFocusChanged = { isFocused ->
                if (isFocused) onSearchBarFocused()
            })

        MapContainer(
            modifier = Modifier.weight(mapWeight),
            selectedPlaceDetails = selectedPlaceDetails,
            onMapTapped = { latLng ->
                focusManager.clearFocus()
                onMapTapped(latLng.latitude, latLng.longitude)
            }
        )

        ResultsContainer(
            modifier = Modifier.weight(resultsWeight),
            searchMode = searchMode,
            suggestions = suggestions,
            selectedPlaceDetails = selectedPlaceDetails,
            onSuggestionClicked = { onSuggestionClicked(it) },
            onViewDetailsClicked = { onViewDetailsClicked(selectedPlaceDetails) }
        )
    }
}

@Composable
private fun SearchBox(
    query: String,
    onQueryChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Buscar spots o lugares...") },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onFocusChanged { focusState -> onFocusChanged(focusState.isFocused) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Composable
fun ResultsContainer(
    searchMode: SearchMode,
    suggestions: List<PlaceSuggestion>,
    selectedPlaceDetails: PlaceDetails?,
    onSuggestionClicked: (PlaceSuggestion) -> Unit,
    onViewDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = searchMode,
        modifier = modifier.fillMaxWidth(),
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        }, label = "resultsAnimation"
    ) { mode ->
        when (mode) {
            SearchMode.LIST_RESULTS -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = suggestions, key = { it.placeId }) { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            onClick = { onSuggestionClicked(suggestion) })
                    }
                }
            }

            SearchMode.MAP_FOCUS -> {
                selectedPlaceDetails?.let { place ->
                    SelectedPlaceCard(
                        placeDetails = place,
                        onViewDetailsClicked = onViewDetailsClicked
                    )
                } ?: Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "Toca el mapa para seleccionar un punto",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@Composable
fun MapContainer(
    selectedPlaceDetails: PlaceDetails?,
    onMapTapped: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(selectedPlaceDetails) {
        selectedPlaceDetails?.let {
            cameraPositionState.animate(
                update = newLatLngZoom(
                    LatLng(
                        it.latitude,
                        it.longitude
                    ), 15f
                ),
                durationMs = 1000
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium)
            .drawBehind {
                drawRoundRect(color = Color.LightGray)
            }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = onMapTapped
        ) {
            selectedPlaceDetails?.let {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            it.latitude,
                            it.longitude
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun SelectedPlaceCard(
    placeDetails: PlaceDetails,
    onViewDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(enabled = true, onClick = {
                onViewDetailsClicked()
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "Lugar",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = placeDetails.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lat: ${
                    String.format(
                        "%.4f",
                        placeDetails.latitude
                    )
                }, Lon: ${String.format("%.4f", placeDetails.longitude)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SuggestionItem(
    suggestion: PlaceSuggestion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Place,
            contentDescription = "Lugar",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = suggestion.primaryText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if (suggestion.secondaryText.isNotEmpty()) {
                Text(
                    text = suggestion.secondaryText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElPointTheme {
        val searchMode = SearchMode.MAP_FOCUS
        ScreenContent(
            searchQuery = "quis",
            mapWeight = if(searchMode == SearchMode.LIST_RESULTS) 0.2f else 0.8f,
            selectedPlaceDetails = null,
            focusManager = LocalFocusManager.current,
            resultsWeight = if(searchMode == SearchMode.LIST_RESULTS) 0.8f else 0.2f,
            searchMode = searchMode,
            suggestions = listOf(
                PlaceSuggestion(
                    placeId = "pri",
                    primaryText = "pericula",
                    secondaryText = "pulvinar"
                ),
                PlaceSuggestion(
                    placeId = "pri2",
                    primaryText = "pericula",
                    secondaryText = "pulvinar"
                ),
                PlaceSuggestion(
                    placeId = "pri3",
                    primaryText = "pericula",
                    secondaryText = "pulvinar"
                ),
                PlaceSuggestion(
                    placeId = "pri4",
                    primaryText = "pericula",
                    secondaryText = "pulvinar"
                ),
            ),
            onQueryChanged = {},
            onSuggestionClicked = {},
            onSearchBarFocused = {},
            onViewDetailsClicked = {},
            onMapTapped = { d: Double, d1: Double -> },
        )
    }
}