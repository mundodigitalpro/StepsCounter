package com.josejordan.stepscounter

import androidx.lifecycle.LiveData
import java.text.SimpleDateFormat
import java.util.*

class StepRepository(private val stepDao: StepDao) {
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun getTodaySteps(): LiveData<StepEntity?> {
        return stepDao.getStepsByDate(today)
    }

    suspend fun insert(step: StepEntity) {
        stepDao.insert(step)
    }

    suspend fun update(step: StepEntity) {
        stepDao.update(step)
    }
}
