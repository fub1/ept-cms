package com.crtyiot.ept.data

import android.content.Context
import com.crtyiot.ept.data.Dao.ScanDataDao
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.repository.ScanDataRespository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineScanDataRepository @Inject constructor (
    private val scanDataDao: ScanDataDao
) : ScanDataRespository {

    override fun getAll(): Flow<List<ScanData>> = scanDataDao.getAllScanData()

    fun getScanTaskNotDeleted(taskId: String): Flow<List<ScanData>> = scanDataDao.getScanTaskNotDeleted(taskId)

    override fun getScanTask(taskId: String): Flow<List<ScanData>> = scanDataDao.getScanTask(taskId)

    override fun getScanDataByVdaSerialCode(vdaSerialCode: String):
            Flow<String> = scanDataDao.getScanDataByVdaSerialCode(vdaSerialCode)
    override suspend fun insert(scanData: ScanData) = scanDataDao.insert(scanData)

    override suspend fun delete(scanData: ScanData) = scanDataDao.delete(scanData)

    override suspend fun update(scanData: ScanData) = scanDataDao.update(scanData)

    suspend fun exportScanDataToCsv(context: Context) {
        val allScanData = getAll().first() // 使用 first() 等待 Flow 收集完成并获取结果
        val csvHeader = "taskId,cmsMatCode,vdaMatCode,scanTime,vdaSerialCode,isDeleted\n"
        val csvContent = allScanData.joinToString("\n") { scanData ->
            "${scanData.taskId},${scanData.cmsMatCode},${scanData.vdaMatCode},${scanData.scanTime},${scanData.vdaSerialCode},${scanData.isDeleted}"
        }
        val csvString = csvHeader + csvContent

        // 生成文件名
        val fileName = "ScanData_${System.currentTimeMillis()}.csv"
        // 获取或创建 "export" 目录
        val exportDir = File(context.getExternalFilesDir(null), "export").apply {
            if (!exists()) mkdirs()
        }
        val file = File(exportDir, fileName)

        // 将CSV内容写入文件
        file.writeText(csvString)
    }

    companion object {
        @Volatile private var instance: OfflineScanDataRepository? = null

        fun getInstance(scanDataDao: ScanDataDao) =
            instance ?: synchronized(this) {
                instance ?: OfflineScanDataRepository(scanDataDao).also { instance = it }
            }
    }
}