package com.zecri.withingstest.data.source.remote

import android.os.Bundle
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.model.video.PixabayVideo
import com.zecri.withingstest.data.source.PixabayDataSource
import com.zecri.withingstest.util.formatToUrlEncoded
import com.zecri.withingstest.util.throwExceptionWhenInvalid
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PixabayRemoteDataSource : PixabayDataSource() {

    private val client: PixabayApi by lazy {
        create()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun create(): PixabayApi = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(PixabayApi::class.java)


    override suspend fun getImages(parameters: Bundle): List<PixabayImage>? {
        val formattedSearchTerm = parameters.getString(Q)?.let { formatToUrlEncoded(it) }
        parameters.putString(Q, formattedSearchTerm)
        throwExceptionWhenInvalid(parameters, TOTAL_IMAGE_AVAILABLE_PARAMETERS)

        val envelope = client.getImagesFromApi(
            q = parameters.getString(Q, null),
            lang = parameters.getString(LANG, null),
            id = if (parameters.containsKey(ID)) parameters.getLong(ID) else null,
            imageType = parameters.getString(IMAGE_TYPE, null),
            orientation = parameters.getString(ORIENTATION, null),
            category = parameters.getString(CATEGORY, null),
            minWidth = if (parameters.containsKey(MIN_WIDTH)) parameters.getInt(MIN_WIDTH) else null,
            minHeight = if (parameters.containsKey(MIN_HEIGHT)) parameters.getInt(MIN_HEIGHT) else null,
            colors = parameters.getString(COLORS, null),
            editorsChoice = parameters.getBoolean(EDITORS_CHOICE),
            safeSearch = parameters.getBoolean(SAFE_SEARCH),
            order = parameters.getString(ORDER, null),
            page = if (parameters.containsKey(PAGE)) parameters.getInt(PAGE) else null,
            perPage = if (parameters.containsKey(PER_PAGE)) parameters.getInt(PER_PAGE) else null,
            callback = parameters.getString(CALLBACK, null)
        )
        return envelope.hits
    }

    override suspend fun getVideos(parameters: Bundle): List<PixabayVideo>? {
        throwExceptionWhenInvalid(parameters, TOTAL_VIDEO_AVAILABLE_PARAMETERS)

        val envelope = client.getVideosFromApi(
            q = parameters.getString(Q, null),
            lang = parameters.getString(LANG, null),
            id = if (parameters.containsKey(ID)) parameters.getLong(ID) else null,
            videoType = parameters.getString(VIDEO_TYPE, null),
            category = parameters.getString(CATEGORY, null),
            minWidth = if (parameters.containsKey(MIN_WIDTH)) parameters.getInt(MIN_WIDTH) else null,
            minHeight = if (parameters.containsKey(MIN_HEIGHT)) parameters.getInt(MIN_HEIGHT) else null,
            editorsChoice = parameters.getBoolean(EDITORS_CHOICE),
            safeSearch = parameters.getBoolean(SAFE_SEARCH),
            order = parameters.getString(ORDER, null),
            page = if (parameters.containsKey(PAGE)) parameters.getInt(PAGE) else null,
            perPage = if (parameters.containsKey(PER_PAGE)) parameters.getInt(PER_PAGE) else null,
            callback = parameters.getString(CALLBACK, null)
        )
        return envelope.hits
    }
}
