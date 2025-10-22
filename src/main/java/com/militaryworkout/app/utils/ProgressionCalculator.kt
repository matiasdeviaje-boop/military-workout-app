package com.militaryworkout.app.utils

import com.militaryworkout.app.models.WorkoutSet

/**
 * Calcula la progresión de entrenamientos basada en rendimiento histórico
 */
object ProgressionCalculator {

    /**
     * Calcula el siguiente nivel de dificultad basado en el desempeño
     */
    fun calculateNextDifficulty(
        currentDifficulty: Int,
        completedSessions: Int,
        totalSessions: Int,
        daysSinceStart: Int
    ): Int {
        val completionRate = if (totalSessions > 0) {
            completedSessions.toDouble() / totalSessions
        } else {
            0.0
        }

        return when {
            // Aumentar dificultad si completó 90% o más y ha pasado 2 semanas
            completionRate >= 0.9 && daysSinceStart >= 14 && currentDifficulty < 5 -> {
                currentDifficulty + 1
            }
            // Aumentar dificultad si completó 80% o más y ha pasado 3 semanas
            completionRate >= 0.8 && daysSinceStart >= 21 && currentDifficulty < 5 -> {
                currentDifficulty + 1
            }
            // Mantener dificultad si está en rango 70-90%
            completionRate in 0.7..0.9 -> currentDifficulty
            // Reducir dificultad si está por debajo de 70%
            completionRate < 0.7 && currentDifficulty > 1 -> currentDifficulty - 1
            else -> currentDifficulty
        }
    }

    /**
     * Calcula las repeticiones progresivas basadas en dificultad y día
     */
    fun calculateProgressiveReps(
        baseReps: Int,
        difficulty: Int,
        dayNumber: Int
    ): Int {
        // Incremento base por dificultad
        val difficultyMultiplier = difficulty * 0.5

        // Incremento por semana (cada 7 días)
        val weekNumber = dayNumber / 7
        val weekIncrement = weekNumber * 2

        // Incremento cada 3 días
        val microCycleIncrement = (dayNumber % 7) / 3

        return (baseReps + (baseReps * difficultyMultiplier).toInt() + weekIncrement + microCycleIncrement).coerceIn(1, 100)
    }

    /**
     * Calcula las series progresivas
     */
    fun calculateProgressiveSets(
        baseSets: Int,
        difficulty: Int,
        dayNumber: Int
    ): Int {
        val difficultyBonus = (difficulty - 1) * 0.5
        val weekNumber = dayNumber / 7
        val weekBonus = weekNumber * 0.3

        return (baseSets + (baseSets * difficultyBonus).toInt() + (baseSets * weekBonus).toInt()).coerceIn(1, 10)
    }

    /**
     * Calcula el tiempo de descanso reducido progresivamente
     */
    fun calculateProgressiveRestTime(
        baseRestTime: Int,
        difficulty: Int,
        dayNumber: Int
    ): Int {
        // Reducción por dificultad: cada nivel reduce 5 segundos
        val difficultyReduction = (difficulty - 1) * 5

        // Reducción por semana: cada semana reduce 3 segundos
        val weekNumber = dayNumber / 7
        val weekReduction = weekNumber * 3

        // Reducción micro-ciclo: cada 3 días reduce 1 segundo
        val microCycleReduction = (dayNumber % 7) / 3

        val totalReduction = difficultyReduction + weekReduction + microCycleReduction

        return maxOf(20, baseRestTime - totalReduction) // Mínimo 20 segundos
    }

    /**
     * Calcula el volumen total de entrenamiento
     */
    fun calculateTotalVolume(sets: List<WorkoutSet>): Int {
        return sets.sumOf { it.plannedSets * it.plannedReps }
    }

    /**
     * Calcula el volumen completado
     */
    fun calculateCompletedVolume(sets: List<WorkoutSet>): Int {
        return sets.sumOf { it.completedSets * it.completedReps }
    }

    /**
     * Calcula la intensidad relativa (0-100)
     */
    fun calculateIntensity(
        difficulty: Int,
        averageReps: Int
    ): Int {
        // Escala: dificultad 1 = 40%, dificultad 5 = 100%
        val difficultyIntensity = 40 + (difficulty - 1) * 15

        // Ajuste por reps: más reps = menos intensidad
        val repsAdjustment = when {
            averageReps > 20 -> -10
            averageReps > 15 -> -5
            averageReps > 10 -> 0
            else -> 5
        }

        return (difficultyIntensity + repsAdjustment).coerceIn(0, 100)
    }

    /**
     * Determina si es tiempo de descanso (día de recuperación)
     */
    fun isRestDay(dayNumber: Int): Boolean {
        // Cada 7 días, tomar un día de descanso
        return dayNumber % 7 == 0
    }

    /**
     * Calcula el índice de fatiga (0-100)
     */
    fun calculateFatigueIndex(
        consecutiveTrainingDays: Int,
        averageCompletionRate: Double
    ): Int {
        // Base: 10% por cada día consecutivo
        val fatigueFromDays = minOf(50, consecutiveTrainingDays * 10)

        // Ajuste por completación: menos fatiga si no completa
        val fatigueAdjustment = if (averageCompletionRate < 0.7) {
            -20
        } else if (averageCompletionRate > 0.9) {
            10
        } else {
            0
        }

        return (fatigueFromDays + fatigueAdjustment).coerceIn(0, 100)
    }

    /**
     * Recomendación de acción basada en el rendimiento
     */
    fun getProgressionRecommendation(
        completionRate: Double,
        fatigueIndex: Int,
        currentDifficulty: Int
    ): String {
        return when {
            fatigueIndex > 70 -> "Toma un día de descanso, estás muy fatigado"
            completionRate >= 0.95 && currentDifficulty < 5 -> "Excelente rendimiento, aumenta la dificultad"
            completionRate >= 0.85 && currentDifficulty < 5 -> "Buen rendimiento, considera aumentar dificultad"
            completionRate in 0.7..0.85 -> "Rendimiento consistente, mantén el ritmo"
            completionRate in 0.5..0.7 -> "Necesitas mejorar, reduce dificultad si es necesario"
            else -> "Bajo rendimiento, tómate un descanso y recalibrá"
        }
    }
}

