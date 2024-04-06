package com.crtyiot.ept.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "scanData")
data class ScanData(
    @PrimaryKey
    val taskId: String,
    val cmsMatCode: String,
    val vdaMatCode: String,
    val scanTime: String,
    val vdaSerialCode: String,
    val isDeleted: Boolean
)
