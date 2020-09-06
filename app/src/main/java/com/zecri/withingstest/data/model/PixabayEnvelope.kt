package com.zecri.withingstest.data.model

data class PixabayEnvelope<M : PixabayMedia>(
    var total: Int? = null,
    var totalHits: Int? = null,
    var hits: List<M>? = null
)