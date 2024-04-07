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
import com.crtyiot.ept.data.model.Material

@Dao
interface TaskDao {

        @Query("SELECT * from task")
        fun getAllTasks(): Flow<List<Task>>

        @Query("SELECT * from task WHERE taskId = :taskId")
        fun getTask(taskId: Int): Flow<Task>

        // uuid 查询vdamat
        @Query("SELECT vdaMatId from task WHERE taskId = :taskId")
        fun getVdaMatIdByTaskId(taskId: String): Flow<String>

        // uuid 查询taskqty
        @Query("SELECT targetQty from task WHERE taskId = :taskId")
        fun getTagQTYByTaskId(taskId: String): Flow<Int>


        // uuid 查询cmsmat
        @Query("""  SELECT material.cmsMatId
                    FROM task 
                    INNER JOIN material ON task.vdaMatId = material.vdaMatCode 
                    WHERE task.taskId = :taskId""")
        fun getCmsMatCodeByTaskId(taskId: String): Flow<String>

        // 可以返回包含任务日期时间，员工号，原材料号，目标数量，和已扫描数量。
        @Query("""
        SELECT task.taskId, task.staff, task.vdaMatId, task.createTaskTime, task.targetQty, 
        (SELECT COUNT(*) FROM scanData WHERE scanData.taskId = task.taskId AND scanData.isDeleted = 0) as scannedCount
        FROM task
        ORDER BY task.createTaskTime DESC
    """)
        fun getTasksWithScannedCount(): Flow<List<TaskWithScannedCount>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(task: Task)

        @Update
        suspend fun update(task: Task)

        @Delete
        suspend fun delete(task: Task)
}