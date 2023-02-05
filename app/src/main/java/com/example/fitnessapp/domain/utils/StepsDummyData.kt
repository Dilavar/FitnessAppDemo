package com.example.fitnessapp.domain.utils

import android.util.Log
import com.example.fitnessapp.domain.model.Steps
import java.text.SimpleDateFormat
import java.util.*

object StepsDummyData {
    fun createDummyStepData(): List<Steps> {
        val steps = ArrayList<Steps>()
        var cal: Calendar = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        var week = 48 * 7
        for (i in 1..week) {
            cal.add(Calendar.MINUTE, -30)
            val stepcount = Random().nextInt(1000).toLong()
            Log.e("STEPS", "Count is ::" + stepcount)
            var step = Steps(cal.timeInMillis, stepcount, SimpleDateFormat("dd/MM/yyyy").format(cal.time))
            steps.add(step)
        }

        return steps.reversed()
    }
}