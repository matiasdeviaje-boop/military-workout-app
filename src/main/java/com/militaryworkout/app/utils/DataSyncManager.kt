package com.militaryworkout.app.utils

import com.militaryworkout.app.data.LocalStorageService
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Gestor de sincronización de datos sin conexión
 */
class DataSyncManager(
    private val repository: WorkoutRepository,
    private val storageService: LocalStorageService
) {

    private val dateFormatter = DateTimeFormatter.ISO_DATE

    /**
     * Sincroniza datos locales (sin conexión a internet)
     */
    suspend fun syncLocalData() {
        try {
            // Actualizar estadísticas locales
            updateLocalStatistics()

            // Guardar timestamp de sincronización
            storageService.saveLastSync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Actualiza las estadísticas locales
     */
    private suspend fun updateLocalStatistics() {
        val stats = repository.getProgressStats()

        // Guardar última fecha de entrenamiento
        val lastWorkout = repository.getLastWorkout()
        if (lastWorkout != null) {
            storageService.saveLastWorkoutDate(lastWorkout.date)
            storageService.updateStreak(lastWorkout.date)
        }
    }

    /**
     * Verifica la integridad de los datos
     */
    suspend fun verifyDataIntegrity(): Boolean {
        return try {
            val stats = repository.getProgressStats()
            stats.totalWorkouts >= 0 && stats.completedWorkouts >= 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Realiza un backup de los datos
     */
    suspend fun backupData(): String {
        return try {
            val workouts = mutableListOf<Workout>()
            val sets = mutableListOf<WorkoutSet>()

            repository.getAllWorkouts().collect { allWorkouts ->
                workouts.addAll(allWorkouts)
                allWorkouts.forEach { workout ->
                    sets.addAll(repository.getSetsByWorkoutId(workout.id))
                }
            }

            storageService.exportDataAsJson(workouts, sets)
        } catch (e: Exception) {
            e.printStackTrace()
            "{}"
        }
    }

    /**
     * Realiza una restauración de datos
     */
    suspend fun restoreData(jsonData: String): Boolean {
        return try {
            storageService.importDataFromJson(jsonData)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Limpia datos antiguos (más de 1 año)
     */
    suspend fun cleanupOldData() {
        try {
            val oneYearAgo = LocalDate.now().minusYears(1).format(dateFormatter)
            
            repository.getAllWorkouts().collect { workouts ->
                workouts.forEach { workout ->
                    if (workout.date < oneYearAgo) {
                        repository.deleteSetsByWorkoutId(workout.id)
                        repository.deleteWorkout(workout)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Obtiene el estado de sincronización
     */
    fun getSyncStatus(): SyncStatus {
        val lastSync = storageService.getLastSync()
        val now = System.currentTimeMillis()
        val timeSinceSync = (now - lastSync) / 1000 // en segundos

        val status = when {
            lastSync == 0L -> "Nunca sincronizado"
            timeSinceSync < 60 -> "Sincronizado ahora"
            timeSinceSync < 3600 -> "Sincronizado hace ${timeSinceSync / 60} minutos"
            timeSinceSync < 86400 -> "Sincronizado hace ${timeSinceSync / 3600} horas"
            else -> "Sincronizado hace ${timeSinceSync / 86400} días"
        }

        return SyncStatus(
            lastSyncTime = lastSync,
            status = status,
            needsSync = storageService.needsSync()
        )
    }
}

data class SyncStatus(
    val lastSyncTime: Long,
    val status: String,
    val needsSync: Boolean
)

