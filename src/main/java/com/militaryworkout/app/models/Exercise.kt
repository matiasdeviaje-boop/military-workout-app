package com.militaryworkout.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String, // "Flexiones", "Dominadas", "Fondos", "Burpees", "Abdominales"
    val variant: String, // Variante específica del ejercicio
    val difficulty: Int, // 1-5 (Intermedio a Maestro)
    val description: String = ""
)

data class ExerciseVariant(
    val name: String,
    val description: String,
    val difficulty: Int
)

// Variantes de ejercicios disponibles
object ExerciseDatabase {
    val pushUpVariants = listOf(
        ExerciseVariant("Flexiones Estándar", "Flexiones normales", 1),
        ExerciseVariant("Flexiones Diamante", "Manos juntas formando diamante", 2),
        ExerciseVariant("Flexiones Archer", "Flexiones con brazos asimétricos", 3),
        ExerciseVariant("Flexiones Pseudo Planche", "Flexiones con peso hacia adelante", 4),
        ExerciseVariant("Flexiones Planche", "Máxima dificultad", 5)
    )

    val pullUpVariants = listOf(
        ExerciseVariant("Dominadas Estándar", "Agarre normal", 1),
        ExerciseVariant("Dominadas Cerradas", "Agarre estrecho", 2),
        ExerciseVariant("Dominadas Archer", "Dominadas asimétricas", 3),
        ExerciseVariant("Dominadas Explosivas", "Con impulso y altura", 4),
        ExerciseVariant("Dominadas Planche", "Máxima dificultad", 5)
    )

    val dipVariants = listOf(
        ExerciseVariant("Fondos Estándar", "Fondos en barras paralelas", 1),
        ExerciseVariant("Fondos Cerrados", "Brazos pegados al cuerpo", 2),
        ExerciseVariant("Fondos Explosivos", "Con impulso", 3),
        ExerciseVariant("Fondos Planche", "Cuerpo extendido", 4),
        ExerciseVariant("Fondos Planche Completo", "Máxima dificultad", 5)
    )

    val burpeeVariants = listOf(
        ExerciseVariant("Burpees Estándar", "Burpees normales", 1),
        ExerciseVariant("Burpees con Flexión", "Burpees con flexión completa", 2),
        ExerciseVariant("Burpees Explosivos", "Con salto explosivo", 3),
        ExerciseVariant("Burpees Dobles", "Dos flexiones por repetición", 4),
        ExerciseVariant("Burpees Extremos", "Máxima dificultad", 5)
    )

    val abdominalVariants = listOf(
        ExerciseVariant("Abdominales Estándar", "Abdominales básicos", 1),
        ExerciseVariant("Abdominales Declinado", "En banco declinado", 2),
        ExerciseVariant("Abdominales Colgante", "Colgado de barra", 3),
        ExerciseVariant("Dragon Flag", "Levantamiento de cuerpo completo", 4),
        ExerciseVariant("Planche Abdominal", "Máxima dificultad", 5)
    )
}

