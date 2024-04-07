package com.crtyiot.ept.data
import com.crtyiot.ept.data.Dao.TaskDao
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.Task
import com.crtyiot.ept.data.model.TaskWithScannedCount
import com.crtyiot.scan.data.repository.TaskRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor (
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getAll(): Flow<List<Task>> = taskDao.getAllTasks()
    override fun getVdaMatId(vdaMatId: String): Flow<Task> {
        TODO("Not yet implemented")
    }

    fun taskDashboard(): Flow<List<TaskWithScannedCount>> = taskDao.getTasksWithScannedCount()


    fun getVdaMatIdByTaskId(taskId: String): Flow<String> = taskDao.getVdaMatIdByTaskId(taskId)

    fun getTagQTYByTaskId(taskId: String): Flow<Int> = taskDao.getTagQTYByTaskId(taskId)

    fun getCmsMatCodeByTaskId(taskId: String): Flow<String> = taskDao.getCmsMatCodeByTaskId(taskId)
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