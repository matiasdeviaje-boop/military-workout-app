package com.militaryworkout.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.militaryworkout.app.databinding.ItemProgressBinding

class ProgressAdapter(
    private var progressItems: List<ExerciseProgressItem>
) : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    inner class ProgressViewHolder(private val binding: ItemProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExerciseProgressItem) {
            binding.apply {
                exerciseName.text = item.name
                totalSessions.text = item.totalSessions.toString()
                completedSessions.text = item.completedSessions.toString()
                maxReps.text = item.maxReps.toString()
                difficulty.text = "${item.currentDifficulty}/5"
                
                val completionRate = if (item.totalSessions > 0) {
                    (item.completedSessions.toDouble() / item.totalSessions) * 100
                } else {
                    0.0
                }
                
                progressBar.progress = completionRate.toInt()
                progressPercentage.text = String.format("%.0f%%", completionRate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val binding = ItemProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(progressItems[position])
    }

    override fun getItemCount() = progressItems.size

    fun updateProgress(newItems: List<ExerciseProgressItem>) {
        progressItems = newItems
        notifyDataSetChanged()
    }
}

