package com.zecri.withingstest.data.source

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage

abstract class PixabayDataSource {

    abstract suspend fun getImages(parameters: Bundle): List<PixabayImage>?
    //TODO implement getVideos as a bonus if enought time
}
