package com.example.fitnessapp.domain.repository

import com.example.fitnessapp.domain.model.Steps

interface StepsRepository {

    fun getSteps(startTime: Long, endTime: Long): List<Steps>

    suspend fun getWeekSteps(startDate: Long, endDate: Long): List<Steps>

    suspend fun getTotalWeekSteps(startDate: Long, endDate: Long): List<Steps>

    suspend fun insertSteps(steps: List<Steps>)

    suspend fun insertStep(steps: Steps)
}