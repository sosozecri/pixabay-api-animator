package com.zecri.withingstest.data.model.video

import androidx.annotation.StringDef
import androidx.room.Entity
import com.zecri.withingstest.data.model.PixabayMedia

@Entity(tableName = "videos")
class PixabayVideo(

    id: Long,
    pageURL: String? = null,
    type: String? = null,
    tags: String? = null,
    val duration: Long? = null,
    val pictureId: String? = null,
    val videos: List<PixabayVideoFormat>? = null,
    views: Int? = null,
    downloads: Int? = null,
    favorites: Int? = null,
    likes: Int? = null,
    comments: Int? = null,
    userId: Long? = null,
    user: String? = null,
    userImageURL: String? = null

) : PixabayMedia(
    id,
    pageURL,
    type,
    tags,
    views,
    downloads,
    favorites,
    likes,
    comments,
    userId,
    user,
    userImageURL
) {
    companion object {
        const val ALL = "all"

        const val FILM = "film"
        const val ANIMATION = "animation"
    }

//Enums have cleaner readability but are quite heavy

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(ALL, FILM, ANIMATION)
    annotation class VideoType
}
