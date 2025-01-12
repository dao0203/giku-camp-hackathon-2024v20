package com.dao0203.giku_camp_hackathon_2024v20

import com.dao0203.giku_camp_hackathon_2024v20.feature.training.MenuDefinitionViewModel
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepository
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<OnGoingTrainingMenuRepository> {
        OnGoingTrainingMenuRepositoryImpl(CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate))
    }
}

val viewModelModule = module {
    viewModel<MenuDefinitionViewModel> { MenuDefinitionViewModel() }
}
