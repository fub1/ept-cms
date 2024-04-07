package com.crtyiot.ept.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.MaterialRepository
import com.crtyiot.ept.data.OfflineScanDataRepository
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.Material
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.model.TaskWithScannedCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.util.UUID
import android.os.Environment
import kotlinx.coroutines.flow.toList
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class indexViewModel @Inject constructor(
    private val offlineScanDataRepository: OfflineScanDataRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val taskDash: Flow<List<TaskWithScannedCount>> = taskRepository.taskDashboard()
    fun exportScanData(context: Context) {
        viewModelScope.launch {
            try {
                offlineScanDataRepository.exportScanDataToCsv(context)
                // 导出成功
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

}