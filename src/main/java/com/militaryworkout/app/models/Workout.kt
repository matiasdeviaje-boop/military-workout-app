package com.militaryworkout.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String, // Formato: YYYY-MM-DD
    val level: String, // "Intermedio", "Avanzado", "Maestro", etc.
    val totalDuration: Int = 0, // en minutos
    val completed: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "workout_sets")
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutId: Int,
    val exerciseId: Int,
    val exerciseName: String,
    val variant: String,
    val plannedReps: Int,
    val completedReps: Int = 0,
    val plannedSets: Int,
    val completedSets: Int = 0,
    val restTime: Int = 60, // en segundos
    val difficulty: Int,
    val notes: String = ""
)

data class DailyProgress(
    val date: String,
    val level: String,
    val exercises: List<ExerciseProgress>,
    val totalDuration: Int,
    val completed: Boolean
)

data class ExerciseProgress(
    val exerciseName: String,
    val variant: String,
    val plannedReps: Int,
    val completedReps: Int,
    val plannedSets: Int,
    val completedSets: Int,
    val difficulty: Int,
    val restTime: Int
)

data class ProgressStats(
    val totalWorkouts: Int,
    val completedWorkouts: Int,
    val totalExercises: Int,
    val averageDifficulty: Double,
    val progressionPercentage: Double,
    val lastWorkoutDate: String,
    val currentLevel: String
)

