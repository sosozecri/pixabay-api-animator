package com.zecri.withingstest.data.source

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.model.video.PixabayVideo
import com.zecri.withingstest.data.source.remote.PixabayRemoteDataSource


internal class PixabayMediaRepository(
    private val dataSource: PixabayDataSource = PixabayRemoteDataSource
) {

    suspend fun getImages(parameters: Bundle): List<PixabayImage>? {
        return dataSource.getImages(parameters)
    }

    suspend fun getVideos(parameters: Bundle): List<PixabayVideo>? {
        return dataSource.getVideos(parameters)
    }
}