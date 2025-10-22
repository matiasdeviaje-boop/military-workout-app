package com.militaryworkout.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Servicio de almacenamiento local sin conexión a internet
 */
class LocalStorageService(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "workout_preferences",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    // Claves de preferencias
    companion object {
        private const val KEY_CURRENT_LEVEL = "current_level"
        private const val KEY_BASE_REST_TIME = "base_rest_time"
        private const val KEY_LAST_WORKOUT_DATE = "last_workout_date"
        private const val KEY_TOTAL_WORKOUTS = "total_workouts"
        private const val KEY_COMPLETED_WORKOUTS = "completed_workouts"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_MAX_STREAK = "max_streak"
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_APP_VERSION = "app_version"
    }

    /**
     * Guarda el nivel actual del usuario
     */
    fun saveCurrentLevel(level: String) {
        sharedPreferences.edit().putString(KEY_CURRENT_LEVEL, level).apply()
    }

    /**
     * Obtiene el nivel actual del usuario
     */
    fun getCurrentLevel(): String {
        return sharedPreferences.getString(KEY_CURRENT_LEVEL, "Intermedio") ?: "Intermedio"
    }

    /**
     * Guarda el tiempo de descanso base
     */
    fun saveBaseRestTime(seconds: Int) {
        sharedPreferences.edit().putInt(KEY_BASE_REST_TIME, seconds).apply()
    }

    /**
     * Obtiene el tiempo de descanso base
     */
    fun getBaseRestTime(): Int {
        return sharedPreferences.getInt(KEY_BASE_REST_TIME, 60)
    }

    /**
     * Guarda la fecha del último entrenamiento
     */
    fun saveLastWorkoutDate(date: String) {
        sharedPreferences.edit().putString(KEY_LAST_WORKOUT_DATE, date).apply()
    }

    /**
     * Obtiene la fecha del último entrenamiento
     */
    fun getLastWorkoutDate(): String {
        return sharedPreferences.getString(KEY_LAST_WORKOUT_DATE, "N/A") ?: "N/A"
    }

    /**
     * Incrementa el contador de entrenamientos totales
     */
    fun incrementTotalWorkouts() {
        val current = sharedPreferences.getInt(KEY_TOTAL_WORKOUTS, 0)
        sharedPreferences.edit().putInt(KEY_TOTAL_WORKOUTS, current + 1).apply()
    }

    /**
     * Obtiene el total de entrenamientos
     */
    fun getTotalWorkouts(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_WORKOUTS, 0)
    }

    /**
     * Incrementa el contador de entrenamientos completados
     */
    fun incrementCompletedWorkouts() {
        val current = sharedPreferences.getInt(KEY_COMPLETED_WORKOUTS, 0)
        sharedPreferences.edit().putInt(KEY_COMPLETED_WORKOUTS, current + 1).apply()
    }

    /**
     * Obtiene el total de entrenamientos completados
     */
    fun getCompletedWorkouts(): Int {
        return sharedPreferences.getInt(KEY_COMPLETED_WORKOUTS, 0)
    }

    /**
     * Actualiza la racha de entrenamientos consecutivos
     */
    fun updateStreak(lastWorkoutDate: String) {
        val today = LocalDate.now().format(dateFormatter)
        val lastDate = LocalDate.parse(lastWorkoutDate, dateFormatter)
        val currentDate = LocalDate.parse(today, dateFormatter)

        val daysDifference = currentDate.toEpochDay() - lastDate.toEpochDay()

        val currentStreak = sharedPreferences.getInt(KEY_CURRENT_STREAK, 0)
        val maxStreak = sharedPreferences.getInt(KEY_MAX_STREAK, 0)

        val newStreak = when {
            daysDifference == 1L -> currentStreak + 1
            daysDifference == 0L -> currentStreak
            else -> 1
        }

        val newMaxStreak = maxOf(newStreak, maxStreak)

        sharedPreferences.edit().apply {
            putInt(KEY_CURRENT_STREAK, newStreak)
            putInt(KEY_MAX_STREAK, newMaxStreak)
            apply()
        }
    }

    /**
     * Obtiene la racha actual
     */
    fun getCurrentStreak(): Int {
        return sharedPreferences.getInt(KEY_CURRENT_STREAK, 0)
    }

    /**
     * Obtiene la racha máxima
     */
    fun getMaxStreak(): Int {
        return sharedPreferences.getInt(KEY_MAX_STREAK, 0)
    }

    /**
     * Guarda la fecha de última sincronización
     */
    fun saveLastSync() {
        val now = System.currentTimeMillis()
        sharedPreferences.edit().putLong(KEY_LAST_SYNC, now).apply()
    }

    /**
     * Obtiene la fecha de última sincronización
     */
    fun getLastSync(): Long {
        return sharedPreferences.getLong(KEY_LAST_SYNC, 0)
    }

    /**
     * Verifica si necesita sincronización
     */
    fun needsSync(): Boolean {
        val lastSync = getLastSync()
        val now = System.currentTimeMillis()
        val oneHourInMillis = 3600000

        return (now - lastSync) > oneHourInMillis
    }

    /**
     * Guarda la versión de la app
     */
    fun saveAppVersion(version: String) {
        sharedPreferences.edit().putString(KEY_APP_VERSION, version).apply()
    }

    /**
     * Obtiene la versión de la app
     */
    fun getAppVersion(): String {
        return sharedPreferences.getString(KEY_APP_VERSION, "1.0.0") ?: "1.0.0"
    }

    /**
     * Limpia todos los datos almacenados
     */
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Exporta datos como JSON para backup
     */
    fun exportDataAsJson(workouts: List<Workout>, sets: List<WorkoutSet>): String {
        val data = mapOf(
            "workouts" to workouts,
            "sets" to sets,
            "metadata" to mapOf(
                "exportDate" to LocalDate.now().format(dateFormatter),
                "appVersion" to getAppVersion(),
                "currentLevel" to getCurrentLevel()
            )
        )
        return gson.toJson(data)
    }

    /**
     * Importa datos desde JSON
     */
    fun importDataFromJson(jsonData: String): Boolean {
        return try {
            val data = gson.fromJson(jsonData, Map::class.java)
            // Aquí se implementaría la lógica de importación
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

