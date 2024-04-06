package com.crtyiot.ept.data

import android.util.Log
import com.crtyiot.ept.data.Dao.MaterialDao
import com.crtyiot.ept.network.MaterialApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.crtyiot.ept.data.model.Material
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException
import javax.inject.Singleton

@Singleton
class MaterialRepository @Inject constructor(
    val materialDao: MaterialDao,
    val materialApiService: MaterialApiService
) {

    fun getAll(): Flow<List<Material>> = materialDao.getAllMaterials()

    fun getMaterial(vda: String): Flow<Material> = materialDao.getMaterial(vda)



    suspend fun refreshMaterials() {
        try {
            // Set a timeout for the network request
            withTimeout(10_000L) {
                val materialsFromServer = materialApiService.getMaterials()
                materialDao.deleteAll()
                materialsFromServer.forEach { material ->
                    materialDao.insert(material)
                }
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> {
                    // Handle no network connection
                    Log.e("MaterialRepository", "No network connection.")
                }
                is TimeoutCancellationException -> {
                    // Handle request timeout
                    Log.e("MaterialRepository", "Timeout.")
                }
                else -> {
                    // Handle other exceptions
                    Log.e("MaterialRepository", "An error occurred: ${e.message}")
                }
            }
        }
    }

    suspend fun insert(material: Material) = materialDao.insert(material)

    suspend fun delete(material: Material) = materialDao.delete(material)

    suspend fun update(material: Material) = materialDao.update(material)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: MaterialRepository? = null

        fun getInstance(materialDao: MaterialDao, materialApiService: MaterialApiService) =
            instance ?: synchronized(this) {
                instance ?: MaterialRepository(materialDao, materialApiService).also { instance = it }
            }
    }
}