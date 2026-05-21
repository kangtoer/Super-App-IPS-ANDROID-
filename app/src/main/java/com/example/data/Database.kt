package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- Generated Materials ---
    @Query("SELECT * FROM generated_materials ORDER BY timestamp DESC")
    fun getAllMaterials(): Flow<List<GeneratedMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: GeneratedMaterial)

    @Query("DELETE FROM generated_materials WHERE id = :id")
    suspend fun deleteMaterialById(id: Int)

    // --- Teaching Journals ---
    @Query("SELECT * FROM teaching_journals ORDER BY timestamp DESC")
    fun getAllJournals(): Flow<List<TeachingJournal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: TeachingJournal)

    @Query("DELETE FROM teaching_journals WHERE id = :id")
    suspend fun deleteJournalById(id: Int)

    // --- Grade Records ---
    @Query("SELECT * FROM grade_records ORDER BY timestamp DESC")
    fun getAllGrades(): Flow<List<GradeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(gradeRecord: GradeRecord)

    @Query("DELETE FROM grade_records WHERE id = :id")
    suspend fun deleteGradeById(id: Int)
}

@Database(entities = [GeneratedMaterial::class, TeachingJournal::class, GradeRecord::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guru_ips_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
