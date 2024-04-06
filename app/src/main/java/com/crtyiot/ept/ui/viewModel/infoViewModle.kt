package com.crtyiot.ept.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.MaterialRepository
import com.crtyiot.ept.data.ScanDataRepository
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.Material
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.crtyiot.ept.network.MaterialApiService
import kotlinx.coroutines.launch


@HiltViewModel
class infoViewModel @Inject constructor(
    materialRepository: MaterialRepository,
    taskRepository: TaskRepository,
    scanDataRepository: ScanDataRepository
) : ViewModel() {

    val mat: Flow<List<Material>> = materialRepository.getAll()
    val task: Flow<List<Task>> = taskRepository.getAll()
    val scanData: Flow<List<ScanData>> = scanDataRepository.getAll()

    init {
        viewModelScope.launch {
            materialRepository.refreshMaterials()
        }
    }



}