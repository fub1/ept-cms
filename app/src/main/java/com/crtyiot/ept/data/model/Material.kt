package com.crtyiot.ept.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


// VdaMatId，vdaMatCode，cmsMatCode和pickQty。
// 这些字段将分别对应VDA物料ID，VDA物料代码，CMS物料代码和拣选数量。
// https://developer.android.com/topic/architecture/data-layer/offline-first?hl=zh-cn

@Entity(tableName = "material")
data class Material(
    @PrimaryKey
    val vdaMatCode: String,
    @ColumnInfo(name = "cmsMatId")
    val cmsMatCode: String,
    @ColumnInfo(name = "pickQty")
    val pickQty: Int
)
