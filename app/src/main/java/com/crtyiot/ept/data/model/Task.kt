package com.crtyiot.ept.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// TaskId，staff，vdaMatId，createTaskTime和isSynced。
// 这些字段将分别对应任务ID，员工，VDA物料ID，任务创建时间和同步状态

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val staff: String,
    val vdaMatId: Int,
    val createTaskTime: LocalDateTime,
    val isSynced: Boolean
)