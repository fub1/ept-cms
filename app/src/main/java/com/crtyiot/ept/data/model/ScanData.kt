package com.crtyiot.ept.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "scanData")
data class ScanData(
    @PrimaryKey(autoGenerate = true)
    val scanDataId: Int = 0,
    val taskId: Int,
    val cmsMatCode: String,
    val vdaMatCode: String,
    val scanTime: LocalDateTime,
    val vdaSerialCode: String,
    val isDeleted: Boolean
)
