package com.example.fitnessapp.domain.model

import androidx.room.Entity
import androidx.room.Ignore
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

internal object Compare {
    fun min(a: Steps, b: Steps): Steps {
        return if (a.steps < b.steps) a else b
    }

    fun max(a: Steps, b: Steps): Steps {
        return if (a.steps > b.steps) a else b
    }
}