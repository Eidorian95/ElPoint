package com.elpoint.presentation.search

sealed class UiEvent {
    object RequestLocationPermission : UiEvent()
}