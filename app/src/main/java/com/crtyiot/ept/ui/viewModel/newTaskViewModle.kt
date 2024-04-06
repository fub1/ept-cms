package com.crtyiot.ept.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.MaterialRepository
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.Material
import com.crtyiot.ept.data.model.Task
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
        val selectMatQty = ((materialRepository.getMaterial(matSelect)).map { it.pickQty })
        viewModelScope.launch {
            delay(1)
            var targetCount :Int = selectMatQty.first()
            val task = Task(
                taskId = UUID.randomUUID().toString(),
                staff = _staffid.value,
                vdaMatId = matSelect, // Assuming matSelect can be converted to Int
                createTaskTime = System.currentTimeMillis().toString(),
                targetQty = targetCount,
                isSynced = false
            )
            if (task.staff.isNotEmpty() && task.vdaMatId.isNotEmpty()) {
                taskRepository.insert(task)
            } else {
                Log.e("newTaskViewModel", "registerTask: Invalid task data")
            }

        }
        Log.i("newTask-VM", ":  $matSelect, ${_staffid.value}, $selectMatQty.first()")

    }




}



