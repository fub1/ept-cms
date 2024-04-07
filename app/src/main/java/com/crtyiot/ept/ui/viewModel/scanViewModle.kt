package com.crtyiot.ept.ui.viewModel

import androidx.compose.animation.defaultDecayAnimationSpec
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crtyiot.ept.data.TaskRepository
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.OfflineScanDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random
import javax.inject.Inject
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.crtyiot.ept.MainApplication
import com.crtyiot.ept.data.model.Material
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first


@HiltViewModel
class scanViewModel @Inject constructor(
    private val scanDataRepository: OfflineScanDataRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {


    private val _scanningTaskId = MutableStateFlow("")
    private val _cmsMat = MutableStateFlow("")
    val cmsMat: StateFlow<String> = _cmsMat.asStateFlow()
    private val _vdaMat = MutableStateFlow("")
    val vdaMat: StateFlow<String> = _vdaMat.asStateFlow()

    private val _taskCmsMat = MutableStateFlow("")
    val taskCmsMat: StateFlow<String> = _taskCmsMat.asStateFlow()
    private val _taskVdaMat = MutableStateFlow("")
    val taskVdaMat: StateFlow<String> = _taskVdaMat.asStateFlow()
    private val _taskTagQTY = MutableStateFlow(0)
    val taskTagQTY: StateFlow<Int> = _taskTagQTY.asStateFlow()
    // 整个扫码任务过程只扫一次cms码
    private val _iscmsCodeScaned = MutableStateFlow(false)

    val scanTaskList: Flow<List<ScanData>> = _scanningTaskId.flatMapLatest { taskId ->
        scanDataRepository.getScanTaskNotDeleted(taskId)
    }

    val scanTaskListCount: Flow<Int> = scanTaskList.map { it.size }


    // 扫描步骤索引
    private val _scanstepindex = MutableStateFlow(0)
    val scanstepindex : Flow<Int> = _scanstepindex
    // 错误提示
    private val _errorMsg = MutableStateFlow("")
    val errorMsg : Flow<String> = _errorMsg
    // 错误1-cms物料号错误
    private val _cmsCodeError = MutableStateFlow(false)
    val cmsCodeError : Flow<Boolean> = _cmsCodeError
    // 错误2-vda物料号错误
    private val _vdaCodeError = MutableStateFlow(false)
    val vdaCodeError : Flow<Boolean> = _vdaCodeError
    // 错误3-序列号错误
    private val _serialCodeError = MutableStateFlow(false)
    val serialCodeError : Flow<Boolean> = _serialCodeError
    // 序列号
    private val _vdaSerialCode = MutableStateFlow("")
    val vdaSerialCode : Flow<String> = _vdaSerialCode



    init {
        //
        viewModelScope.launch {
            delay(1000)
            resetScanData()
            loadMatCodes()
            loadVdaCodes()
            loadQTY()

        }
    }


    fun loadMatCodes() {
        viewModelScope.launch {
            _scanningTaskId.value.let { taskId ->
                taskRepository.getCmsMatCodeByTaskId(taskId).collect { cmsCode ->
                    _taskCmsMat.value = cmsCode
                    Log.i("scan 111", "loadMatCodes: $cmsCode")
                    Log.i("scan 112", "loadMatCodes: ${_taskCmsMat.value}")
                }
            }
        }
    }

    fun loadVdaCodes() {
        viewModelScope.launch {
            _scanningTaskId.value.let { taskId ->
                taskRepository.getVdaMatIdByTaskId(taskId).collect { vdaCode ->
                    _taskVdaMat.value = vdaCode
                    Log.i("scan 222", "loadMatCodes: $vdaCode")
                    Log.i("scan 223", "loadMatCodes: ${_taskVdaMat.value}")
                }
            }
        }
    }

    fun loadQTY() {
        viewModelScope.launch {
            _scanningTaskId.value.let { taskId ->
                taskRepository.getTagQTYByTaskId(taskId).collect { qty ->
                    _taskTagQTY.value = qty
                    Log.i("scan 225", "loadMatCodes: $qty")
                    Log.i("scan 226", "loadMatCodes: ${_taskTagQTY.value}")
                }
            }
        }
    }

    fun getBCScanData(data: String) {
        when (_scanstepindex.value) {
            0 -> {
                val cmsMatValue = _taskCmsMat.value
                if (data.trim() != cmsMatValue.trim()) {
                    Log.i("scan", "getBCScanData: $data")
                    Log.i("scan", "getBCScanData: $cmsMatValue")
                    _cmsCodeError.value = true
                    _cmsMat.value = data.trim()
                    _errorMsg.value = "cms物料号错误"
                } else {
                    _cmsMat.value = data.trim()
                    _scanstepindex.value = 1
                    _iscmsCodeScaned.value = true
                }
            }
            1 -> {
                val vdaMatValue = _taskVdaMat.value
                if (data.substring(1).trim() != vdaMatValue.trim()) {
                    _vdaCodeError.value = true
                    _vdaMat.value = data.substring(1).trim()
                    _errorMsg.value = "vda物料号错误"
                } else {
                    _vdaMat.value = data.substring(1).trim()
                    _scanstepindex.value = 2
                }
            }
            2 -> {
                val v_data = data.substring(1).trim()
                if (!v_data.matches("\\d{5,6}".toRegex())) {
                    _serialCodeError.value = true
                    _vdaSerialCode.value = data.substring(1).trim()
                    _errorMsg.value = "序列号错误"
                } else {
                    _vdaSerialCode.value = data.substring(1).trim()
                    _scanstepindex.value = 3
                }
            }
        }
    }
    fun setTaskId(taskId: String) {
        _scanningTaskId.value = taskId
    }

    fun submitScanData() {
        val scanDataSubmit = ScanData(
            taskId = _scanningTaskId.value,
            cmsMatCode = _cmsMat.value,
            vdaMatCode = _vdaMat.value,
            scanTime = System.currentTimeMillis().toString(),
            vdaSerialCode = _vdaSerialCode.value,
            isDeleted = false
        )
        viewModelScope.launch {
            scanDataRepository.insert(scanDataSubmit)
            resetScanData()
        }
    }



    fun resetScanData() {
        _errorMsg.value = ""
        _cmsCodeError.value = false
        _vdaCodeError.value = false
        _serialCodeError.value = false
        _vdaMat.value = ""
        _vdaSerialCode.value = ""
        if (_iscmsCodeScaned.value) {
            _scanstepindex.value = 1
        } else {
            _scanstepindex.value = 0
            _cmsMat.value = ""
        }

    }

    fun backScanData() {
        if (_scanstepindex.value == 0) {
            _scanstepindex.value = 0
            _errorMsg.value = ""
            _cmsCodeError.value = false
            _cmsMat.value = ""
        } else if (_scanstepindex.value == 1) {
            _scanstepindex.value = 1
            _errorMsg.value = ""
            _vdaCodeError.value = false
            _vdaMat.value = ""
        } else if (_scanstepindex.value == 2) {
            _scanstepindex.value = 2
            _errorMsg.value = ""
            _serialCodeError.value = false
            _vdaSerialCode.value = ""
        } else if (_scanstepindex.value == 3) {
            _scanstepindex.value = 2
            _errorMsg.value = ""
            _serialCodeError.value = false
            _vdaSerialCode.value = ""
        }
    }
}


