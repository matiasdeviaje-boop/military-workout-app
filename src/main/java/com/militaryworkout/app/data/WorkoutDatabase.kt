package com.militaryworkout.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.militaryworkout.app.models.Exercise
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet

@Database(
    entities = [Exercise::class, Workout::class, WorkoutSet::class],
    version = 1,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSetDao(): WorkoutSetDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

