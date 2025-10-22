package com.militaryworkout.app.data

import com.militaryworkout.app.models.Exercise
import com.militaryworkout.app.models.ExerciseDatabase

/**
 * Inicializa la base de datos con ejercicios predeterminados
 */
object DatabaseInitializer {

    /**
     * Obtiene la lista de ejercicios predeterminados
     */
    fun getPredefinedExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        // Flexiones y variantes
        ExerciseDatabase.pushUpVariants.forEachIndexed { index, variant ->
            exercises.add(
                Exercise(
                    id = exercises.size + 1,
                    name = "Flexiones",
                    category = "Flexiones",
                    variant = variant.name,
                    difficulty = index + 1,
                    description = variant.description
                )
            )
        }

        // Dominadas y variantes
        ExerciseDatabase.pullUpVariants.forEachIndexed { index, variant ->
            exercises.add(
                Exercise(
                    id = exercises.size + 1,
                    name = "Dominadas",
                    category = "Dominadas",
                    variant = variant.name,
                    difficulty = index + 1,
                    description = variant.description
                )
            )
        }

        // Fondos y variantes
        ExerciseDatabase.dipVariants.forEachIndexed { index, variant ->
            exercises.add(
                Exercise(
                    id = exercises.size + 1,
                    name = "Fondos",
                    category = "Fondos",
                    variant = variant.name,
                    difficulty = index + 1,
                    description = variant.description
                )
            )
        }

        // Burpees y variantes
        ExerciseDatabase.burpeeVariants.forEachIndexed { index, variant ->
            exercises.add(
                Exercise(
                    id = exercises.size + 1,
                    name = "Burpees",
                    category = "Burpees",
                    variant = variant.name,
                    difficulty = index + 1,
                    description = variant.description
                )
            )
        }

        // Abdominales y variantes
        ExerciseDatabase.abdominalVariants.forEachIndexed { index, variant ->
            exercises.add(
                Exercise(
                    id = exercises.size + 1,
                    name = "Abdominales",
                    category = "Abdominales",
                    variant = variant.name,
                    difficulty = index + 1,
                    description = variant.description
                )
            )
        }

        return exercises
    }

    /**
     * Inicializa la base de datos con datos predeterminados
     */
    suspend fun initializeDatabase(repository: WorkoutRepository) {
        try {
            val exercises = getPredefinedExercises()
            exercises.forEach { exercise ->
                repository.insertExercise(exercise)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

