package com.crtyiot.ept.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.MaterialRepository
import com.crtyiot.ept.data.ScanDataRepository
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


@HiltViewModel
class indexViewModel @Inject constructor(

    private val taskRepository: TaskRepository,
) : ViewModel() {

    val taskDash: Flow<List<TaskWithScannedCount>> = taskRepository.taskDashboard()








}



