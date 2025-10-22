package com.militaryworkout.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.militaryworkout.app.data.WorkoutDatabase
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.databinding.FragmentProgressBinding
import com.militaryworkout.app.utils.PDFGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: WorkoutRepository
    private lateinit var progressAdapter: ProgressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
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

        setupRecyclerView()
        setupButtons()
        loadProgressData()
    }

    private fun setupRecyclerView() {
        progressAdapter = ProgressAdapter(emptyList())
        binding.exerciseProgressRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.exerciseProgressRecycler.adapter = progressAdapter
    }

    private fun setupButtons() {
        binding.btnExportPdf.setOnClickListener {
            exportProgressPDF()
        }

        binding.btnViewHistory.setOnClickListener {
            // Navegar a historial
            Toast.makeText(requireContext(), "Historial de entrenamientos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProgressData() {
        lifecycleScope.launch {
            val stats = repository.getProgressStats()
            
            binding.progressTotalWorkouts.text = stats.totalWorkouts.toString()
            binding.progressCompleted.text = stats.completedWorkouts.toString()
            binding.progressRate.text = String.format("%.1f%%", stats.progressionPercentage)

            // Cargar progreso por ejercicio
            repository.getAllWorkouts().collect { workouts ->
                val workoutSets = mutableMapOf<Int, List<WorkoutSet>>()
                workouts.forEach { workout ->
                    workoutSets[workout.id] = repository.getSetsByWorkoutId(workout.id)
                }
                
                val exerciseProgress = calculateExerciseProgress(workoutSets)
                progressAdapter.updateProgress(exerciseProgress)
            }
        }
    }

    private suspend fun calculateExerciseProgress(
        workoutSets: Map<Int, List<WorkoutSet>>
    ): List<ExerciseProgressItem> {
        val exerciseMap = mutableMapOf<String, ExerciseProgressItem>()

        for ((_, sets) in workoutSets) {
            for (set in sets) {
                val item = exerciseMap.getOrPut(set.exerciseName) {
                    ExerciseProgressItem(
                        name = set.exerciseName,
                        totalSessions = 0,
                        completedSessions = 0,
                        maxReps = 0,
                        currentDifficulty = set.difficulty
                    )
                }
                
                item.totalSessions++
                if (set.completedReps > 0) {
                    item.completedSessions++
                    item.maxReps = maxOf(item.maxReps, set.completedReps)
                }
                item.currentDifficulty = set.difficulty
            }
        }

        return exerciseMap.values.toList()
    }

    private fun exportProgressPDF() {
        lifecycleScope.launch {
            try {
                val workouts = mutableListOf<com.militaryworkout.app.models.Workout>()
                val workoutSets = mutableMapOf<Int, List<com.militaryworkout.app.models.WorkoutSet>>()
                
                repository.getAllWorkouts().collect { allWorkouts ->
                    workouts.addAll(allWorkouts)
                    allWorkouts.forEach { workout ->
                        workoutSets[workout.id] = repository.getSetsByWorkoutId(workout.id)
                    }
                }

                val stats = repository.getProgressStats()
                
                val pdfFile = PDFGenerator.generateProgressReport(
                    requireContext(),
                    workouts,
                    workoutSets,
                    stats
                )

                Toast.makeText(
                    requireContext(),
                    "PDF guardado: ${pdfFile.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al generar PDF: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ExerciseProgressItem(
    val name: String,
    var totalSessions: Int,
    var completedSessions: Int,
    var maxReps: Int,
    var currentDifficulty: Int
)

