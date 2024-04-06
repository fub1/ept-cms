package com.crtyiot.ept.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.OfflineScanDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject

@HiltViewModel
class scanViewModel @Inject constructor(
    private val scanDataRepository: OfflineScanDataRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _scanningTaskId = MutableStateFlow("")
    val scanningTaskId: Flow<String> = _scanningTaskId
    val task: Flow<List<Task>> = taskRepository.getAll()
    val scanTaskList: Flow<List<ScanData>> = _scanningTaskId.flatMapLatest { taskId ->
        scanDataRepository.getScanTask(taskId)
    }

    fun setTaskId(taskId: String) {
        _scanningTaskId.value = taskId
    }

    fun addScanRecord() {
        val scanDatax = ScanData(
            taskId = "bc0cd199-a59a-4219-ae41-c5ea9575d832",
            cmsMatCode = "xxx",
            vdaMatCode = "T566426",
            scanTime = "1712407292225",
            vdaSerialCode = Random.nextInt(122999,1000000).toString(),
            isDeleted = false
        )
        viewModelScope.launch {
            scanDataRepository.insert(scanDatax)
        }
    }
}