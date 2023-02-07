package com.example.fitnessapp.domain.use_cases

import android.util.Log
import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class GetMonthsWeekStepsUseCase(val repository: StepsRepository) {

    operator fun invoke(): Flow<Resource<List<Steps>>> = flow {
        emit(Resource.Loading())

        var cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val endcal = Calendar.getInstance()
        endcal.set(Calendar.DAY_OF_WEEK, endcal.get(Calendar.DAY_OF_WEEK) - 27)

        val steps = repository.getTotalWeekSteps(endcal.timeInMillis, cal.timeInMillis)
        Log.e("GetMonthsWeek:: ", "STEPS :: $steps")
        if (steps.isEmpty()) {
            emit(Resource.Error("404"))
        } else {
            emit(Resource.Success(steps))
        }

    }
}