package com.militaryworkout.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.militaryworkout.app.R
import com.militaryworkout.app.data.WorkoutDatabase
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.databinding.FragmentHomeBinding
import com.militaryworkout.app.utils.WorkoutGenerator
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: WorkoutRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        setupButtons()
        loadStatistics()
    }

    private fun setupLevelSpinner() {
        val levels = arrayOf("Intermedio", "Avanzado", "Maestro", "Élite")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            levels
        )
        binding.levelSpinner.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnStartWorkout.setOnClickListener {
            val selectedLevel = binding.levelSpinner.selectedItem.toString()
            generateAndStartWorkout(selectedLevel)
        }

        binding.btnGenerateReport.setOnClickListener {
            // Navegar a pantalla de progreso
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProgressFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun generateAndStartWorkout(level: String) {
        lifecycleScope.launch {
            val (workout, sets) = WorkoutGenerator.generateDailyWorkout(level)
            
            val workoutId = repository.insertWorkout(workout).toInt()
            
            sets.forEach { set ->
                val updatedSet = set.copy(workoutId = workoutId)
                repository.insertWorkoutSet(updatedSet)
            }

            // Navegar a pantalla de entrenamiento
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadStatistics() {
        lifecycleScope.launch {
            val stats = repository.getProgressStats()
            
            binding.statTotalWorkouts.text = stats.totalWorkouts.toString()
            binding.statCompleted.text = stats.completedWorkouts.toString()
            binding.statCompletionRate.text = String.format("%.1f%%", stats.progressionPercentage)
            
            binding.lastWorkoutInfo.text = "Último: ${stats.lastWorkoutDate}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

