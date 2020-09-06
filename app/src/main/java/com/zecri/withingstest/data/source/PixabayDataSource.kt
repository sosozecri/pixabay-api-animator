package com.zecri.withingstest.data.source

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.model.video.PixabayVideo

abstract class PixabayDataSource {

    abstract suspend fun getImages(parameters: Bundle): List<PixabayImage>?
    abstract suspend fun getVideos(parameters: Bundle): List<PixabayVideo>?
}
