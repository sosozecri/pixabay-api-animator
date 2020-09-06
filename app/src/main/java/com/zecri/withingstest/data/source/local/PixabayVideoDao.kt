package com.zecri.withingstest.data.source.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zecri.withingstest.data.model.video.PixabayVideo

interface PixabayVideoDao {

    @Query("SELECT * from videos")
    fun getAll(): List<PixabayVideo>

    @Query("SELECT * FROM videos where id = :id ")
    fun getVideoById(id: Long): List<PixabayVideo>

    @Query("SELECT * FROM videos where tags LIKE :tags ")
    fun getVideoByTags(tags: String): List<PixabayVideo>

    @Query("DELETE FROM videos")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVideo(video: PixabayVideo)

}