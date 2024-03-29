package com.example.fitnessapp.domain.use_cases

import android.util.Log
import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDayStepsUseCase(val repository: StepsRepository) {

    operator fun invoke(startTime: Long, endTime: Long): Flow<Resource<List<Steps>>> = flow {
        emit(Resource.Loading())
        val steps = repository.getSteps(startTime, endTime)
        Log.e("GetDayStepsUseCase :: ", "STEPS :: $steps")
        if(steps.isEmpty()){
            emit(Resource.Error("404"))
        }else{
            emit(Resource.Success(steps))
        }

    }
}