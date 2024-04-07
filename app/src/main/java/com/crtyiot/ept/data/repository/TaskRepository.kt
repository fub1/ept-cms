package com.crtyiot.scan.data.repository

import com.crtyiot.ept.data.model.Task
import kotlinx.coroutines.flow.Flow

// UI elements (state) ->  ViewModel  ->  Use_case Layer  ->Repository  ->  Data Source
// Data Source: (DB/network/file/DataStore)
// ViewModel or Use_case Layer only access the data from  "repository" not from data source

// Data Layer important step 2-1:
// Interface of repo:provide data from data source to Use_case/UI layer
// Define the repository interface

// Flow for data observation in the UI or Use_case layer

// data write to DB mast use kotlin coroutines(suspend fun)
interface TaskRepository {
    fun getAll(): Flow<List<Task>>

    fun getVdaMatId(vdaMatId: String): Flow<Task>

    suspend fun insert(task: Task)
    suspend fun delete(task: Task)
    suspend fun update(task: Task)

}