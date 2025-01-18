package com.dao0203.gikucampv20.repository

import com.dao0203.gikucampv20.domain.Training
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

interface TrainingHistoryRepository {
    val trainingHistory: Flow<List<Training.History>>

    suspend fun addTrainingHistory(training: Training.History)

    suspend fun deleteTrainingHistory(training: Training.History)

    suspend fun updateTrainingHistory(training: Training.History)
}

class TrainingHistoryRepositoryImpl : TrainingHistoryRepository {
    private val _trainingHistory = MutableStateFlow(mutableListOf<Training.History>())
    override val trainingHistory: Flow<List<Training.History>> = _trainingHistory.asStateFlow()

    override suspend fun addTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            _trainingHistory.value.add(training)
            return@withContext
        }
    }

    override suspend fun deleteTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            _trainingHistory.value.remove(training)
            return@withContext
        }
    }

    override suspend fun updateTrainingHistory(training: Training.History) {
        withContext(Dispatchers.IO) {
            val index = _trainingHistory.value.indexOfFirst { it.id == training.id }
            if (index != -1) {
                _trainingHistory.value[index] = training
            }
            return@withContext
        }
    }
}
