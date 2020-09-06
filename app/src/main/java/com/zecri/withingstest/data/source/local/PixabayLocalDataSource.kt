package com.zecri.withingstest.data.source.local

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.model.video.PixabayVideo
import com.zecri.withingstest.data.source.PixabayDataSource

/**
 * TODO Room implementation bonus to do if enough time
 */
class PixabayLocalDataSource : PixabayDataSource() {

    private lateinit var pixabayImageDao: PixabayImageDao

    override suspend fun getImages(parameters: Bundle): List<PixabayImage>? {
        TODO("Not yet implemented")
    }

    override suspend fun getVideos(parameters: Bundle): List<PixabayVideo>? {
        TODO("Not yet implemented")
    }
}