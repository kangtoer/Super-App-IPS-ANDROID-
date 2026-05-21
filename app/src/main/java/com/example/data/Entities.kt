package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_materials")
data class GeneratedMaterial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Modul Ajar", "LKPD", "Soal Evaluasi", "Model Pembelajaran", "Lainnya"
    val classLevel: String, // "Kelas 7", "Kelas 8", "Kelas 9"
    val content: String, // Raw response content (markdown)
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "teaching_journals")
data class TeachingJournal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String, // e.g. "2026-05-21" or "Kamis, 21 Mei 2026"
    val className: String, // e.g. "Kelas 7-A"
    val topicName: String, // e.g. "Letak Astronomis Indonesia"
    val notes: String, // Teacher reflection or details
    val absentStudents: String, // names or count
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "grade_records")
data class GradeRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val className: String, // e.g. "Kelas 8-B"
    val assessmentName: String, // e.g. "Ulangan Pertengahan Semester - Sosiologi"
    val scoresJson: String, // Serialized List of StudentScore: "[{\"studentName\":\"Ahmad\",\"score\":85},...]"
    val timestamp: Long = System.currentTimeMillis()
)

data class StudentScore(
    val studentName: String,
    val score: Int
)
