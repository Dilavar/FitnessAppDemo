package com.example.fitnessapp.domain.use_cases

import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDayStepsUseCase(val repository: StepsRepository) {

    operator fun invoke(day: String): Flow<Resource<List<Steps>>> = flow {
        emit(Resource.Loading())
        val steps = repository.getSteps(day)
        val list= ArrayList<Steps>()
        list.add(Steps(12121212121,123,"2-3-2022",1))
        emit(Resource.Success(steps))
    }
}