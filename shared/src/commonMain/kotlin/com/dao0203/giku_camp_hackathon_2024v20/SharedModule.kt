package com.dao0203.giku_camp_hackathon_2024v20

import com.dao0203.giku_camp_hackathon_2024v20.feature.training.DefinitionMenuViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun sharedModule() = module {
    viewModel { DefinitionMenuViewModel() }
}
