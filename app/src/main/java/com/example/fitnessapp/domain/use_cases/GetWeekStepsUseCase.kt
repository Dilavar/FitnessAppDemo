package com.example.fitnessapp.domain.use_cases

import android.util.Log
import com.example.fitnessapp.common.Resource
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class GetWeekStepsUseCase(val repository: StepsRepository) {

    operator fun invoke(startTime: Long, endTime: Long): Flow<Resource<List<Steps>>> = flow {
        emit(Resource.Loading())

        var cal= Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)

        val endcal= Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK,cal.get(Calendar.DAY_OF_WEEK)-6)

        val steps = repository.getWeekSteps(endcal.timeInMillis,cal.timeInMillis)
        Log.e("GetDayStepsUseCase :: ", "STEPS :: $steps")
        if(steps.isEmpty()){
            emit(Resource.Error("404"))
        }else{
            emit(Resource.Success(steps))
        }

    }
}