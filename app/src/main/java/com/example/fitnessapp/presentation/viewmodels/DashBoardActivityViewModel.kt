package com.example.fitnessapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.use_cases.GetDayStepsUseCase
import com.example.fitnessapp.domain.utils.StepsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardActivityViewModel @Inject constructor(val stepsUseCase: GetDayStepsUseCase) :
    ViewModel() {


    private val _state = MutableStateFlow(StepsState())
    var state = _state

    fun getSteps(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            stepsUseCase(day = date).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = StepsState(steps = result.data)
                    }
                    is Resource.Loading -> {
                        _state.value = StepsState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _state.value = StepsState(error = "Something went wrong")
                    }

                }
            }
        }
    }

    fun insertDummySteps() {
        CoroutineScope(Dispatchers.IO).launch {
            stepsUseCase(day = date).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = StepsState(steps = result.data)
                    }
                    is Resource.Loading -> {
                        _state.value = StepsState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _state.value = StepsState(error = "Something went wrong")
                    }

                }
            }
        }
    }

}


