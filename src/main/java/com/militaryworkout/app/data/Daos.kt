package com.militaryworkout.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.militaryworkout.app.models.Exercise
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise?

    @Query("SELECT * FROM exercises WHERE category = :category")
    fun getExercisesByCategory(category: String): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE difficulty = :difficulty")
    fun getExercisesByDifficulty(difficulty: Int): Flow<List<Exercise>>
}

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: Workout): Long

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutById(id: Int): Workout?

    @Query("SELECT * FROM workouts WHERE date = :date")
    suspend fun getWorkoutByDate(date: String): Workout?

    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE completed = 1 ORDER BY date DESC")
    fun getCompletedWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWorkoutsInRange(startDate: String, endDate: String): Flow<List<Workout>>

    @Query("SELECT COUNT(*) FROM workouts WHERE completed = 1")
    suspend fun getCompletedWorkoutCount(): Int

    @Query("SELECT COUNT(*) FROM workouts")
    suspend fun getTotalWorkoutCount(): Int

    @Query("SELECT * FROM workouts ORDER BY date DESC LIMIT 1")
    suspend fun getLastWorkout(): Workout?
}

@Dao
interface WorkoutSetDao {
    @Insert
    suspend fun insert(workoutSet: WorkoutSet): Long

    @Update
    suspend fun update(workoutSet: WorkoutSet)

    @Delete
    suspend fun delete(workoutSet: WorkoutSet)

    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun getSetsByWorkoutId(workoutId: Int): List<WorkoutSet>

    @Query("SELECT * FROM workout_sets WHERE id = :id")
    suspend fun getSetById(id: Int): WorkoutSet?

    @Query("SELECT * FROM workout_sets")
    fun getAllSets(): Flow<List<WorkoutSet>>

    @Query("DELETE FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun deleteSetsByWorkoutId(workoutId: Int)

    @Query("SELECT AVG(difficulty) FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun getAverageDifficultyForWorkout(workoutId: Int): Double?

    @Query("SELECT COUNT(*) FROM workout_sets WHERE workoutId = :workoutId AND completedReps > 0")
    suspend fun getCompletedExercisesCount(workoutId: Int): Int
}

