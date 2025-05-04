package com.elpoint.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpoint.domain.usecases.GetUserPointsUseCase
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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