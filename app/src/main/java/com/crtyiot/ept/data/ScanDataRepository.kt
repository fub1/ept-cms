package com.crtyiot.ept.data

import com.crtyiot.ept.data.Dao.ScanDataDao
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.repository.ScanDataRespository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanDataRepository @Inject constructor (
    private val scanDataDao: ScanDataDao
) : ScanDataRespository {

    override fun getAll(): Flow<List<ScanData>> = scanDataDao.getAllScanData()

    override fun getScanTask(taskId: String): Flow<List<ScanData>> = scanDataDao.getScanTask(taskId)


    override suspend fun insert(scanData: ScanData) = scanDataDao.insert(scanData)

    override suspend fun delete(scanData: ScanData) = scanDataDao.delete(scanData)

    override suspend fun update(scanData: ScanData) = scanDataDao.update(scanData)

    companion object {
        @Volatile private var instance: ScanDataRepository? = null

        fun getInstance(scanDataDao: ScanDataDao) =
            instance ?: synchronized(this) {
                instance ?: ScanDataRepository(scanDataDao).also { instance = it }
            }
    }
}