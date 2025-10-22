package com.militaryworkout.app.utils

import com.militaryworkout.app.models.Exercise
import com.militaryworkout.app.models.ExerciseDatabase
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WorkoutGenerator {
    
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    /**
     * Genera una rutina de entrenamiento basada en el nivel y progresión
     */
    fun generateDailyWorkout(
        level: String,
        dayNumber: Int = 1,
        previousDifficulty: Int = 1
    ): Pair<Workout, List<WorkoutSet>> {
        val today = LocalDate.now().format(dateFormatter)
        
        val workout = Workout(
            date = today,
            level = level,
            completed = false
        )

        val sets = mutableListOf<WorkoutSet>()
        var exerciseId = 1

        // Estructura de entrenamiento militar progresivo
        when (level) {
            "Intermedio" -> {
                // Día 1: Flexiones + Abdominales
                sets.addAll(generateFlexionesWorkout(exerciseId, dayNumber, 1, 60))
                exerciseId += 5
                sets.addAll(generateAbdominalesWorkout(exerciseId, dayNumber, 1, 45))
                exerciseId += 5
            }
            "Avanzado" -> {
                // Día 1: Flexiones + Dominadas + Abdominales
                sets.addAll(generateFlexionesWorkout(exerciseId, dayNumber, 2, 50))
                exerciseId += 5
                sets.addAll(generateDominadasWorkout(exerciseId, dayNumber, 2, 50))
                exerciseId += 5
                sets.addAll(generateAbdominalesWorkout(exerciseId, dayNumber, 2, 40))
                exerciseId += 5
            }
            "Maestro" -> {
                // Día 1: Flexiones + Dominadas + Fondos + Burpees + Abdominales
                sets.addAll(generateFlexionesWorkout(exerciseId, dayNumber, 3, 40))
                exerciseId += 5
                sets.addAll(generateDominadasWorkout(exerciseId, dayNumber, 3, 40))
                exerciseId += 5
                sets.addAll(generateFondosWorkout(exerciseId, dayNumber, 3, 40))
                exerciseId += 5
                sets.addAll(generateBurpeesWorkout(exerciseId, dayNumber, 3, 35))
                exerciseId += 5
                sets.addAll(generateAbdominalesWorkout(exerciseId, dayNumber, 3, 30))
                exerciseId += 5
            }
        }

        // Asignar IDs de entrenamiento después de crear el workout
        return Pair(workout, sets)
    }

    private fun generateFlexionesWorkout(
        startId: Int,
        dayNumber: Int,
        difficulty: Int,
        restTime: Int
    ): List<WorkoutSet> {
        val variant = ExerciseDatabase.pushUpVariants.getOrNull(difficulty - 1)?.name 
            ?: "Flexiones Estándar"
        
        val reps = when {
            dayNumber % 7 == 0 -> 20 + difficulty * 5
            dayNumber % 3 == 0 -> 18 + difficulty * 4
            else -> 15 + difficulty * 3
        }

        val sets = when {
            dayNumber % 7 == 0 -> 5
            dayNumber % 3 == 0 -> 4
            else -> 3
        }

        return listOf(
            WorkoutSet(
                id = startId,
                workoutId = 0,
                exerciseId = startId,
                exerciseName = "Flexiones",
                variant = variant,
                plannedReps = reps,
                plannedSets = sets,
                restTime = restTime,
                difficulty = difficulty
            )
        )
    }

    private fun generateDominadasWorkout(
        startId: Int,
        dayNumber: Int,
        difficulty: Int,
        restTime: Int
    ): List<WorkoutSet> {
        val variant = ExerciseDatabase.pullUpVariants.getOrNull(difficulty - 1)?.name 
            ?: "Dominadas Estándar"
        
        val reps = when {
            dayNumber % 7 == 0 -> 12 + difficulty * 2
            dayNumber % 3 == 0 -> 10 + difficulty * 2
            else -> 8 + difficulty
        }

        val sets = when {
            dayNumber % 7 == 0 -> 5
            dayNumber % 3 == 0 -> 4
            else -> 3
        }

        return listOf(
            WorkoutSet(
                id = startId,
                workoutId = 0,
                exerciseId = startId,
                exerciseName = "Dominadas",
                variant = variant,
                plannedReps = reps,
                plannedSets = sets,
                restTime = restTime,
                difficulty = difficulty
            )
        )
    }

    private fun generateFondosWorkout(
        startId: Int,
        dayNumber: Int,
        difficulty: Int,
        restTime: Int
    ): List<WorkoutSet> {
        val variant = ExerciseDatabase.dipVariants.getOrNull(difficulty - 1)?.name 
            ?: "Fondos Estándar"
        
        val reps = when {
            dayNumber % 7 == 0 -> 15 + difficulty * 3
            dayNumber % 3 == 0 -> 12 + difficulty * 2
            else -> 10 + difficulty
        }

        val sets = when {
            dayNumber % 7 == 0 -> 4
            dayNumber % 3 == 0 -> 3
            else -> 3
        }

        return listOf(
            WorkoutSet(
                id = startId,
                workoutId = 0,
                exerciseId = startId,
                exerciseName = "Fondos",
                variant = variant,
                plannedReps = reps,
                plannedSets = sets,
                restTime = restTime,
                difficulty = difficulty
            )
        )
    }

    private fun generateBurpeesWorkout(
        startId: Int,
        dayNumber: Int,
        difficulty: Int,
        restTime: Int
    ): List<WorkoutSet> {
        val variant = ExerciseDatabase.burpeeVariants.getOrNull(difficulty - 1)?.name 
            ?: "Burpees Estándar"
        
        val reps = when {
            dayNumber % 7 == 0 -> 20 + difficulty * 3
            dayNumber % 3 == 0 -> 18 + difficulty * 2
            else -> 15 + difficulty
        }

        val sets = when {
            dayNumber % 7 == 0 -> 4
            dayNumber % 3 == 0 -> 3
            else -> 3
        }

        return listOf(
            WorkoutSet(
                id = startId,
                workoutId = 0,
                exerciseId = startId,
                exerciseName = "Burpees",
                variant = variant,
                plannedReps = reps,
                plannedSets = sets,
                restTime = restTime,
                difficulty = difficulty
            )
        )
    }

    private fun generateAbdominalesWorkout(
        startId: Int,
        dayNumber: Int,
        difficulty: Int,
        restTime: Int
    ): List<WorkoutSet> {
        val variant = ExerciseDatabase.abdominalVariants.getOrNull(difficulty - 1)?.name 
            ?: "Abdominales Estándar"
        
        val reps = when {
            dayNumber % 7 == 0 -> 25 + difficulty * 5
            dayNumber % 3 == 0 -> 22 + difficulty * 4
            else -> 20 + difficulty * 3
        }

        val sets = when {
            dayNumber % 7 == 0 -> 4
            dayNumber % 3 == 0 -> 3
            else -> 3
        }

        return listOf(
            WorkoutSet(
                id = startId,
                workoutId = 0,
                exerciseId = startId,
                exerciseName = "Abdominales",
                variant = variant,
                plannedReps = reps,
                plannedSets = sets,
                restTime = restTime,
                difficulty = difficulty
            )
        )
    }

    /**
     * Calcula la progresión basada en el historial
     */
    fun calculateNextDifficulty(
        currentDifficulty: Int,
        completionRate: Double,
        daysInLevel: Int
    ): Int {
        return when {
            completionRate >= 0.9 && daysInLevel >= 14 -> minOf(5, currentDifficulty + 1)
            completionRate >= 0.7 && daysInLevel >= 21 -> minOf(5, currentDifficulty + 1)
            else -> currentDifficulty
        }
    }

    /**
     * Reduce tiempos de descanso progresivamente
     */
    fun calculateRestTime(
        baseRestTime: Int,
        difficulty: Int,
        dayNumber: Int
    ): Int {
        val reduction = (dayNumber / 7) * 5 // Reduce 5 segundos cada semana
        return maxOf(20, baseRestTime - reduction)
    }
}

