package com.militaryworkout.app.utils

import android.content.Context
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.militaryworkout.app.models.ProgressStats
import com.militaryworkout.app.models.Workout
import com.militaryworkout.app.models.WorkoutSet
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PDFGenerator {

    private val titleFont = Font(Font.FontFamily.HELVETICA, 20f, Font.BOLD, BaseColor.BLACK)
    private val headerFont = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, BaseColor.WHITE)
    private val subHeaderFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.BLACK)
    private val normalFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)
    private val smallFont = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, BaseColor.DARK_GRAY)

    fun generateProgressReport(
        context: Context,
        workouts: List<Workout>,
        workoutSets: Map<Int, List<WorkoutSet>>,
        stats: ProgressStats,
        fileName: String = "workout_progress_${LocalDate.now()}.pdf"
    ): File {
        val file = File(context.getExternalFilesDir(null), fileName)
        val document = Document()
        
        try {
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Encabezado
            addHeader(document, stats)

            // Estadísticas generales
            addStatistics(document, stats)

            // Historial de entrenamientos
            addWorkoutHistory(document, workouts, workoutSets)

            // Análisis de progresión
            addProgressionAnalysis(document, workouts, workoutSets)

            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    private fun addHeader(document: Document, stats: ProgressStats) {
        val title = Paragraph("REPORTE DE PROGRESO DE ENTRENAMIENTO", titleFont)
        title.alignment = Element.ALIGN_CENTER
        document.add(title)

        val date = Paragraph(
            "Generado: ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
            smallFont
        )
        date.alignment = Element.ALIGN_CENTER
        document.add(date)

        val level = Paragraph("Nivel Actual: ${stats.currentLevel}", subHeaderFont)
        level.alignment = Element.ALIGN_CENTER
        document.add(level)

        document.add(Paragraph("\n"))
    }

    private fun addStatistics(document: Document, stats: ProgressStats) {
        document.add(Paragraph("ESTADÍSTICAS GENERALES", subHeaderFont))

        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(25f, 25f, 25f, 25f))

        val headers = arrayOf(
            "Total Entrenamientos",
            "Completados",
            "Tasa Completación",
            "Último Entrenamiento"
        )

        for (header in headers) {
            val cell = PdfPCell(Phrase(header, headerFont))
            cell.backgroundColor = BaseColor(64, 64, 64)
            cell.borderColor = BaseColor.BLACK
            cell.padding = 8f
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }

        val completionRate = String.format("%.1f%%", stats.progressionPercentage)
        val values = arrayOf(
            stats.totalWorkouts.toString(),
            stats.completedWorkouts.toString(),
            completionRate,
            stats.lastWorkoutDate
        )

        for (value in values) {
            val cell = PdfPCell(Phrase(value, normalFont))
            cell.padding = 8f
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }

        document.add(table)
        document.add(Paragraph("\n"))
    }

    private fun addWorkoutHistory(
        document: Document,
        workouts: List<Workout>,
        workoutSets: Map<Int, List<WorkoutSet>>
    ) {
        document.add(Paragraph("HISTORIAL DE ENTRENAMIENTOS", subHeaderFont))

        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(15f, 15f, 20f, 20f, 15f, 15f))

        val headers = arrayOf("Fecha", "Nivel", "Ejercicios", "Completados", "Duración", "Estado")

        for (header in headers) {
            val cell = PdfPCell(Phrase(header, headerFont))
            cell.backgroundColor = BaseColor(64, 64, 64)
            cell.borderColor = BaseColor.BLACK
            cell.padding = 8f
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }

        for (workout in workouts.take(30)) { // Últimos 30 entrenamientos
            val sets = workoutSets[workout.id] ?: emptyList()
            val completedSets = sets.count { it.completedReps > 0 }
            val status = if (workout.completed) "Completado" else "Pendiente"
            val statusColor = if (workout.completed) BaseColor(0, 128, 0) else BaseColor(255, 165, 0)

            val cells = arrayOf(
                PdfPCell(Phrase(workout.date, normalFont)),
                PdfPCell(Phrase(workout.level, normalFont)),
                PdfPCell(Phrase(sets.size.toString(), normalFont)),
                PdfPCell(Phrase("$completedSets/${sets.size}", normalFont)),
                PdfPCell(Phrase("${workout.totalDuration} min", normalFont)),
                PdfPCell(Phrase(status, normalFont)).apply { backgroundColor = statusColor }
            )

            for (cell in cells) {
                cell.padding = 6f
                cell.horizontalAlignment = Element.ALIGN_CENTER
                table.addCell(cell)
            }
        }

        document.add(table)
        document.add(Paragraph("\n"))
    }

    private fun addProgressionAnalysis(
        document: Document,
        workouts: List<Workout>,
        workoutSets: Map<Int, List<WorkoutSet>>
    ) {
        document.add(Paragraph("ANÁLISIS DE PROGRESIÓN", subHeaderFont))

        val exerciseProgress = mutableMapOf<String, ExerciseStats>()

        for (workout in workouts) {
            val sets = workoutSets[workout.id] ?: continue
            for (set in sets) {
                val stats = exerciseProgress.getOrPut(set.exerciseName) {
                    ExerciseStats(set.exerciseName)
                }
                stats.totalSessions++
                if (set.completedReps > 0) {
                    stats.completedSessions++
                    stats.totalReps += set.completedReps
                    stats.maxReps = maxOf(stats.maxReps, set.completedReps)
                }
                stats.currentDifficulty = set.difficulty
            }
        }

        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(20f, 15f, 15f, 15f, 15f, 20f))

        val headers = arrayOf("Ejercicio", "Sesiones", "Completadas", "Reps Máx", "Dificultad", "Progresión")

        for (header in headers) {
            val cell = PdfPCell(Phrase(header, headerFont))
            cell.backgroundColor = BaseColor(64, 64, 64)
            cell.borderColor = BaseColor.BLACK
            cell.padding = 8f
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }

        for ((_, stats) in exerciseProgress) {
            val completionRate = if (stats.totalSessions > 0) {
                (stats.completedSessions.toDouble() / stats.totalSessions) * 100
            } else {
                0.0
            }

            val progression = when {
                completionRate >= 90 -> "Excelente"
                completionRate >= 75 -> "Bueno"
                completionRate >= 50 -> "Regular"
                else -> "Necesita mejora"
            }

            val cells = arrayOf(
                PdfPCell(Phrase(stats.name, normalFont)),
                PdfPCell(Phrase(stats.totalSessions.toString(), normalFont)),
                PdfPCell(Phrase(stats.completedSessions.toString(), normalFont)),
                PdfPCell(Phrase(stats.maxReps.toString(), normalFont)),
                PdfPCell(Phrase("${stats.currentDifficulty}/5", normalFont)),
                PdfPCell(Phrase(progression, normalFont))
            )

            for (cell in cells) {
                cell.padding = 6f
                cell.horizontalAlignment = Element.ALIGN_CENTER
                table.addCell(cell)
            }
        }

        document.add(table)
        document.add(Paragraph("\n"))

        // Recomendaciones
        addRecommendations(document, exerciseProgress)
    }

    private fun addRecommendations(
        document: Document,
        exerciseProgress: Map<String, ExerciseStats>
    ) {
        document.add(Paragraph("RECOMENDACIONES", subHeaderFont))

        val recommendations = mutableListOf<String>()

        for ((_, stats) in exerciseProgress) {
            val completionRate = if (stats.totalSessions > 0) {
                (stats.completedSessions.toDouble() / stats.totalSessions) * 100
            } else {
                0.0
            }

            when {
                completionRate >= 90 && stats.currentDifficulty < 5 -> {
                    recommendations.add("• ${stats.name}: Considera aumentar la dificultad en la próxima sesión.")
                }
                completionRate < 50 -> {
                    recommendations.add("• ${stats.name}: Necesita más práctica. Reduce la dificultad si es necesario.")
                }
                completionRate in 50.0..75.0 -> {
                    recommendations.add("• ${stats.name}: Buen progreso. Mantén la consistencia.")
                }
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("• Mantén la consistencia y sigue progresando.")
        }

        for (rec in recommendations) {
            document.add(Paragraph(rec, normalFont))
        }
    }

    private data class ExerciseStats(
        val name: String,
        var totalSessions: Int = 0,
        var completedSessions: Int = 0,
        var totalReps: Int = 0,
        var maxReps: Int = 0,
        var currentDifficulty: Int = 1
    )
}

