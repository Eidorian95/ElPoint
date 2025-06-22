package com.elpoint.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.PlaceDetails
import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.domain.usecases.GetAddressFromCoordinatesUseCase
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
    private val getAddressFromCoordinatesUseCase: GetAddressFromCoordinatesUseCase,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase
) : ViewModel() {

    sealed class NavigationEvent {
        data class ToDetailScreen(val details: PlaceDetails) : NavigationEvent()
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions.asStateFlow()

    private val _selectedPlaceDetails = MutableStateFlow<PlaceDetails?>(null)
    val selectedPlaceDetails: StateFlow<PlaceDetails?> = _selectedPlaceDetails.asStateFlow()

    private val _searchMode = MutableStateFlow(SearchMode.LIST_RESULTS)
    val searchMode: StateFlow<SearchMode> = _searchMode.asStateFlow()

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
        if (_searchMode.value == SearchMode.MAP_FOCUS) {
            _searchMode.value = SearchMode.LIST_RESULTS
        }
    }

    fun onSuggestionClicked(suggestion: PlaceSuggestion) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getPlaceDetailsUseCase(suggestion.placeId)
            result.onSuccess { details ->
                _selectedPlaceDetails.value = details
                _searchQuery.value = details.name
                _suggestions.value = emptyList()
                _navigationEvent.send(
                    NavigationEvent.ToDetailScreen(details)
                )
            }.onFailure {
                _error.value = "Error al obtener detalles: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun onSearchBarFocused() {
        _searchMode.value = SearchMode.LIST_RESULTS
    }

    fun onMapTapped(lat: Double, lng: Double) {
        viewModelScope.launch {
            if (searchMode.value == SearchMode.LIST_RESULTS) {
                _searchMode.value = SearchMode.MAP_FOCUS
                _searchQuery.value = ""
                _suggestions.value = emptyList()
                _selectedPlaceDetails.value = null
            } else {
                _isLoading.value = true
                getAddressFromCoordinatesUseCase(lat, lng).onSuccess { address ->
                    _selectedPlaceDetails.value = PlaceDetails(address, lat, lng)
                }.onFailure {
                    _selectedPlaceDetails.value = PlaceDetails("Ubicación sin nombre", lat, lng)
                }
                _isLoading.value = false
            }
        }
    }

    fun onViewDetailsClicked(selectedPlaceDetails: PlaceDetails?) {
        viewModelScope.launch {
            selectedPlaceDetails?.let {
                _navigationEvent.send(
                    NavigationEvent.ToDetailScreen(it)
                )
            }
        }
    }
}