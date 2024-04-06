package com.crtyiot.ept.data.model

data class TaskWithScannedCount(
    val taskId: String,
    val staff: String,
    val vdaMatId: String,
    val createTaskTime: String,
    val targetQty: Int,
    val scannedCount: Int
)