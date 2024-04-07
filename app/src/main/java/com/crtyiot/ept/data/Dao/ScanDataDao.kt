package com.crtyiot.ept.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.crtyiot.ept.data.model.Material
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.ScanData

// scanDataId，taskId，cmsMatCode，vdaMatCode，scanTime，vdaSerialCode和isDeleted。
// 这些字段将分别对应扫描数据ID，任务ID，CMS物料代码，VDA物料代码，扫描时间，VDA序列代码和删除状态。

@Dao
interface ScanDataDao {

    @Query("SELECT * from scanData")
    fun getAllScanData(): Flow<List<ScanData>>

    @Query("SELECT * from scanData WHERE taskId = :taskId")
    fun getScanTask(taskId: String): Flow<List<ScanData>>

    @Query("SELECT * from scanData WHERE taskId = :taskId AND isDeleted = 0")
    fun getScanTaskNotDeleted(taskId: String): Flow<List<ScanData>>



    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(scanData: ScanData)

    @Update
    suspend fun update(scanData: ScanData)

    @Delete
    suspend fun delete(scanData: ScanData)
}

