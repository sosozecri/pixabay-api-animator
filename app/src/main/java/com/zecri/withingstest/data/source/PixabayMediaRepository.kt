package com.zecri.withingstest.data.source

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.source.remote.PixabayRemoteDataSource


internal class PixabayMediaRepository {

    private val remoteDataSource = PixabayRemoteDataSource

    suspend fun getImages(parameters: Bundle): List<PixabayImage>? {
        return remoteDataSource.getImages(parameters)
    }
}