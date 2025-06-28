package com.elpoint.presentation.search

import com.elpoint.domain.model.PlaceDetails

sealed class NavigationEvent {
    data class ToDetailScreen(val details: PlaceDetails) : NavigationEvent()
}