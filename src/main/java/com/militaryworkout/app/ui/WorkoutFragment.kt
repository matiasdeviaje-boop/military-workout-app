package com.militaryworkout.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.militaryworkout.app.data.WorkoutDatabase
import com.militaryworkout.app.data.WorkoutRepository
import com.militaryworkout.app.databinding.FragmentWorkoutBinding
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: WorkoutRepository
    private lateinit var adapter: ExerciseAdapter
    private var currentWorkout: Workout? = null
    private var workoutSets: List<WorkoutSet> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
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
        loadTodayWorkout()
    }

    private fun setupRecyclerView() {
        adapter = ExerciseAdapter(emptyList())
        binding.exercisesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.exercisesRecycler.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnCompleteWorkout.setOnClickListener {
            completeWorkout()
        }

        binding.btnSkipWorkout.setOnClickListener {
            skipWorkout()
        }
    }

    private fun loadTodayWorkout() {
        lifecycleScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val workout = repository.getWorkoutByDate(today)

            if (workout != null) {
                currentWorkout = workout
                binding.workoutDate.text = "Hoy - ${today}"
                binding.workoutLevel.text = workout.level

                workoutSets = repository.getSetsByWorkoutId(workout.id)
                adapter.updateExercises(workoutSets)
            }
        }
    }

    private fun completeWorkout() {
        currentWorkout?.let { workout ->
            lifecycleScope.launch {
                val completedWorkout = workout.copy(
                    completed = true,
                    totalDuration = calculateDuration()
                )
                repository.updateWorkout(completedWorkout)
                
                // Actualizar sets completados
                workoutSets.forEach { set ->
                    if (set.completedReps > 0) {
                        repository.updateWorkoutSet(set)
                    }
                }

                // Volver a inicio
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun skipWorkout() {
        currentWorkout?.let { workout ->
            lifecycleScope.launch {
                repository.deleteWorkout(workout)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun calculateDuration(): Int {
        // Calcular duración basada en series, repeticiones y descanso
        var duration = 0
        workoutSets.forEach { set ->
            duration += (set.completedSets * set.completedReps * 2) + (set.completedSets * set.restTime)
        }
        return duration / 60 // Convertir a minutos
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

