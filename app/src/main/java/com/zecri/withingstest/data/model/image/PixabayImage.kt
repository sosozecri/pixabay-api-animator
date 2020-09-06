package com.zecri.withingstest.data.model.image

import androidx.annotation.StringDef
import androidx.room.Entity
import com.zecri.withingstest.data.model.PixabayMedia

@Entity(tableName = "images") //TODO implement as a bonus if enought time
class PixabayImage(
    id: Long,
    pageURL: String? = null,
    type: String? = null,
    tags: String? = null,
    var previewURL: String? = null,
    var previewWidth: Int? = null,
    var previewHeight: Int? = null,
    var webformatURL: String? = null,
    var webformatWidth: Int? = null,
    var webformatHeight: Int? = null,
    var largeImageURL: String? = null,
    var imageWidth: Int? = null,
    var imageHeight: Int? = null,
    var imageSize: Int? = null,
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

        const val PHOTO = "photo"
        const val VECTOR = "vector"
        const val ILLUSTRATION = "illustration"

        const val HORIZONTAL = "horizontal"
        const val VERTICAL = "vertical"
    }

    //Enums have cleaner readability but are quite heavy
    // TODO use annotation as a bonus if enought time
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(ALL, PHOTO, VECTOR, ILLUSTRATION)
    annotation class ImageType

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(VERTICAL, HORIZONTAL)
    annotation class Orientation

}