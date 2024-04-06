package com.crtyiot.ept.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.Task

@Dao
interface TaskDao {

        @Query("SELECT * from task")
        fun getAllTasks(): Flow<List<Task>>

        @Query("SELECT * from task WHERE taskId = :taskId")
        fun getTask(taskId: Int): Flow<Task>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(task: Task)

        @Update
        suspend fun update(task: Task)

        @Delete
        suspend fun delete(task: Task)
}