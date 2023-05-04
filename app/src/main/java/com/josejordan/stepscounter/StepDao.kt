package com.josejordan.stepscounter

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepDao {
    @Insert
    suspend fun insert(step: StepEntity)

    @Update
    suspend fun update(step: StepEntity)

    @Query("SELECT * FROM step_table WHERE date = :date")
    fun getStepsByDate(date: String): LiveData<StepEntity?>

    @Query("DELETE FROM step_table")
    suspend fun deleteAll()
}
