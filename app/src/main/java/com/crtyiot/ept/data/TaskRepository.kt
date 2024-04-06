package com.crtyiot.ept.data
import com.crtyiot.ept.data.Dao.TaskDao
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.model.TaskWithScannedCount
import com.crtyiot.scan.data.repository.TaskRespository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor (
    private val taskDao: TaskDao
) : TaskRespository {
    override fun getAll(): Flow<List<Task>> = taskDao.getAllTasks()

    fun taskDashboard(): Flow<List<TaskWithScannedCount>> = taskDao.getTasksWithScannedCount()
    override suspend fun insert(task: Task) = taskDao.insert(task)
    override suspend fun delete(task: Task) = taskDao.delete(task)
    override suspend fun update(task: Task) = taskDao.update(task)

    companion object {
        @Volatile private var instance: TaskRepository? = null
        fun getInstance(taskDao: TaskDao) =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(taskDao).also { instance = it }
            }
    }

}