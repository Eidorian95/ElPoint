package com.elpoint.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.domain.usecases.GetPlaceDetailsUseCase
import com.elpoint.domain.usecases.SearchPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase
) : ViewModel() {

    sealed class NavigationEvent {
        data class ToDetailScreen(val details: PlaceDetails) : NavigationEvent()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions.asStateFlow()

    private val _selectedPlaceLocation = MutableStateFlow<PlaceDetails?>(null)
    val selectedPlaceLocation: StateFlow<PlaceDetails?> = _selectedPlaceLocation.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun observeSearchPlaces() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .onEach { query ->
                    if (query.length > 2) {
                        _isLoading.value = true
                        searchPlacesUseCase(query).collect { result ->
                            result.onSuccess {
                                _suggestions.value = it
                            }.onFailure {
                                _error.value = "Error al buscar lugares: ${it.message}"
                            }
                            _isLoading.value = false
                        }
                    } else {
                        _suggestions.value = emptyList()
                    }
                }
                .launchIn(this)
        }
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onSuggestionClicked(suggestion: PlaceSuggestion) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getPlaceDetailsUseCase(suggestion.placeId)
            result.onSuccess { details ->
                _selectedPlaceLocation.value = details
                _navigationEvent.send(
                    NavigationEvent.ToDetailScreen(details)
                )
            }.onFailure {
                _error.value = "Error al obtener detalles: ${it.message}"
            }
            _isLoading.value = false
        }
    }
}