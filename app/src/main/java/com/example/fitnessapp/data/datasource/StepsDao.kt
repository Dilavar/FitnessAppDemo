package com.example.fitnessapp.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnessapp.domain.model.Steps
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Query("Select * from STEPS_TABLE where date=:mDate")
    fun getDaySteps(mDate: String): List<Steps>

    @Query("Select * from STEPS_TABLE WHERE date BETWEEN :startDate AND :endDate")
    fun getWeekSteps(startDate: String, endDate: String): Flow<List<Steps>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(steps: Steps)

}