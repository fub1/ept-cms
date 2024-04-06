package com.crtyiot.ept.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.model.TaskWithScannedCount

@Dao
interface TaskDao {

        @Query("SELECT * from task")
        fun getAllTasks(): Flow<List<Task>>

        @Query("SELECT * from task WHERE taskId = :taskId")
        fun getTask(taskId: Int): Flow<Task>


        // 可以返回包含任务日期时间，员工号，原材料号，目标数量，和已扫描数量。
        @Query("""
        SELECT task.taskId, task.staff, task.vdaMatId, task.createTaskTime, task.targetQty, 
        (SELECT COUNT(*) FROM scanData WHERE scanData.taskId = task.taskId AND scanData.isDeleted = 0) as scannedCount
        FROM task
    """)
        fun getTasksWithScannedCount(): Flow<List<TaskWithScannedCount>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(task: Task)

        @Update
        suspend fun update(task: Task)

        @Delete
        suspend fun delete(task: Task)
}