package com.crtyiot.ept.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.MaterialRepository
import com.crtyiot.ept.data.ScanDataRepository
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.Material
import com.crtyiot.ept.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.util.UUID


@HiltViewModel
class newTaskViewModel @Inject constructor(
    private val materialRepository: MaterialRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val mat: Flow<List<Material>> = materialRepository.getAll()

    val _staffid = MutableStateFlow("")
    val staffid: Flow<String> = _staffid


    init {
        viewModelScope.launch {
            materialRepository.refreshMaterials()
        }
    }

    fun setStaffid(staffid: String) {
        _staffid.value = staffid
    }

    fun registerTask(matSelect: String) {

        viewModelScope.launch {
            delay(1)
            val task = Task(
                taskId = UUID.randomUUID().toString(),
                staff = _staffid.value,
                vdaMatId = matSelect, // Assuming matSelect can be converted to Int
                createTaskTime = System.currentTimeMillis().toString(),
                isSynced = false
            )
            if (task.staff.isNotEmpty() && task.vdaMatId.isNotEmpty()) {
                taskRepository.insert(task)
            } else {
                Log.e("newTaskViewModel", "registerTask: Invalid task data")
            }

        }
        Log.i("newTaskViewModel", "registerTask:  $matSelect, ${_staffid.value}")

    }




}



