package com.zecri.withingstest.data.model.video

data class PixabayVideoFormat(
    val large: PixabayVideoFormatDetail? = null,
    val medium: PixabayVideoFormatDetail? = null,
    val small: PixabayVideoFormatDetail? = null,
    val tiny: PixabayVideoFormatDetail? = null,
)