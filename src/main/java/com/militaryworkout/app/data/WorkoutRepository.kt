package com.militaryworkout.app.data

import com.militaryworkout.app.models.Exercise
import com.militaryworkout.app.models.ProgressStats
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutRepository(
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val workoutSetDao: WorkoutSetDao
) {
    // Exercise operations
    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insert(exercise)
    suspend fun updateExercise(exercise: Exercise) = exerciseDao.update(exercise)
    suspend fun deleteExercise(exercise: Exercise) = exerciseDao.delete(exercise)
    suspend fun getExerciseById(id: Int) = exerciseDao.getExerciseById(id)
    fun getExercisesByCategory(category: String) = exerciseDao.getExercisesByCategory(category)
    fun getAllExercises() = exerciseDao.getAllExercises()
    fun getExercisesByDifficulty(difficulty: Int) = exerciseDao.getExercisesByDifficulty(difficulty)

    // Workout operations
    suspend fun insertWorkout(workout: Workout) = workoutDao.insert(workout)
    suspend fun updateWorkout(workout: Workout) = workoutDao.update(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.delete(workout)
    suspend fun getWorkoutById(id: Int) = workoutDao.getWorkoutById(id)
    suspend fun getWorkoutByDate(date: String) = workoutDao.getWorkoutByDate(date)
    fun getAllWorkouts() = workoutDao.getAllWorkouts()
    fun getCompletedWorkouts() = workoutDao.getCompletedWorkouts()
    fun getWorkoutsInRange(startDate: String, endDate: String) = 
        workoutDao.getWorkoutsInRange(startDate, endDate)
    suspend fun getLastWorkout() = workoutDao.getLastWorkout()

    // Workout Set operations
    suspend fun insertWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.insert(workoutSet)
    suspend fun updateWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.update(workoutSet)
    suspend fun deleteWorkoutSet(workoutSet: WorkoutSet) = workoutSetDao.delete(workoutSet)
    suspend fun getSetsByWorkoutId(workoutId: Int) = workoutSetDao.getSetsByWorkoutId(workoutId)
    suspend fun getSetById(id: Int) = workoutSetDao.getSetById(id)
    suspend fun deleteSetsByWorkoutId(workoutId: Int) = workoutSetDao.deleteSetsByWorkoutId(workoutId)

    // Statistics and Progress
    suspend fun getProgressStats(): ProgressStats {
        val totalWorkouts = workoutDao.getTotalWorkoutCount()
        val completedWorkouts = workoutDao.getCompletedWorkoutCount()
        val lastWorkout = workoutDao.getLastWorkout()
        
        val progressionPercentage = if (totalWorkouts > 0) {
            (completedWorkouts.toDouble() / totalWorkouts) * 100
        } else {
            0.0
        }

        return ProgressStats(
            totalWorkouts = totalWorkouts,
            completedWorkouts = completedWorkouts,
            totalExercises = 0, // Se calculará desde los sets
            averageDifficulty = 0.0,
            progressionPercentage = progressionPercentage,
            lastWorkoutDate = lastWorkout?.date ?: "N/A",
            currentLevel = lastWorkout?.level ?: "Intermedio"
        )
    }

    suspend fun getWorkoutProgress(workoutId: Int): Pair<Int, Int> {
        val sets = workoutSetDao.getSetsByWorkoutId(workoutId)
        val completedSets = sets.count { it.completedReps > 0 }
        return Pair(completedSets, sets.size)
    }

    fun getTodayWorkout(): Flow<Workout?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return workoutDao.getAllWorkouts()
    }
}

