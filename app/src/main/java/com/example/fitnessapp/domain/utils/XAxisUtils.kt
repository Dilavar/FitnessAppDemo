package com.example.fitnessapp.domain.utils

object XAxisUtils {

    fun getXAxisUtils(key: Int): String {
        var map = HashMap<Int, String>()
        map[0] = "12 AM"
        map[7] = "4 AM"
        map[15] = "8 AM"
        map[23] = "12 PM"
        map[31] = "4 PM"
        map[39] = "8 PM"
        map[47] = "12 AM"
        return map.get(key) ?: "NA"
    }

    fun getWeekXAxisUtils(key: Int): String {
        var map = HashMap<Int, String>()
        map[0] = "Sun"
        map[1] = "Mon"
        map[2] = "Tue"
        map[3] = "Wed"
        map[4] = "Thur"
        map[5] = "Fri"
        map[6] = "Sat"
        return map.get(key) ?: "NA"
    }

    fun getMonthWeeksUtils(key: Int): String {
        var map = HashMap<Int, String>()
        map[0] = "Week 1"
        map[1] = "Week 2"
        map[2] = "Week 3"
        map[3] = "Week 4"
        return map.get(key) ?: "NA"
    }
}