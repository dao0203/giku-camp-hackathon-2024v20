package com.dao0203.giku_camp_hackathon_2024v20

import com.dao0203.giku_camp_hackathon_2024v20.feature.training.MenuDefinitionViewModel
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepository
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<OnGoingTrainingMenuRepository> {
        OnGoingTrainingMenuRepositoryImpl()
    }
}

val viewModelModule = module {
    viewModel<MenuDefinitionViewModel> { MenuDefinitionViewModel() }
}
