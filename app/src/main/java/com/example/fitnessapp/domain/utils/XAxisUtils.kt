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
}