package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import java.util.Locale
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.data.GeneratedMaterial
import com.example.data.GradeRecord
import com.example.data.StudentScore
import com.example.data.TeachingJournal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    val materials by viewModel.allMaterials.collectAsStateWithLifecycle()
    val journals by viewModel.allJournals.collectAsStateWithLifecycle()
    val grades by viewModel.allGrades.collectAsStateWithLifecycle()

    var showDocDialog by remember { mutableStateOf<GeneratedMaterial?>(null) }
    var showGradesViewerDialog by remember { mutableStateOf<GradeRecord?>(null) }

    // Floating UI colors for gradient
    val brushGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "GURU IPS SMP SUPERAPP",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Dedikasi: Catur Pamungkas, S.Pd.,Gr.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                        
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "FREE PRO",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "catatanguruips.blogspot.com • Pintar, Gratis & Selamanya",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                windowInsets = WindowInsets.navigationBars,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = viewModel.activeTab == AppTab.ASISTEN_AI,
                    onClick = { viewModel.activeTab = AppTab.ASISTEN_AI },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Asisten AI") },
                    label = { Text("Asisten AI", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_asisten_ai")
                )

                NavigationBarItem(
                    selected = viewModel.activeTab == AppTab.PORTOFOLIO,
                    onClick = { viewModel.activeTab = AppTab.PORTOFOLIO },
                    icon = { Icon(Icons.Default.FolderSpecial, contentDescription = "Portofolio") },
                    label = { Text("Portofolio", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_portofolio")
                )

                NavigationBarItem(
                    selected = viewModel.activeTab == AppTab.JURNAL_MENGAJAR,
                    onClick = { viewModel.activeTab = AppTab.JURNAL_MENGAJAR },
                    icon = { Icon(Icons.Default.EditCalendar, contentDescription = "Jurnal Mengajar") },
                    label = { Text("Jurnal", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_jurnal")
                )

                NavigationBarItem(
                    selected = viewModel.activeTab == AppTab.BUKU_NILAI,
                    onClick = { viewModel.activeTab = AppTab.BUKU_NILAI },
                    icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Buku Nilai") },
                    label = { Text("Nilai IPS", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_nilai")
                )

                NavigationBarItem(
                    selected = viewModel.activeTab == AppTab.REFERENSI_TENTANG,
                    onClick = { viewModel.activeTab = AppTab.REFERENSI_TENTANG },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Tentang App") },
                    label = { Text("Referensi", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_referensi")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (viewModel.activeTab) {
                AppTab.ASISTEN_AI -> {
                    AiAssistantScreen(viewModel, dbClickAction = { item ->
                        showDocDialog = item
                    })
                }
                AppTab.PORTOFOLIO -> {
                    PortfolioScreen(
                        materials = materials,
                        onDelete = { id -> viewModel.deletePortfolioItem(id) },
                        onView = { item -> showDocDialog = item }
                    )
                }
                AppTab.JURNAL_MENGAJAR -> {
                    JournalScreen(
                        viewModel = viewModel,
                        journals = journals
                    )
                }
                AppTab.BUKU_NILAI -> {
                    GradesScreen(
                        viewModel = viewModel,
                        records = grades,
                        onViewRecord = { showGradesViewerDialog = it }
                    )
                }
                AppTab.REFERENSI_TENTANG -> {
                    ReferenceAndAboutScreen(context, clipboardManager)
                }
            }
        }
    }

    // --- DIALOG: Document Viewer (Generated Material detail preview) ---
    showDocDialog?.let { mat ->
        Dialog(
            onDismissRequest = { showDocDialog = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mat.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Kategori: ${mat.category} • ${mat.classLevel}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            )
                        }
                        IconButton(onClick = { showDocDialog = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = mat.content,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.SansSerif,
                                lineHeight = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Footer actions
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setPrimaryClip(ClipData.newPlainText("Bahan IPS", mat.content))
                                    Toast.makeText(context, "Konten berhasil disalin ke papan klip!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Salin")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Salin Semua")
                            }

                            Button(
                                onClick = { showDocDialog = null },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Selesai Membaca")
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIALOG: Grades Viewer (Saved grade list detail table) ---
    showGradesViewerDialog?.let { record ->
        val scoreList = remember(record) { viewModel.deserializeScores(record.scoresJson) }
        val average = remember(scoreList) {
            if (scoreList.isNotEmpty()) scoreList.map { it.score }.average().coerceAtLeast(0.0) else 0.0
        }
        val bestScore = remember(scoreList) {
            if (scoreList.isNotEmpty()) scoreList.maxOfOrNull { it.score } ?: 0 else 0
        }

        Dialog(
            onDismissRequest = { showGradesViewerDialog = null }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = record.assessmentName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Kelas: ${record.className}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { showGradesViewerDialog = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats box
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Rata-rata", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1f", average),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Nilai Tertinggi", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = bestScore.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total Siswa", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = scoreList.size.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Rincian Nilai Siswa",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column {
                            scoreList.forEachIndexed { idx, student ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (idx % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                            else Color.Transparent
                                        )
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${idx + 1}. ",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = student.studentName,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (student.score >= 75) MaterialTheme.colorScheme.primaryContainer
                                            else MaterialTheme.colorScheme.errorContainer
                                        )
                                    ) {
                                        Text(
                                            text = student.score.toString(),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val csv = scoreList.joinToString("\n") { "${it.studentName},${it.score}" }
                                val textToCopy = "Hasil Penilaian: ${record.assessmentName}\nKelas: ${record.className}\n\nNama,Nilai\n$csv"
                                clipboardManager.setPrimaryClip(ClipData.newPlainText("CSV Nilai", textToCopy))
                                Toast.makeText(context, "Daftar nilai berhasil disalin. Siap ditempel di Microsoft Excel / Google Sheets!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Salin Excel")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ekspor Excel")
                        }

                        Button(
                            onClick = { showGradesViewerDialog = null },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tutup")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 1: ASISTEN AI (AI Assistant Generator & Chat)
// ==========================================
@Composable
fun AiAssistantScreen(
    viewModel: AppViewModel,
    dbClickAction: (GeneratedMaterial) -> Unit
) {
    var modeChat by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Mode Selector Tab (Sub-navigation)
        TabRow(selectedTabIndex = if (modeChat) 1 else 0) {
            Tab(
                selected = !modeChat,
                onClick = { modeChat = false },
                text = { Text("AI Pembuat RPP / LKPD", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = modeChat,
                onClick = { modeChat = true },
                text = { Text("Diskusi Bebas (Chat AI)", fontWeight = FontWeight.Bold) }
            )
        }

        if (!modeChat) {
            // Mode A: Dynamic Template Generator
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Warning if API Key is not set up in AI Studio Panel
                ApiKeyMissingAlert(viewModel)

                Text(
                    text = "Pilih Dokumen yang Ingin Dibuat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Selectable type chips
                val categories = listOf("Modul Ajar", "LKPD", "Soal Evaluasi", "Model Pembelajaran")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = viewModel.generationCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.generationCategory = cat },
                            label = { Text(cat, fontSize = 13.sp) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = "Check", modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pilih Tingkat Kelas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Class selections
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val levels = listOf("Kelas 7", "Kelas 8", "Kelas 9")
                    levels.forEach { lvl ->
                        val isSelected = viewModel.generationClassLevel == lvl
                        OutlinedCard(
                            onClick = { viewModel.generationClassLevel = lvl },
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent
                            ),
                            border = BorderStroke(1.5.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = lvl,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Topik / Materi IPS SMP",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = viewModel.generationTopic,
                    onValueChange = { viewModel.generationTopic = it },
                    placeholder = { Text("Contoh: Relief Geografis Jawa, Kerajaan Majapahit, Inflasi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("topic_input_field"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Catatan Tambahan Guru (Opsional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = viewModel.generationExtraNotes,
                    onValueChange = { viewModel.generationExtraNotes = it },
                    placeholder = { Text("Contoh: Gunakan metode PBL, fokus pada isu lingkungan sekitar, dll.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Generate Button
                Button(
                    onClick = { viewModel.generateAsset() },
                    enabled = !viewModel.isGenerating,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("generate_button")
                ) {
                    if (viewModel.isGenerating) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Menganalisis Kurikulum Merdeka...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Sparkle Icon")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Rancang dengan AI Sekarang", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error section
                viewModel.generationError?.let { err ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "Peringatan", tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = err, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }

                // AI PREVIEW AND SAVE PORTFOLIO SECTION
                AnimatedVisibility(
                    visible = viewModel.generationResult != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    viewModel.generationResult?.let { text ->
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Hasil Rancangan AI",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Text(
                                        text = "Berhasil dibuat!",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 280.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = FontFamily.SansSerif,
                                                lineHeight = 20.sp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        val c = LocalContext.current
                                        val clip = remember { c.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
                                        OutlinedButton(
                                            onClick = {
                                                clip.setPrimaryClip(ClipData.newPlainText("Bahan AI IPS", text))
                                                Toast.makeText(c, "Teks berhasil disalin!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Salin")
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.saveGeneratedToPortfolio()
                                                Toast.makeText(c, "Modul berhasil diterbitkan ke Portofolio!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.SaveAlt, contentDescription = "Save Portfolio")
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Simpan Dokumen")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Mode B: Dedicated Interactive Chat Screen for SMP IPS
            Column(modifier = Modifier.fillMaxSize()) {
                // Disclaimer key configuration
                ApiKeyMissingAlert(viewModel)

                // Scrollable Chat Items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    reverseLayout = false,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
                ) {
                    items(viewModel.chatMessages) { msg ->
                        val isUser = msg.sender == "user"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(vertical = 2.dp),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 2.dp,
                                    bottomEnd = if (isUser) 2.dp else 16.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUser) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = msg.content,
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                        color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (viewModel.isChatLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Menghimpun materi penunjang SMP...",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Chat Input Console
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.clearChat() },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Hapus Riwayat")
                        }

                        OutlinedTextField(
                            value = viewModel.currentChatInput,
                            onValueChange = { viewModel.currentChatInput = it },
                            placeholder = { Text("Tanyakan konsep, evaluasi & metode IPS...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            maxLines = 2,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = { viewModel.sendChatMessage() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )

                        IconButton(
                            onClick = { viewModel.sendChatMessage() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("send_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Kirim", modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// Helper block to show API key missing warnings
@Composable
fun ApiKeyMissingAlert(viewModel: AppViewModel) {
    if (!viewModel.isApiKeyConfigured) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Key,
                    contentDescription = "No API Key",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mode Simulasi Aktif / Guru Lain Sekali Klik",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Aplikasi terintegrasi dengan Google AI Studio. Di workspace AI Studio Anda, asisten ini akan bekerja otomatis tanpa batas token. Jika diclone guru lain, sistem secara otomatis memasang kunci mereka.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

// ==========================================
// SCREEN 2: PORTOFOLIO SAYA (Portfolio Management)
// ==========================================
@Composable
fun PortfolioScreen(
    materials: List<GeneratedMaterial>,
    onDelete: (Int) -> Unit,
    onView: (GeneratedMaterial) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Semua") }

    val filteredList = remember(materials, selectedFilter) {
        if (selectedFilter == "Semua") materials
        else materials.filter { it.category == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Portofolio Dokumen IPS Saya",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Koleksi Modul Ajar, LKPD, RPP, dan Soal yang telah disimpan.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal filter chips
        val filters = listOf("Semua", "Modul Ajar", "LKPD", "Soal Evaluasi", "Model Pembelajaran")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = "Kosong",
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Dokumen Belum Tersedia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Silakan rancang modul, LKPD atau soal kuis di tab 'Asisten AI' terlebih dahulu, lalu klik Simpan.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { mat ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SuggestionChip(
                                        onClick = { },
                                        label = { Text(mat.category, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                            labelColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    
                                    SuggestionChip(
                                        onClick = { },
                                        label = { Text(mat.classLevel, fontSize = 11.sp) }
                                    )
                                }

                                IconButton(
                                    onClick = { onDelete(mat.id) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = mat.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = mat.content,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { onView(mat) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = "Buka", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Buka Pembaca Dokumen")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 3: JURNAL MENGAJAR (Teaching History Register)
// ==========================================
@Composable
fun JournalScreen(
    viewModel: AppViewModel,
    journals: List<TeachingJournal>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Jurnal Harian Mengajar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Catat agenda pembelajaran materi kelas & absensi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Button(
                onClick = { viewModel.isJournalFormVisible = !viewModel.isJournalFormVisible },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.isJournalFormVisible) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                if (viewModel.isJournalFormVisible) {
                    Text("Tutup Form")
                } else {
                    Icon(Icons.Default.Add, contentDescription = "Add Journal")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buat Jurnal")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AnimatedVisibility(
            visible = viewModel.isJournalFormVisible,
            enter = slideInVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Masukan Agenda Mengajar",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Class selector
                        var expandedClassDropdown by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = viewModel.journalClass,
                                onValueChange = {},
                                label = { Text("Kelas") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { expandedClassDropdown = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = expandedClassDropdown,
                                onDismissRequest = { expandedClassDropdown = false }
                            ) {
                                listOf("Kelas 7-A", "Kelas 7-B", "Kelas 8-A", "Kelas 8-B", "Kelas 9-A", "Kelas 9-B").forEach { cls ->
                                    DropdownMenuItem(
                                        text = { Text(cls) },
                                        onClick = {
                                            viewModel.journalClass = cls
                                            expandedClassDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Date Picker text field (simulated fast)
                        OutlinedTextField(
                            value = viewModel.journalDate,
                            onValueChange = { viewModel.journalDate = it },
                            label = { Text("Tanggal") },
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.journalTopic,
                        onValueChange = { viewModel.journalTopic = it },
                        label = { Text("Materi Pembahasan IPS") },
                        placeholder = { Text("e.g. Kedatangan Bangsa Barat, Dinamika Perekonomian") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.journalNotes,
                        onValueChange = { viewModel.journalNotes = it },
                        label = { Text("Refleksi / Catatan Guru") },
                        placeholder = { Text("e.g. Siswa senang menggambar komik sejarah, 2 anak butuh bimbingan") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.journalAbsent,
                        onValueChange = { viewModel.journalAbsent = it },
                        label = { Text("Keterangan Absensi Siswa") },
                        placeholder = { Text("e.g. Andi (Sakit), Bayu (Izin)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.addJournalEntry() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan Agenda Harian", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (journals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(
                        Icons.Outlined.EditCalendar,
                        contentDescription = "Empty",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Jurnal Mengajar Kosong",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Klik '+ Buat Jurnal' di sudut kanan atas untuk mencatat riwayat mengajar Anda hari ini.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(journals) { jrnl ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.School,
                                        contentDescription = "Class",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = jrnl.className,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(
                                            text = jrnl.dateString,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteJournalEntry(jrnl.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Materi Pokok: ${jrnl.topicName}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Refleksi Guru: ${jrnl.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.PeopleOutline,
                                    contentDescription = "Absen",
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Siswa Berhalangan: ${jrnl.absentStudents}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 4: BUKU NILAI (Grade Register/Evaluations)
// ==========================================
@Composable
fun GradesScreen(
    viewModel: AppViewModel,
    records: List<GradeRecord>,
    onViewRecord: (GradeRecord) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Buku Nilai IPS Siswa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Rekam hasil kuis, ujian mandiri, atau tugas peta.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { viewModel.isGradeFormVisible = !viewModel.isGradeFormVisible },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.isGradeFormVisible) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                if (viewModel.isGradeFormVisible) {
                    Text("Tutup")
                } else {
                    Icon(Icons.Default.Add, contentDescription = "Add grade")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Input Nilai")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AnimatedVisibility(
            visible = viewModel.isGradeFormVisible,
            enter = slideInVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Rapor Hasil Evaluasi",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        var expandedClassDropdown by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = viewModel.gradeClass,
                                onValueChange = {},
                                label = { Text("Kelas") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { expandedClassDropdown = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = expandedClassDropdown,
                                onDismissRequest = { expandedClassDropdown = false }
                            ) {
                                listOf("Kelas 7-A", "Kelas 7-B", "Kelas 8-A", "Kelas 8-B", "Kelas 9-A", "Kelas 9-B").forEach { cls ->
                                    DropdownMenuItem(
                                        text = { Text(cls) },
                                        onClick = {
                                            viewModel.gradeClass = cls
                                            expandedClassDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = viewModel.gradeAssessmentName,
                            onValueChange = { viewModel.gradeAssessmentName = it },
                            label = { Text("Nama Penilaian") },
                            placeholder = { Text("e.g. Ulangan Bab 1") },
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Student list in Form
                    Text(
                        text = "Daftar Skor Siswa (Klik nama siswa untuk edit/hapus)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Scrollable list of students in form
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                            itemsIndexed(viewModel.gradeStudentsList) { idx, stu ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${idx+1}. ${stu.studentName} - Nilai: ${stu.score}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeStudentFromTempList(idx) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.RemoveCircleOutline,
                                            contentDescription = "Hapus",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Inline student adder
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = viewModel.tempStudentName,
                            onValueChange = { viewModel.tempStudentName = it },
                            placeholder = { Text("Nama Siswa Baru") },
                            modifier = Modifier.weight(2f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = viewModel.tempStudentScore,
                            onValueChange = { viewModel.tempStudentScore = it },
                            placeholder = { Text("Nilai") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        IconButton(
                            onClick = { viewModel.addStudentToTempList() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = "Add student inline")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.saveGradeRecord() },
                        enabled = viewModel.gradeStudentsList.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan Lembar Nilai", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(
                        Icons.Default.Leaderboard,
                        contentDescription = "Empty",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Lembar Nilai Belum Tersedia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Catat dan arsipkan hasil belajar siswa Anda secara digital dengan mengeklik 'Input Nilai'.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(records) { record ->
                    val list = viewModel.deserializeScores(record.scoresJson)
                    val avgVal = if (list.isNotEmpty()) list.map { it.score }.average() else 0.0
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = record.assessmentName,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = record.className,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Avg: ${String.format(Locale.getDefault(), "%.1f", avgVal)} • total: ${list.size} siswa",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { onViewRecord(record) },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Buka", fontSize = 12.sp)
                                }

                                IconButton(
                                    onClick = { viewModel.deleteGradeRecord(record.id) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 5: REFERENSI MATERI & ATRIBUSI
// ==========================================
@Composable
fun ReferenceAndAboutScreen(
    context: Context,
    clipboardManager: ClipboardManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Atribusi yang diminta khusus oleh user di barisan paling atas
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Stars,
                    contentDescription = "Special Attribution",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Aplikasi ini Direkomendasikan & Dibuat Oleh:",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Catur Pamungkas, S.Pd.,Gr.",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "Kreator Jendela Studi IPS Berkelanjutan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("Blog Catur Pamungkas", "http://catatanguruips.blogspot.com"))
                        Toast.makeText(context, "Situs catatanguruips.blogspot.com disalin ke clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillModifierOutlined()
                ) {
                    Icon(Icons.Default.Language, contentDescription = "Website Visit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salin Situs: catatanguruips.blogspot.com")
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = "Semua materi yang digenerasikan AI dapat dibagikan gratis untuk sesama rekan guru tanpa batasan royalti.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = "Kamus Kilat & Referensi IPS SMP (Offline)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Asisten penunjang cepat guru untuk mengingat indikator pembelajaran di kelas.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Categories of social science quick reference
        ReferenceSectionItem(
            title = "1. GEOGRAFI: Ruang & Lingkungan",
            content = "• Letak Astronomis: Indonesia terletak di 6° LU - 11° LS dan 95° BT - 141° BT. Berdampak pada iklim tropis basah.\n\n" +
                    "• Letak Geografis: Terhimpit di antara 2 Benua (Asia & Australia) dan 2 Samudra (Hindia & Pasifik) yang memicu posisi perdagangan silang nan vital.\n\n" +
                    "• Potensi Maritim: Meliputi perikanan tangkap, energi kelautan terbarukan, pertambangan migas, dan wisata bahari dunia pelayaran Nusantara."
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReferenceSectionItem(
            title = "2. SEJARAH: Perkembangan Masyarakat",
            content = "• Masa Praaksara: Periodisasi masa berburu-meramu, bercocok tanam hingga zaman perundagian (pengecoran logam).\n\n" +
                    "• Teori Hindu-Buddha: Asal-usul kebudayaan India masuk lewat teori Waisya (pedagang), Ksatria (bangsawan), Brahmana (pendeta), atau Arus Balik (pelajar).\n\n" +
                    "• Masa Kolonial: Struktur monopoli rempah-rempah oleh VOC, sistem Kerja Paksa Daendels, Cultuurstelsel (Tanam Paksa van den Bosch), hingga lahirnya kebijakan politik etis."
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReferenceSectionItem(
            title = "3. SOSIOLOGI: Interaksi & Lembaga Sosial",
            content = "• Syarat Interaksi: Adanya Kontak Sosial (fisik maupun sekunder digital) dan Komunikasi yang melahirkan pemahaman makna pesan.\n\n" +
                    "• Bentuk Interaksi:\n" +
                    "  1. Asosiatif: Kerja sama, Akomodasi, Asimilasi, Akulturasi.\n" +
                    "  2. Disosiatif: Persaingan (kompetisi), Kontravensi, Pertikaian (konflik).\n\n" +
                    "• Lembaga Sosial: Lembaga keluarga, pendidikan, agama, ekonomi, hukum, dan politik yang menjaga kestabilan norma masyarakat."
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReferenceSectionItem(
            title = "4. EKONOMI: Kebutuhan & Kelangkaan",
            content = "• Kelangkaan (Scarcity): Kesenjangan antara kebutuhan manusia yang beraneka ragam dan tidak terbatas dengan alat pemuas kebutuhan yang jumlahnya terbatas.\n\n" +
                    "• Tindakan Motif & Prinsip Ekonomi: Usaha memperoleh hasil maksimal dengan pengorbanan tertentu atau meminimalkan pengorbanan.\n\n" +
                    "• Pasar & Saluran Distribusi: Agen, grosir, pengecer yang menyalurkan barang bernilai guna tinggi secara efisien ke tangan konsumen akhir."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ReferenceSectionItem(title: String, content: String) {
    var isExpanded by remember { mutableStateOf(false) }
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { isExpanded = !isExpanded },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Inline extension mapping
private fun Modifier.fillModifierOutlined(): Modifier = this.fillMaxWidth()
