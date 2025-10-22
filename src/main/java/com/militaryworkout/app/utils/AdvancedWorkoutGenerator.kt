package com.militaryworkout.app.utils

import com.militaryworkout.app.models.ExerciseDatabase
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Generador avanzado de rutinas con progresión militar
 */
object AdvancedWorkoutGenerator {

    private val dateFormatter = DateTimeFormatter.ISO_DATE

    /**
     * Genera una rutina completa con progresión adaptativa
     */
    fun generateAdaptiveWorkout(
        level: String,
        dayNumber: Int = 1,
        previousCompletionRate: Double = 0.8
    ): Pair<Workout, List<WorkoutSet>> {
        val today = LocalDate.now().format(dateFormatter)
        
        // Calcular dificultad adaptativa
        val baseDifficulty = when (level) {
            "Intermedio" -> 1
            "Avanzado" -> 2
            "Maestro" -> 3
            "Élite" -> 4
            else -> 1
        }

        val adaptiveDifficulty = ProgressionCalculator.calculateNextDifficulty(
            baseDifficulty,
            (previousCompletionRate * 10).toInt(),
            10,
            dayNumber
        )

        val workout = Workout(
            date = today,
            level = level,
            completed = false
        )

        val sets = mutableListOf<WorkoutSet>()
        var exerciseId = 1

        // Generar rutina basada en nivel y día
        when (level) {
            "Intermedio" -> generateIntermediateRoutine(
                sets,
                exerciseId,
                dayNumber,
                adaptiveDifficulty
            )
            "Avanzado" -> generateAdvancedRoutine(
                sets,
                exerciseId,
                dayNumber,
                adaptiveDifficulty
            )
            "Maestro" -> generateMasterRoutine(
                sets,
                exerciseId,
                dayNumber,
                adaptiveDifficulty
            )
            "Élite" -> generateEliteRoutine(
                sets,
                exerciseId,
                dayNumber,
                adaptiveDifficulty
            )
        }

        return Pair(workout, sets)
    }

    private fun generateIntermediateRoutine(
        sets: MutableList<WorkoutSet>,
        startId: Int,
        dayNumber: Int,
        difficulty: Int
    ) {
        var currentId = startId

        // Flexiones progresivas
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Flexiones",
            variant = ExerciseDatabase.pushUpVariants.getOrNull(difficulty - 1)?.name ?: "Flexiones Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(15, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(3, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(60, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Abdominales progresivos
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Abdominales",
            variant = ExerciseDatabase.abdominalVariants.getOrNull(difficulty - 1)?.name ?: "Abdominales Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(20, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(3, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(45, difficulty, dayNumber),
            difficulty = difficulty
        ))
    }

    private fun generateAdvancedRoutine(
        sets: MutableList<WorkoutSet>,
        startId: Int,
        dayNumber: Int,
        difficulty: Int
    ) {
        var currentId = startId

        // Flexiones
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Flexiones",
            variant = ExerciseDatabase.pushUpVariants.getOrNull(difficulty - 1)?.name ?: "Flexiones Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(15, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(4, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(50, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Dominadas
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Dominadas",
            variant = ExerciseDatabase.pullUpVariants.getOrNull(difficulty - 1)?.name ?: "Dominadas Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(8, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(4, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(50, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Abdominales
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Abdominales",
            variant = ExerciseDatabase.abdominalVariants.getOrNull(difficulty - 1)?.name ?: "Abdominales Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(20, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(3, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(40, difficulty, dayNumber),
            difficulty = difficulty
        ))
    }

    private fun generateMasterRoutine(
        sets: MutableList<WorkoutSet>,
        startId: Int,
        dayNumber: Int,
        difficulty: Int
    ) {
        var currentId = startId

        // Flexiones
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Flexiones",
            variant = ExerciseDatabase.pushUpVariants.getOrNull(difficulty - 1)?.name ?: "Flexiones Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(15, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(40, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Dominadas
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Dominadas",
            variant = ExerciseDatabase.pullUpVariants.getOrNull(difficulty - 1)?.name ?: "Dominadas Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(8, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(4, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(40, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Fondos
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Fondos",
            variant = ExerciseDatabase.dipVariants.getOrNull(difficulty - 1)?.name ?: "Fondos Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(10, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(4, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(40, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Burpees
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Burpees",
            variant = ExerciseDatabase.burpeeVariants.getOrNull(difficulty - 1)?.name ?: "Burpees Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(15, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(3, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(35, difficulty, dayNumber),
            difficulty = difficulty
        ))

        // Abdominales
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Abdominales",
            variant = ExerciseDatabase.abdominalVariants.getOrNull(difficulty - 1)?.name ?: "Abdominales Estándar",
            plannedReps = ProgressionCalculator.calculateProgressiveReps(20, difficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(4, difficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(30, difficulty, dayNumber),
            difficulty = difficulty
        ))
    }

    private fun generateEliteRoutine(
        sets: MutableList<WorkoutSet>,
        startId: Int,
        dayNumber: Int,
        difficulty: Int
    ) {
        var currentId = startId

        // Rutina élite con máxima dificultad
        val maxDifficulty = minOf(5, difficulty + 1)

        // Flexiones Planche
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Flexiones",
            variant = ExerciseDatabase.pushUpVariants.last().name,
            plannedReps = ProgressionCalculator.calculateProgressiveReps(20, maxDifficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, maxDifficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(30, maxDifficulty, dayNumber),
            difficulty = maxDifficulty
        ))

        // Dominadas Planche
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Dominadas",
            variant = ExerciseDatabase.pullUpVariants.last().name,
            plannedReps = ProgressionCalculator.calculateProgressiveReps(12, maxDifficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, maxDifficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(30, maxDifficulty, dayNumber),
            difficulty = maxDifficulty
        ))

        // Fondos Planche
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Fondos",
            variant = ExerciseDatabase.dipVariants.last().name,
            plannedReps = ProgressionCalculator.calculateProgressiveReps(15, maxDifficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, maxDifficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(30, maxDifficulty, dayNumber),
            difficulty = maxDifficulty
        ))

        // Burpees Extremos
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Burpees",
            variant = ExerciseDatabase.burpeeVariants.last().name,
            plannedReps = ProgressionCalculator.calculateProgressiveReps(20, maxDifficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, maxDifficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(25, maxDifficulty, dayNumber),
            difficulty = maxDifficulty
        ))

        // Dragon Flag
        sets.add(createExerciseSet(
            id = currentId++,
            exerciseName = "Abdominales",
            variant = ExerciseDatabase.abdominalVariants.last().name,
            plannedReps = ProgressionCalculator.calculateProgressiveReps(25, maxDifficulty, dayNumber),
            plannedSets = ProgressionCalculator.calculateProgressiveSets(5, maxDifficulty, dayNumber),
            restTime = ProgressionCalculator.calculateProgressiveRestTime(25, maxDifficulty, dayNumber),
            difficulty = maxDifficulty
        ))
    }

    private fun createExerciseSet(
        id: Int,
        exerciseName: String,
        variant: String,
        plannedReps: Int,
        plannedSets: Int,
        restTime: Int,
        difficulty: Int
    ): WorkoutSet {
        return WorkoutSet(
            id = id,
            workoutId = 0,
            exerciseId = id,
            exerciseName = exerciseName,
            variant = variant,
            plannedReps = plannedReps,
            plannedSets = plannedSets,
            restTime = restTime,
            difficulty = difficulty
        )
    }
}

