package com.crtyiot.ept.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.crtyiot.ept.data.model.Material

@Dao
interface MaterialDao {

    @Query("SELECT * from material")
        fun getAllMaterials(): Flow<List<Material>>

    @Query("SELECT * from material WHERE vdaMatCode = :vdaMatCode")
    fun getMaterial(vdaMatCode: String): Flow<Material>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(material: Material)

    @Update
    suspend fun update(material: Material)

    @Delete
    suspend fun delete(material: Material)

    @Query("DELETE FROM material")
    suspend fun deleteAll()
}