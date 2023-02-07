package com.example.fitnessapp.data.repository

import com.example.fitnessapp.data.datasource.StepsDao
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow

class StepRepositoryImpl(val dao: StepsDao) : StepsRepository {
    override fun getSteps(startTime: Long, endtime: Long): List<Steps> {
        return dao.getDaySteps(startTime = startTime, endtime)
    }

    override suspend fun getWeekSteps(startDate: Long, endDate: Long): List<Steps> {
        return dao.getWeekSteps()
    }

    override suspend fun getTotalWeekSteps(startDate: Long, endDate: Long): List<Steps> {
        return dao.getMonthWeeks()
    }

    override suspend fun insertSteps(steps: List<Steps>) {
        dao.insertSteps(steps)
    }

    override suspend fun insertStep(steps: Steps) {
        dao.insertStep(steps)
    }
}