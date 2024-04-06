package com.crtyiot.ept.data
import com.crtyiot.ept.data.Dao.MaterialDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.crtyiot.ept.data.model.Material
import com.crtyiot.scan.data.repository.MaterialRepository
import javax.inject.Singleton

// Data Layer important step 2-2:
// Repository implementation with Injection

@Singleton
class MaterialRepository @Inject constructor(
    private val materialDao: MaterialDao
) : MaterialRepository
{

    override fun getAll(): Flow<List<Material>> = materialDao.getAllMaterials()

    override suspend fun insert(material: Material) = materialDao.insert(material)

    override suspend fun delete(material: Material) = materialDao.delete(material)

    override suspend fun update(material: Material) = materialDao.update(material)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: MaterialRepository? = null

        fun getInstance(materialDao: MaterialDao) =
            instance ?: synchronized(this) {
                instance ?: MaterialRepository(materialDao).also { instance = it }
            }
    }




}

