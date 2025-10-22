package com.militaryworkout.app

import android.app.Application
import android.content.Context
import com.militaryworkout.app.data.LocalStorageService
import com.militaryworkout.app.data.WorkoutDatabase
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.data.DatabaseInitializer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Clase principal de la aplicación
 */
class MilitaryWorkoutApp : Application() {

    companion object {
        private var instance: MilitaryWorkoutApp? = null
        private var repository: WorkoutRepository? = null
        private var storageService: LocalStorageService? = null

        fun getInstance(): MilitaryWorkoutApp {
            return instance ?: throw RuntimeException("Application not initialized")
        }

        fun getRepository(): WorkoutRepository {
            return repository ?: throw RuntimeException("Repository not initialized")
        }

        fun getStorageService(): LocalStorageService {
            return storageService ?: throw RuntimeException("Storage service not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Inicializar servicios
        initializeServices()
    }

    private fun initializeServices() {
        // Inicializar base de datos
        val db = WorkoutDatabase.getDatabase(this)
        repository = WorkoutRepository(
            db.exerciseDao(),
            db.workoutDao(),
            db.workoutSetDao()
        )

        // Inicializar almacenamiento local
        storageService = LocalStorageService(this)

        // Inicializar base de datos con ejercicios predeterminados
        GlobalScope.launch {
            try {
                DatabaseInitializer.initializeDatabase(repository!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Guardar versión de la app
        storageService?.saveAppVersion("1.0.0")
    }
}

