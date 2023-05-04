package com.josejordan.stepscounter

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val steps: Int,
    val distance: Double
)
