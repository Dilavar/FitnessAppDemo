package com.example.fitnessapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.use_cases.GetDayStepsUseCase
import com.example.fitnessapp.domain.use_cases.GetWeekStepsUseCase
import com.example.fitnessapp.domain.use_cases.InserDummyStepsUsecase
import com.example.fitnessapp.domain.utils.DummyDataInsertState
import com.example.fitnessapp.domain.utils.StepsDummyData
import com.example.fitnessapp.domain.utils.StepsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashBoardActivityViewModel @Inject constructor(
    val stepsUseCase: GetDayStepsUseCase,
    val stepsDummyStepsUsecase: InserDummyStepsUsecase,
    val getWeekStepsUseCase: GetWeekStepsUseCase
) :
    ViewModel() {


    private val _state = MutableStateFlow(StepsState())
    var state = _state

    private val _stateInsertDummyData = MutableStateFlow(DummyDataInsertState())
    var stateInsertDummyData = _stateInsertDummyData

    fun getSteps() {
        CoroutineScope(Dispatchers.IO).launch {

            var cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)

            var endCal = Calendar.getInstance()
            endCal.set(Calendar.HOUR_OF_DAY, 23)
            endCal.set(Calendar.MINUTE, 59)
            endCal.set(Calendar.SECOND, 59  )

            stepsUseCase(cal.timeInMillis, endCal.timeInMillis).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = StepsState(steps = result.data)
                    }
                    is Resource.Loading -> {
                        _state.value = StepsState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _state.value = StepsState(error = result.message.toString())
                    }

                }
            }
        }
        Random(1000)
    }

    fun getWeekSteps() {
        CoroutineScope(Dispatchers.IO).launch {


            getWeekStepsUseCase(0, 0).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = StepsState(steps = result.data)
                    }
                    is Resource.Loading -> {
                        _state.value = StepsState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _state.value = StepsState(error = result.message.toString())
                    }

                }
            }
        }
        Random(1000)
    }

    fun insertDummySteps() {
        CoroutineScope(Dispatchers.IO).launch {
            stepsDummyStepsUsecase(noteList = StepsDummyData.createDummyStepData()).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _stateInsertDummyData.value = DummyDataInsertState(isInsertedSuccess = true)
                        getSteps()
                    }
                    is Resource.Loading -> {
                        _stateInsertDummyData.value = DummyDataInsertState(isLoading = true)
                    }
                    is Resource.Error -> {
                        _stateInsertDummyData.value = DummyDataInsertState(error = result.message)
                    }
                }
            }
        }
    }

}


