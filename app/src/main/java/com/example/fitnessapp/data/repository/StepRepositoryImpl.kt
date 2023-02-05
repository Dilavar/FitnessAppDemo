package com.example.fitnessapp.data.repository

import com.example.fitnessapp.data.datasource.StepsDao
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow

class StepRepositoryImpl(val dao: StepsDao) : StepsRepository {
    override fun getSteps(date: String): List<Steps> {
        return dao.getDaySteps(date)
    }

    override suspend fun getWeekSteps(startDate: String, endDate: String): Flow<List<Steps>> {
        return dao.getWeekSteps(startDate, endDate)
    }

    override suspend fun insertSteps(steps: Steps) {
        dao.insertSteps(steps)
    }
}