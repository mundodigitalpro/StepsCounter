package com.josejordan.stepscounter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepEntity::class], version = 1, exportSchema = false)
abstract class StepCounterDatabase : RoomDatabase() {
    abstract fun stepDao(): StepDao

    companion object {
        @Volatile
        private var INSTANCE: StepCounterDatabase? = null

        fun getDatabase(context: Context): StepCounterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StepCounterDatabase::class.java,
                    "step_counter_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
