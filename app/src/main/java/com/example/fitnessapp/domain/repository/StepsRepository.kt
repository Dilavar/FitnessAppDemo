package com.example.fitnessapp.domain.repository

import com.example.fitnessapp.domain.model.Steps
import kotlinx.coroutines.flow.Flow

interface StepsRepository {

     fun getSteps(date: String): List<Steps>

    suspend fun getWeekSteps(startDate: String, endDate: String): Flow<List<Steps>>

    suspend fun insertSteps(steps: Steps)
}