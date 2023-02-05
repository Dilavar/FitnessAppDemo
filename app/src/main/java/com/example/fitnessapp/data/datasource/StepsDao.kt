package com.example.fitnessapp.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitnessapp.domain.model.Steps

@Dao
interface StepsDao {

    @Query("Select * from STEPS_TABLE where timeStamp BETWEEN :startTime AND :endTime")
    fun getDaySteps(startTime: Long, endTime: Long): List<Steps>

    @Query("Select sum(steps) as steps ,steps_table.date,timeStamp,id  FROM steps_table  WHERE timeStamp GROUP by date")
    fun getWeekSteps(): List<Steps>

    @Insert()
    suspend fun insertSteps(steps: List<Steps>)

    @Insert()
    suspend fun insertStep(steps: Steps)


}