package com.elpoint.presentation.state


sealed class HomeState {
    data object Loading : HomeState()
    data class Success(val points: UserPointsUiModel) : HomeState()
    data class Error(val message: String) : HomeState()
}