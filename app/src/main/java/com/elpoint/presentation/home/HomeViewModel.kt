package com.elpoint.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.usecases.GetUserPointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getUserPoints:GetUserPointsUseCase,
): ViewModel() {

    fun fetchPoints(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = getUserPoints()
            Log.d("USER_POINTS", result.toString())
        }
    }
}