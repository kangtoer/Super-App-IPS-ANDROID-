package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    val allMaterials: Flow<List<GeneratedMaterial>> = appDao.getAllMaterials()
    val allJournals: Flow<List<TeachingJournal>> = appDao.getAllJournals()
    val allGrades: Flow<List<GradeRecord>> = appDao.getAllGrades()

    // --- Material Room Operations ---
    suspend fun insertMaterial(material: GeneratedMaterial) = withContext(Dispatchers.IO) {
        appDao.insertMaterial(material)
    }

    suspend fun deleteMaterialById(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteMaterialById(id)
    }

    // --- Journal Room Operations ---
    suspend fun insertJournal(journal: TeachingJournal) = withContext(Dispatchers.IO) {
        appDao.insertJournal(journal)
    }

    suspend fun deleteJournalById(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteJournalById(id)
    }

    // --- Grade Room Operations ---
    suspend fun insertGrade(gradeRecord: GradeRecord) = withContext(Dispatchers.IO) {
        appDao.insertGrade(gradeRecord)
    }

    suspend fun deleteGradeById(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteGradeById(id)
    }

    // --- Gemini Generation ---
    suspend fun generateTeachingAsset(
        category: String,
        classLevel: String,
        topic: String,
        extraNotes: String = ""
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Kunci API Gemini (GEMINI_API_KEY) belum terpasang di AI Studio Secrets. Silakan tambahkan kunci API Anda di menu Secrets tab sidebar AI Studio."))
        }

        val prompt = when (category) {
            "Modul Ajar" -> {
                "Buatkan RPP / Modul Ajar Kurikulum Merdeka yang lengkap untuk mata pelajaran IPS $classLevel SMP. " +
                        "Topik materi: $topic. Durasi pembelajaran: 2 JP (2x40 menit). " +
                        "Catatan tambahan dari guru: $extraNotes. " +
                        "Modul ajar wajib mencakup: (1) Tujuan Pembelajaran, (2) Pertanyaan Pemantik, " +
                        "(3) Langkah Kegiatan Pembelajaran yang inovatif (Pendahuluan, Inti dengan PBL/PjBL atau Cooperative Learning, Penutup), " +
                        "(4) Asesmen Pembelajaran (Formatasif/Sumatif), dan (5) Refleksi Guru & Siswa. " +
                        "Gunakan bahasa Indonesia yang formal, inspiratif, dan sajikan dalam format Markdown yang indah dan terstruktur secara rapi."
            }
            "LKPD" -> {
                "Buatkan LKPD (Lembar Kerja Peserta Didik) yang kreatif, interaktif, dan menantang untuk siswa SMP $classLevel pada materi IPS: $topic. " +
                        "Catatan tambahan dari guru: $extraNotes. " +
                        "LKPD harus memuat: (1) Petunjuk Pengerjaan, (2) Pertanyaan HOTS (Higher Order Thinking Skills) minimal 5 nomor esai kritis/peta konsep/observasi sosial, " +
                        "(3) Lembar Aktivitas Diskusi Kelompok atau Penyelidikan Mandiri, serta (4) Rubrik Penilaian untuk guru. " +
                        "Gunakan bahasa Indonesia yang sesuai usia siswa SMP dan sajikan dalam format Markdown terstruktur yang rapi."
            }
            "Soal Evaluasi" -> {
                "Buatkan Naskah Soal Evaluasi dan Kisi-kisi Kuis materi IPS SMP $classLevel dengan topik: $topic. " +
                        "Catatan tambahan dari guru: $extraNotes. " +
                        "Buatkan 10 soal, terdiri atas: 7 butir soal Pilihan Ganda (opsi A, B, C, D) berkarakter HOTS, dan 3 butir soal Esai eksploratif. " +
                        "Sertakan Kunci Jawaban Lengkap beserta Rubrik atau Pembahasan di bagian bawah sebagai panduan guru. " +
                        "Tulis dalam bahasa Indonesia yang baku dan sajikan dengan format Markdown yang rapi."
            }
            "Model Pembelajaran" -> {
                "Berikan inspirasi rancangan Model Pembelajaran Aktif & Kreatif untuk materi IPS SMP $classLevel dengan tema: $topic. " +
                        "Catatan tambahan dari guru: $extraNotes. " +
                        "Rekomendasikan minimal 2 model pembelajaran menyenangkan (contoh: Role Playing, Jigsaw, Debat Isu Kontemporer, Mind Mapping Kolektif, atau Market Place of Activity). " +
                        "Jelaskan langkah demi langkah penerapannya di kelas IPS, peralatan yang dibutuhkan, serta tips manajemen kelas agar pembelajaran berjalan kondusif. " +
                        "Sajikan dalam format Markdown yang rapi."
            }
            else -> {
                "Jawab pertanyaan guru IPS berikut dengan kapasitasmu sebagai Asisten Ahli Guru IPS SMP Kurikulum Merdeka & K13: " +
                        "Materi/Pertanyaan: $topic. Catatan khusus: $extraNotes. " +
                        "Sajikan penjelasanmu dalam format Markdown yang rapi, lengkap dengan poin-poin yang mudah dipahami."
            }
        }

        try {
            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                systemInstruction = GeminiContent(parts = listOf(GeminiPart(
                    text = "Anda adalah AI Super-Asisten Guru IPS SMP di Indonesia yang sangat ahli " +
                            "dalam Geografi, Sejarah, Sosiologi, Ekonomi, serta Pedagogi Kurikulum Merdeka. " +
                            "Anda mengabdi membantu guru secara gratis, ramah, dan solutif. Semua output menggunakan " +
                            "Bahasa Indonesia yang sangat baik, santun, terperinci, dan ramah guru. " +
                            "Selalu sertakan atribusi di dasar konten: \"Digenerasikan oleh Guru IPS SuperApp - Dedikasi Catur Pamungkas, S.Pd.,Gr.\""
                )))
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val generatedText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (generatedText != null) {
                Result.success(generatedText)
            } else {
                Result.failure(Exception("Maaf, API mengembalikan respons kosong. Silakan coba sesaat lagi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Free Chat Interface ---
    suspend fun sendMessage(chatHistory: List<GeminiContent>): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Kunci API Gemini (GEMINI_API_KEY) belum terpasang. Konfigurasikan di tab Secrets AI Studio."))
        }

        try {
            val request = GeminiRequest(
                contents = chatHistory,
                systemInstruction = GeminiContent(parts = listOf(GeminiPart(
                    text = "Anda adalah AI Guru IPS SMP virtual yang cerdas, ramah, berbasis di Indonesia. " +
                            "Misi Anda membantu rekan guru IPS memecahkan masalah pembelajaran, materi sulit, " +
                            "konsep Geografi, Sejarah, Sosiologi, dan Ekonomi. Bicara dengan hangat menggunakan kata " +
                            "panggilan 'Rekan Guru' atau 'Pak/Ibu'. Tetap fokus pada topik IPS tingkat SMP."
                )))
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (responseText != null) {
                Result.success(responseText)
            } else {
                Result.failure(Exception("Respons kosong dari asisten."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
