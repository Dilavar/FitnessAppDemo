package com.example.fitnessapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnessapp.domain.model.Steps

@Database(
    entities = [Steps::class],
    version = 1
)
abstract class FitnessDatabase : RoomDatabase() {
    abstract val stepsDao: StepsDao


    companion object{
        const val FITNESS_DATABASE_NAME="FITNESS_DATABASE_NAME"
    }
}