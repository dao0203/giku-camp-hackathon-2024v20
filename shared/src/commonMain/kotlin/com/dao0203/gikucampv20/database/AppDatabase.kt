package com.dao0203.gikucampv20.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.dao0203.gikucampv20.database.entity.MuscleGroupEntityConverter
import com.dao0203.gikucampv20.database.entity.MuscleGroupEntityListConverter
import com.dao0203.gikucampv20.database.entity.PoseLandmarkEntityConverter
import com.dao0203.gikucampv20.database.entity.TrainingHistoryEntity
import com.dao0203.gikucampv20.database.entity.TrainingTypeEntityListConverter

@Database(
    entities = [TrainingHistoryEntity::class],
    version = 1,
)
@TypeConverters(
    MuscleGroupEntityConverter::class,
    PoseLandmarkEntityConverter::class,
    MuscleGroupEntityListConverter::class,
    TrainingTypeEntityListConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainingHistoryDao(): TrainingHistoryDao
}

@Dao
interface TrainingHistoryDao {
    @Insert
    suspend fun insert(entity: TrainingHistoryEntity)

    @Query("SELECT * FROM TrainingHistoryEntity")
    suspend fun getAll(): List<TrainingHistoryEntity>

    @Delete
    suspend fun delete(entity: TrainingHistoryEntity)

    @Update
    suspend fun update(entity: TrainingHistoryEntity)
}
