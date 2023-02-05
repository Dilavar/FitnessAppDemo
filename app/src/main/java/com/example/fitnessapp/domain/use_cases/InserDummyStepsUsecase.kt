package com.example.fitnessapp.domain.use_cases

import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import com.example.fitnessapp.domain.utils.StepsDummyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InserDummyStepsUsecase(val repository: StepsRepository) {

    operator fun invoke(noteList: List<Steps>): Flow<Resource<Boolean?>> = flow {
        emit(Resource.Loading())
        try {
            val insert = repository.insertSteps(StepsDummyData.createDummyStepData())
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Something went wrong"))
        }
    }
}