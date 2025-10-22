package com.militaryworkout.app.utils

import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Analiza el rendimiento y progreso del usuario
 */
object PerformanceAnalyzer {

    /**
     * Calcula las métricas de rendimiento general
     */
    fun calculatePerformanceMetrics(
        workouts: List<Workout>,
        workoutSets: Map<Int, List<WorkoutSet>>
    ): PerformanceMetrics {
        val completedWorkouts = workouts.count { it.completed }
        val totalWorkouts = workouts.size
        val completionRate = if (totalWorkouts > 0) {
            completedWorkouts.toDouble() / totalWorkouts
        } else {
            0.0
        }

        val totalVolume = workoutSets.values.sumOf { sets ->
            sets.sumOf { it.plannedSets * it.plannedReps }
        }

        val completedVolume = workoutSets.values.sumOf { sets ->
            sets.sumOf { it.completedSets * it.completedReps }
        }

        val averageDifficulty = if (workoutSets.isNotEmpty()) {
            workoutSets.values.flatten().map { it.difficulty }.average()
        } else {
            0.0
        }

        val averageRestTime = if (workoutSets.isNotEmpty()) {
            workoutSets.values.flatten().map { it.restTime }.average().toInt()
        } else {
            0
        }

        return PerformanceMetrics(
            totalWorkouts = totalWorkouts,
            completedWorkouts = completedWorkouts,
            completionRate = completionRate,
            totalVolume = totalVolume,
            completedVolume = completedVolume,
            volumeCompletionRate = if (totalVolume > 0) completedVolume.toDouble() / totalVolume else 0.0,
            averageDifficulty = averageDifficulty,
            averageRestTime = averageRestTime
        )
    }

    /**
     * Calcula la tendencia de progreso
     */
    fun calculateProgressTrend(
        workouts: List<Workout>,
        workoutSets: Map<Int, List<WorkoutSet>>,
        daysBack: Int = 30
    ): ProgressTrend {
        val cutoffDate = LocalDate.now().minusDays(daysBack.toLong())
        val dateFormatter = DateTimeFormatter.ISO_DATE

        val recentWorkouts = workouts.filter { workout ->
            LocalDate.parse(workout.date, dateFormatter).isAfter(cutoffDate)
        }

        val recentCompletionRate = if (recentWorkouts.isNotEmpty()) {
            recentWorkouts.count { it.completed }.toDouble() / recentWorkouts.size
        } else {
            0.0
        }

        // Calcular cambio de dificultad
        val firstWorkout = recentWorkouts.firstOrNull()
        val lastWorkout = recentWorkouts.lastOrNull()

        val firstDifficulty = firstWorkout?.let { workout ->
            workoutSets[workout.id]?.firstOrNull()?.difficulty ?: 1
        } ?: 1

        val lastDifficulty = lastWorkout?.let { workout ->
            workoutSets[workout.id]?.firstOrNull()?.difficulty ?: 1
        } ?: 1

        val difficultyChange = lastDifficulty - firstDifficulty

        return ProgressTrend(
            daysAnalyzed = daysBack,
            recentWorkouts = recentWorkouts.size,
            recentCompletionRate = recentCompletionRate,
            difficultyChange = difficultyChange,
            isProgressing = recentCompletionRate > 0.75 && difficultyChange >= 0
        )
    }

    /**
     * Calcula el índice de consistencia
     */
    fun calculateConsistencyIndex(workouts: List<Workout>): ConsistencyIndex {
        if (workouts.isEmpty()) return ConsistencyIndex(0, 0, 0.0, "Sin datos")

        val sortedWorkouts = workouts.sortedBy { it.date }
        val dateFormatter = DateTimeFormatter.ISO_DATE

        var consecutiveDays = 0
        var maxConsecutiveDays = 0
        var currentStreak = 0

        var lastDate: LocalDate? = null

        for (workout in sortedWorkouts) {
            val currentDate = LocalDate.parse(workout.date, dateFormatter)

            if (lastDate == null || currentDate.minusDays(1) == lastDate) {
                currentStreak++
                if (currentStreak > maxConsecutiveDays) {
                    maxConsecutiveDays = currentStreak
                }
            } else {
                currentStreak = 1
            }

            lastDate = currentDate
        }

        val totalDays = (LocalDate.parse(sortedWorkouts.last().date, dateFormatter)
            .toEpochDay() - LocalDate.parse(sortedWorkouts.first().date, dateFormatter).toEpochDay()).toInt() + 1

        val consistencyScore = if (totalDays > 0) {
            (workouts.size.toDouble() / totalDays) * 100
        } else {
            0.0
        }

        val consistency = when {
            consistencyScore >= 90 -> "Excelente"
            consistencyScore >= 75 -> "Muy Buena"
            consistencyScore >= 60 -> "Buena"
            consistencyScore >= 45 -> "Regular"
            else -> "Necesita Mejora"
        }

        return ConsistencyIndex(
            currentStreak = currentStreak,
            maxConsecutiveDays = maxConsecutiveDays,
            consistencyScore = consistencyScore,
            consistency = consistency
        )
    }

