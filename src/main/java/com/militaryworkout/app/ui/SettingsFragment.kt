package com.militaryworkout.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.militaryworkout.app.data.WorkoutDatabase
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: WorkoutRepository
    private var baseRestTime = 60

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = WorkoutDatabase.getDatabase(requireContext())
        repository = WorkoutRepository(
            db.exerciseDao(),
            db.workoutDao(),
            db.workoutSetDao()
        )

        setupLevelSpinner()
        setupRestTimeControls()
        setupActionButtons()
    }

    private fun setupLevelSpinner() {
        val levels = arrayOf("Intermedio", "Avanzado", "Maestro", "Élite")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            levels
        )
        binding.settingsLevelSpinner.adapter = adapter
    }

    private fun setupRestTimeControls() {
        binding.restTimeValue.text = "$baseRestTime segundos"

        binding.btnIncreaseRest.setOnClickListener {
            baseRestTime = minOf(300, baseRestTime + 10)
            binding.restTimeValue.text = "$baseRestTime segundos"
        }

        binding.btnDecreaseRest.setOnClickListener {
            baseRestTime = maxOf(20, baseRestTime - 10)
            binding.restTimeValue.text = "$baseRestTime segundos"
        }
    }

    private fun setupActionButtons() {
        binding.btnClearData.setOnClickListener {
            showClearDataDialog()
        }

        binding.btnAbout.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun showClearDataDialog() {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Limpiar Datos")
            .setMessage("¿Estás seguro de que deseas eliminar todos los datos de entrenamiento?")
            .setPositiveButton("Sí") { _, _ ->
                clearAllData()
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }

    private fun clearAllData() {
        lifecycleScope.launch {
            try {
                // Eliminar todos los entrenamientos y sets
                repository.getAllWorkouts().collect { workouts ->
                    workouts.forEach { workout ->
                        repository.deleteSetsByWorkoutId(workout.id)
                        repository.deleteWorkout(workout)
                    }
                }
                
                Toast.makeText(
                    requireContext(),
                    "Todos los datos han sido eliminados",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al limpiar datos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showAboutDialog() {
        val aboutText = """
            Military Workout v1.0.0
            
            Entrenamiento de Tren Superior
            Estilo Militar y Alto Impacto
            
            Características:
            • Seguimiento diario de entrenamientos
            • Rutina progresiva y adaptativa
            • Almacenamiento local sin internet
            • Generación de reportes en PDF
            • Ejercicios de tren superior
            
            Desarrollado para máximo rendimiento
            y progresión constante.
        """.trimIndent()

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Acerca de Military Workout")
            .setMessage(aboutText)
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

