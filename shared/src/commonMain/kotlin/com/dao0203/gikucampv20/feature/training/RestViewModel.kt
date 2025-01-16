package com.dao0203.gikucampv20.feature.training

import androidx.lifecycle.ViewModel
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RestViewModel :
    ViewModel(),
    KoinComponent {
    val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()
}
