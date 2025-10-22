package com.militaryworkout.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.militaryworkout.app.databinding.ItemExerciseBinding
import com.militaryworkout.app.models.WorkoutSet

class ExerciseAdapter(
    private var exercises: List<WorkoutSet>
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: WorkoutSet) {
            binding.apply {
                exerciseName.text = exercise.exerciseName
                exerciseVariant.text = exercise.variant
                difficultyBadge.text = "${exercise.difficulty}/5"
                repsCount.text = exercise.plannedReps.toString()
                setsCount.text = exercise.plannedSets.toString()
                restTime.text = exercise.restTime.toString()

                // Configurar listeners para botones
                btnIncreaseReps.setOnClickListener {
                    val newReps = exercise.plannedReps + 1
                    repsCount.text = newReps.toString()
                    exercise.copy(plannedReps = newReps)
                }

                btnDecreaseReps.setOnClickListener {
                    val newReps = maxOf(1, exercise.plannedReps - 1)
                    repsCount.text = newReps.toString()
                    exercise.copy(plannedReps = newReps)
                }

                btnIncreaseSets.setOnClickListener {
                    val newSets = exercise.plannedSets + 1
                    setsCount.text = newSets.toString()
                    exercise.copy(plannedSets = newSets)
                }

                btnDecreaseSets.setOnClickListener {
                    val newSets = maxOf(1, exercise.plannedSets - 1)
                    setsCount.text = newSets.toString()
                    exercise.copy(plannedSets = newSets)
                }

                exerciseCompleted.isChecked = exercise.completedReps > 0
                exerciseCompleted.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        exercise.copy(completedReps = exercise.plannedReps)
                    } else {
                        exercise.copy(completedReps = 0)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount() = exercises.size

    fun updateExercises(newExercises: List<WorkoutSet>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}

