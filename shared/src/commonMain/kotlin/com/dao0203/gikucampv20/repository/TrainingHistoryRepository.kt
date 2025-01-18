package com.dao0203.gikucampv20.repository

import com.dao0203.gikucampv20.database.TrainingHistoryDao
import com.dao0203.gikucampv20.database.entity.TrainingHistoryEntity
import com.dao0203.gikucampv20.database.entity.toTrainingHistory
import com.dao0203.gikucampv20.domain.Training
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface TrainingHistoryRepository {
    val trainingHistory: Flow<List<Training.History>>

    suspend fun addTrainingHistory(training: Training.History)

    suspend fun deleteTrainingHistory(training: Training.History)

    suspend fun updateTrainingHistory(training: Training.History)
}

class TrainingHistoryRepositoryImpl(
    private val trainingHistoryDao: TrainingHistoryDao,
) : TrainingHistoryRepository {
    override val trainingHistory: Flow<List<Training.History>> =
        flow {
            emitAll(trainingHistoryDao.getAllStream().map { it.toTrainingHistory() })
        }

    override suspend fun addTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            val entity = TrainingHistoryEntity.fromTrainingHistory(training)
            trainingHistoryDao.insert(entity)
        }
    }

    override suspend fun deleteTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            val entity = TrainingHistoryEntity.fromTrainingHistory(training)
            trainingHistoryDao.delete(entity)
        }
    }

    override suspend fun updateTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            val entity = TrainingHistoryEntity.fromTrainingHistory(training)
            trainingHistoryDao.update(entity)
        }
    }
}
