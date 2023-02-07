package com.example.fitnessapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "steps_table")
data class Steps(
    val timeStamp: Long,
    val steps: Long,
    val date: String,
    val week:String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)