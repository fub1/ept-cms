package com.crtyiot.ept.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey


// VdaMatId，vdaMatCode，cmsMatCode和pickQty。
// 这些字段将分别对应VDA物料ID，VDA物料代码，CMS物料代码和拣选数量。

@Entity(tableName = "material")
data class Material(
    @PrimaryKey(autoGenerate = true)
    val vdaMatId: Int = 0,
    val vdaMatCode: String,
    val cmsMatCode: String,
    val pickQty: Int
)

