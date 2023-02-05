package com.example.fitnessapp.domain.utils

import com.example.fitnessapp.domain.model.Steps

data class StepsState(
    val isLoading: Boolean = false,
    val steps: List<Steps>? = emptyList(),
    val error: String = ""
)
