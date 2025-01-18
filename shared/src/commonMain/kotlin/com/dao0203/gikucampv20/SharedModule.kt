package com.dao0203.gikucampv20

import com.dao0203.gikucampv20.feature.record.HistoryWithCalenderViewModel
import com.dao0203.gikucampv20.feature.training.MenuDefinitionViewModel
import com.dao0203.gikucampv20.feature.training.TrainingRestViewModel
import com.dao0203.gikucampv20.feature.training.TrainingResultViewModel
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepositoryImpl
import com.dao0203.gikucampv20.repository.TrainingHistoryRepository
import com.dao0203.gikucampv20.repository.TrainingHistoryRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule =
    module {
        single<TrainingHistoryRepository> { TrainingHistoryRepositoryImpl(get()) }
        single<OnGoingTrainingMenuRepository> {
            OnGoingTrainingMenuRepositoryImpl(get())
        }
    }

val viewModelModule =
    module {
        viewModel<MenuDefinitionViewModel> { MenuDefinitionViewModel() }
        viewModel<TrainingRestViewModel> { TrainingRestViewModel() }
        viewModel<TrainingResultViewModel> { TrainingResultViewModel() }
        viewModel<HistoryWithCalenderViewModel> { HistoryWithCalenderViewModel() }
    }

expect val databaseModule: Module
