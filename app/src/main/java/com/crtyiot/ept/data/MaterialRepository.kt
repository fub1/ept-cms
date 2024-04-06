package com.crtyiot.ept.data

import com.crtyiot.ept.data.Dao.MaterialDao
import com.crtyiot.ept.network.MaterialApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.crtyiot.ept.data.model.Material
import javax.inject.Singleton

@Singleton
class MaterialRepository @Inject constructor(
    val materialDao: MaterialDao,
    val materialApiService: MaterialApiService
) {

    fun getAll(): Flow<List<Material>> = materialDao.getAllMaterials()

    suspend fun refreshMaterials() {
        val materialsFromServer = materialApiService.getMaterials()
        materialsFromServer.forEach { material ->
            materialDao.insert(material)
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