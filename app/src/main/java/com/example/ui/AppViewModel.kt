package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AppTab {
    ASISTEN_AI,
    PORTOFOLIO,
    JURNAL_MENGAJAR,
    BUKU_NILAI,
    REFERENSI_TENTANG
}

data class ChatMessage(
    val sender: String, // "user" or "ai"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db.appDao())

    // --- Tab Selection ---
    var activeTab by mutableStateOf(AppTab.ASISTEN_AI)

    // --- State Flows from Room ---
    val allMaterials: StateFlow<List<GeneratedMaterial>> = repository.allMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allJournals: StateFlow<List<TeachingJournal>> = repository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGrades: StateFlow<List<GradeRecord>> = repository.allGrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- AI Generator Tab State ---
    var isGenerating by mutableStateOf(false)
    var generationCategory by mutableStateOf("Modul Ajar") // "Modul Ajar", "LKPD", "Soal Evaluasi", "Model Pembelajaran"
    var generationClassLevel by mutableStateOf("Kelas 7") // "Kelas 7", "Kelas 8", "Kelas 9"
    var generationTopic by mutableStateOf("")
    var generationExtraNotes by mutableStateOf("")
    var generationResult by mutableStateOf<String?>(null)
    var generationError by mutableStateOf<String?>(null)

    // --- Chat AI Sub-state ---
    var chatMessages by mutableStateOf(
        listOf(
            ChatMessage(
                sender = "ai",
                content = "Halo Rekan Guru! 👋 Saya adalah **Asisten AI Ruang Guru IPS SMP** yang dirancang khusus oleh **Catur Pamungkas, S.Pd.,Gr**.\n\nSaya siap membantu Anda mempersiapkan bahan ajar sejarah, merancang kuis geografi, menjelaskan konsep sosiologi & ekonomi, atau membuat soal HOTS secara instan tanpa batas token.\n\n_Ada yang bisa saya bantu hari ini, Rekan Guru?_"
            )
        )
    )
    var currentChatInput by mutableStateOf("")
    var isChatLoading by mutableStateOf(false)

    // --- New Journal Form State ---
    var isJournalFormVisible by mutableStateOf(false)
    var journalDate by mutableStateOf(SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date()))
    var journalClass by mutableStateOf("Kelas 7-A")
    var journalTopic by mutableStateOf("")
    var journalNotes by mutableStateOf("")
    var journalAbsent by mutableStateOf("")

    // --- New Grade Form State ---
    var isGradeFormVisible by mutableStateOf(false)
    var gradeClass by mutableStateOf("Kelas 7-A")
    var gradeAssessmentName by mutableStateOf("")
    var gradeStudentsList by mutableStateOf<List<StudentScore>>(
        listOf(
            StudentScore("Ahmad Yani", 80),
            StudentScore("Budi Luhur", 85),
            StudentScore("Citra Lestari", 78),
            StudentScore("Dian Saputra", 90),
            StudentScore("Endah Wahyuni", 82)
        )
    )
    var tempStudentName by mutableStateOf("")
    var tempStudentScore by mutableStateOf("")

    // --- Details Dialog State (to view saved content) ---
    var selectedMaterialForView by mutableStateOf<GeneratedMaterial?>(null)

    // Check key validity
    val isApiKeyConfigured: Boolean
        get() = BuildConfig.GEMINI_API_KEY.isNotEmpty() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    // --- Generation Action ---
    fun generateAsset() {
        if (generationTopic.trim().isEmpty()) {
            generationError = "Harap masukkan topik atau tema materi terlebih dahulu."
            return
        }
        isGenerating = true
        generationError = null
        generationResult = null

        viewModelScope.launch {
            val result = repository.generateTeachingAsset(
                category = generationCategory,
                classLevel = generationClassLevel,
                topic = generationTopic,
                extraNotes = generationExtraNotes
            )
            isGenerating = false
            result.onSuccess { text ->
                generationResult = text
            }.onFailure { err ->
                generationError = err.message ?: "Terjadi kesalahan tidak dikenal saat menghubungi Gemini API."
            }
        }
    }

    fun saveGeneratedToPortfolio() {
        val currentResult = generationResult ?: return
        val title = "$generationCategory $generationClassLevel: $generationTopic"
        viewModelScope.launch {
            repository.insertMaterial(
                GeneratedMaterial(
                    title = title,
                    category = generationCategory,
                    classLevel = generationClassLevel,
                    content = currentResult
                )
            )
            // Clear inputs & previews or notify
            generationTopic = ""
            generationExtraNotes = ""
            generationResult = null
            // Switch tab to Portfolio to let them see it was saved!
            activeTab = AppTab.PORTOFOLIO
        }
    }

    // --- Chat Actions ---
    fun sendChatMessage() {
        val input = currentChatInput.trim()
        if (input.isEmpty()) return

        val userMsg = ChatMessage(sender = "user", content = input)
        chatMessages = chatMessages + userMsg
        currentChatInput = ""
        isChatLoading = true

        viewModelScope.launch {
            // Mapping ChatMessage to Gemini API Content format
            val apiHistory = chatMessages.map {
                GeminiContent(
                    parts = listOf(GeminiPart(text = if (it.sender == "user") it.content else {
                        // strip initial formatting of welcome if any, or just pass content
                        it.content
                    }))
                )
            }

            val result = repository.sendMessage(apiHistory)
            isChatLoading = false
            result.onSuccess { text ->
                chatMessages = chatMessages + ChatMessage(sender = "ai", content = text)
            }.onFailure { err ->
                chatMessages = chatMessages + ChatMessage(
                    sender = "ai",
                    content = "⚠️ **Gagal memproses pesan:** ${err.message}\n\nPastikan koneksi internet Anda aktif dan GEMINI_API_KEY sudah dikonfigurasikan dengan benar di Secrets panel."
                )
            }
        }
    }

    fun clearChat() {
        chatMessages = listOf(
            ChatMessage(
                sender = "ai",
                content = "Riwayat percakapan telah dibersihkan. Silakan ajukan pertanyaan baru seputar IPS SMP!"
            )
        )
    }

    // --- Portfolio Actions ---
    fun deletePortfolioItem(id: Int) {
        viewModelScope.launch {
            repository.deleteMaterialById(id)
        }
    }

    // --- Journal Form Actions ---
    fun addJournalEntry() {
        if (journalTopic.trim().isEmpty()) return
        viewModelScope.launch {
            repository.insertJournal(
                TeachingJournal(
                    dateString = journalDate,
                    className = journalClass,
                    topicName = journalTopic,
                    notes = journalNotes,
                    absentStudents = journalAbsent.ifEmpty { "Nihil" }
                )
            )
            // Reset Form fields
            journalTopic = ""
            journalNotes = ""
            journalAbsent = ""
            isJournalFormVisible = false
        }
    }

    fun deleteJournalEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteJournalById(id)
        }
    }

    // --- Score Sheet Form Actions ---
    fun addStudentToTempList() {
        val name = tempStudentName.trim()
        val scoreVal = tempStudentScore.toIntOrNull() ?: 0
        if (name.isNotEmpty()) {
            gradeStudentsList = gradeStudentsList + StudentScore(name, scoreVal)
            tempStudentName = ""
            tempStudentScore = ""
        }
    }

    fun removeStudentFromTempList(index: Int) {
        if (index in gradeStudentsList.indices) {
            gradeStudentsList = gradeStudentsList.filterIndexed { idx, _ -> idx != index }
        }
    }

    fun saveGradeRecord() {
        if (gradeAssessmentName.trim().isEmpty()) return
        val serialized = serializeScores(gradeStudentsList)
        viewModelScope.launch {
            repository.insertGrade(
                GradeRecord(
                    className = gradeClass,
                    assessmentName = gradeAssessmentName,
                    scoresJson = serialized
                )
            )
            // Reset
            gradeAssessmentName = ""
            // Keep a nice default list for next sheets
            gradeStudentsList = listOf(
                StudentScore("Ahmad Yani", 80),
                StudentScore("Budi Luhur", 85),
                StudentScore("Citra Lestari", 78),
                StudentScore("Dian Saputra", 90),
                StudentScore("Endah Wahyuni", 82)
            )
            isGradeFormVisible = false
        }
    }

    fun deleteGradeRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteGradeById(id)
        }
    }

    // --- Score Serializers ---
    fun serializeScores(list: List<StudentScore>): String {
        return list.joinToString(separator = ";") { "${it.studentName},${it.score}" }
    }

    fun deserializeScores(data: String): List<StudentScore> {
        if (data.isEmpty()) return emptyList()
        return data.split(";").mapNotNull {
            val parts = it.split(",")
            if (parts.size == 2) {
                StudentScore(parts[0], parts[1].toIntOrNull() ?: 0)
            } else null
        }
    }
}
