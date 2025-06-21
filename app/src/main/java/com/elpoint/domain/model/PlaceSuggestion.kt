package com.elpoint.domain.model

import androidx.compose.runtime.Immutable
@Immutable
data class PlaceSuggestion(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
)