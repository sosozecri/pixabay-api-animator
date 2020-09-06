package com.zecri.withingstest.data.source.remote

import com.zecri.withingstest.data.model.PixabayEnvelope
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.model.video.PixabayVideo
import retrofit2.http.GET
import retrofit2.http.Query

internal const val API_BASE_URL = "https://pixabay.com/"
internal const val API_IMAGE_PATH = "api/"
internal const val API_VIDEO_PATH = API_IMAGE_PATH + "videos/"

private const val API_KEY = "5511001-7691b591d9508e60ec89b63c4"

internal const val KEY = "key"
internal const val Q = "q"
internal const val LANG = "lang"
internal const val ID = "id"
internal const val IMAGE_TYPE = "image_type"
internal const val VIDEO_TYPE = "video_type"
internal const val ORIENTATION = "orientation"
internal const val CATEGORY = "category"
internal const val MIN_WIDTH = "min_width"
internal const val MIN_HEIGHT = "min_height"
internal const val COLORS = "colors"

internal const val EDITORS_CHOICE = "editors_choice"
internal const val SAFE_SEARCH = "safesearch"
internal const val ORDER = "order"
internal const val PAGE = "page"
internal const val PER_PAGE = "per_page"
internal const val CALLBACK = "callback"

internal const val TOTAL_IMAGE_AVAILABLE_PARAMETERS = 17
internal const val TOTAL_VIDEO_AVAILABLE_PARAMETERS = 15

interface PixabayApi {

    @GET(API_IMAGE_PATH)
    suspend fun getImagesFromApi(
        @Query(KEY) key: String = API_KEY,
        @Query(Q) q: String? = null,
        @Query(LANG) lang: String? = null,
        @Query(ID) id: Long? = null,
        @Query(IMAGE_TYPE) imageType: String? = null,
        @Query(ORIENTATION) orientation: String? = null,
        @Query(CATEGORY) category: String? = null,
        @Query(MIN_WIDTH) minWidth: Int? = null,
        @Query(MIN_HEIGHT) minHeight: Int? = null,
        @Query(COLORS) colors: String? = null,
        @Query(EDITORS_CHOICE) editorsChoice: Boolean? = null,
        @Query(SAFE_SEARCH) safeSearch: Boolean? = null,
        @Query(ORDER) order: String? = null,
        @Query(PAGE) page: Int? = null,
        @Query(PER_PAGE) perPage: Int? = null,
        @Query(CALLBACK) callback: String? = null
    ): PixabayEnvelope<PixabayImage>

    // Search for Video
    @GET(API_VIDEO_PATH)
    suspend fun getVideosFromApi(
        @Query(KEY) key: String = API_KEY,
        @Query(Q) q: String? = null,
        @Query(LANG) lang: String? = null,
        @Query(ID) id: Long? = null,
        @Query(VIDEO_TYPE) videoType: String? = null,
        @Query(CATEGORY) category: String? = null,
        @Query(MIN_WIDTH) minWidth: Int? = null,
        @Query(MIN_HEIGHT) minHeight: Int? = null,
        @Query(EDITORS_CHOICE) editorsChoice: Boolean? = null,
        @Query(SAFE_SEARCH) safeSearch: Boolean? = null,
        @Query(ORDER) order: String? = null,
        @Query(PAGE) page: Int? = null,
        @Query(PER_PAGE) perPage: Int? = null,
        @Query(CALLBACK) callback: String? = null
    ): PixabayEnvelope<PixabayVideo>

}
