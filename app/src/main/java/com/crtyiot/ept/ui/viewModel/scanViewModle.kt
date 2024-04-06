package com.crtyiot.ept.ui.viewModel

import androidx.lifecycle.ViewModel
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.OfflineScanDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class scanViewModel @Inject constructor(
    private val scanDataRepository: OfflineScanDataRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _scanningTaskId = MutableStateFlow("")
    val scanningTaskId: Flow<String> = _scanningTaskId
    val task: Flow<List<Task>> = taskRepository.getAll()
    val scanTaskList: Flow<List<ScanData>> = scanDataRepository.getScanTask(_scanningTaskId.value)


    fun setTaskId(taskId: String) {
        _scanningTaskId.value = taskId
    }
}
