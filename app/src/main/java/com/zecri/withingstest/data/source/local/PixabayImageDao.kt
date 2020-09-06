package com.zecri.withingstest.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zecri.withingstest.data.model.image.PixabayImage


interface PixabayImageDao {

    @Query("SELECT * from images")
    fun getAll(): List<PixabayImage>

    @Query("SELECT * FROM images where id = :id ")
    fun getImageById(id: Long): List<PixabayImage>

    @Query("SELECT * FROM images where tags LIKE :tags ")
    fun getImageByTags(tags: String): List<PixabayImage>

    @Query("DELETE FROM images")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(image: PixabayImage)

}