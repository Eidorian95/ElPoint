package com.elpoint.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.PlaceSuggestion
import com.elpoint.domain.usecases.SearchPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlacesUseCase: SearchPlacesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
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
        // TODO: Implementar esta lógica más adelante
    }
}