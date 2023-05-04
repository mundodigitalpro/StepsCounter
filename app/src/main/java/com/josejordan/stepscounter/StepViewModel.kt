package com.josejordan.stepscounter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StepRepository
    val stepData: LiveData<StepEntity?>

    init {
        val stepDao = StepCounterDatabase.getDatabase(application).stepDao()
        repository = StepRepository(stepDao)
        stepData = repository.getTodaySteps()
    }

    fun insert(step: StepEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(step)
    }

    fun update(step: StepEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(step)
    }
}
