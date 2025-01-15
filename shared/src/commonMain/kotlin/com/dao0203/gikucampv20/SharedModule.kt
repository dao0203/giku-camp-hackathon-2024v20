package com.dao0203.gikucampv20

import com.dao0203.gikucampv20.feature.training.MenuDefinitionViewModel
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepositoryImpl
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