    /**
     * Calcula el índice de ganancia de fuerza
     */
    fun calculateStrengthGain(
        workoutSets: Map<Int, List<WorkoutSet>>
    ): StrengthGain {
        val allSets = workoutSets.values.flatten()

        val exerciseProgress = mutableMapOf<String, ExerciseGain>()

        for (set in allSets) {
            val gain = exerciseProgress.getOrPut(set.exerciseName) {
                ExerciseGain(
                    exerciseName = set.exerciseName,
                    startDifficulty = set.difficulty,
                    currentDifficulty = set.difficulty,
                    maxReps = set.plannedReps,
                    totalSessions = 0
                )
            }

            gain.currentDifficulty = set.difficulty
            gain.maxReps = maxOf(gain.maxReps, set.plannedReps)
            gain.totalSessions++
        }

        val totalGain = exerciseProgress.values.sumOf { it.currentDifficulty - it.startDifficulty }
        val averageGain = if (exerciseProgress.isNotEmpty()) {
            totalGain.toDouble() / exerciseProgress.size
        } else {
            0.0
        }

        return StrengthGain(
            exerciseGains = exerciseProgress,
            totalDifficultyGain = totalGain,
            averageDifficultyGain = averageGain
        )
    }

    /**
     * Genera recomendaciones basadas en el análisis
     */
    fun generateRecommendations(
        metrics: PerformanceMetrics,
        trend: ProgressTrend,
        consistency: ConsistencyIndex
    ): List<String> {
        val recommendations = mutableListOf<String>()

        // Recomendaciones por completación
        when {
            metrics.completionRate >= 0.95 -> {
                recommendations.add("Excelente adherencia. Considera aumentar la dificultad.")
            }
            metrics.completionRate >= 0.85 -> {
                recommendations.add("Muy buen desempeño. Mantén la consistencia.")
            }
            metrics.completionRate >= 0.70 -> {
                recommendations.add("Buen progreso. Trabaja en mejorar la consistencia.")
            }
            metrics.completionRate >= 0.50 -> {
                recommendations.add("Necesitas mejorar tu adherencia. Intenta entrenar más días.")
            }
            else -> {
                recommendations.add("Muy baja adherencia. Recalibrá tus objetivos.")
            }
        }

        // Recomendaciones por dificultad
        if (trend.difficultyChange > 0) {
            recommendations.add("Progresión de dificultad detectada. Excelente trabajo.")
        } else if (trend.difficultyChange < 0) {
            recommendations.add("Dificultad en descenso. Considera entrenar más consistentemente.")
        }

        // Recomendaciones por consistencia
        if (consistency.consistencyScore >= 80) {
            recommendations.add("Consistencia excelente. Eres un atleta disciplinado.")
        } else if (consistency.consistencyScore < 50) {
            recommendations.add("Necesitas entrenar más regularmente para ver resultados.")
        }

        // Recomendaciones por volumen
        if (metrics.volumeCompletionRate < 0.70) {
            recommendations.add("Completas menos del 70% del volumen planeado. Reduce la dificultad.")
        }

        return recommendations
    }
}

data class PerformanceMetrics(
    val totalWorkouts: Int,
    val completedWorkouts: Int,
    val completionRate: Double,
    val totalVolume: Int,
    val completedVolume: Int,
    val volumeCompletionRate: Double,
    val averageDifficulty: Double,
    val averageRestTime: Int
)

data class ProgressTrend(
    val daysAnalyzed: Int,
    val recentWorkouts: Int,
    val recentCompletionRate: Double,
    val difficultyChange: Int,
    val isProgressing: Boolean
)

data class ConsistencyIndex(
    val currentStreak: Int,
    val maxConsecutiveDays: Int,
    val consistencyScore: Double,
    val consistency: String
)

data class StrengthGain(
    val exerciseGains: Map<String, ExerciseGain>,
    val totalDifficultyGain: Int,
    val averageDifficultyGain: Double
)

data class ExerciseGain(
    val exerciseName: String,
    val startDifficulty: Int,
    var currentDifficulty: Int,
    var maxReps: Int,
    var totalSessions: Int
)

