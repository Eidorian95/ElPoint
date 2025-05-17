package com.elpoint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.model.Point
import com.elpoint.domain.usecases.GetUserPointsUseCase
import com.elpoint.presentation.state.HomeState
import com.elpoint.presentation.state.PointUiModel
import com.elpoint.presentation.state.UserPointsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getUserPoints: GetUserPointsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    fun fetchPoints() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getUserPoints()
                _state.value = HomeState.Success(result.toUiModel())
            } catch (e: Exception) { //TODO: not catch all exceptions
                _state.value = HomeState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

private fun List<Point>.toUiModel(): UserPointsUiModel {
    return UserPointsUiModel(
        points = this.map {
            PointUiModel(
                id = it.id,
                name = it.name,
                latitude = it.latitude,
                longitude = it.longitude,
                imageUrl = it.imageUrl,
                alarm = it.alarm
            )
        }
    )
}
