package com.zecri.withingstest.data.model

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName

abstract class PixabayMedia(
    val id: Long, //    A unique identifier for this image.
    val pageURL: String? = null, //    Source page on Pixabay, which provides a download link for the original image of the dimension imageWidth x imageHeight and the file size imageSize.
    val type: String? = null,
    val tags: String? = null,
    val views: Int? = null, //    Total number of views.
    val downloads: Int? = null, //    Total number of downloads.
    val favorites: Int? = null, //     Total number of favorites.
    val likes: Int? = null, //    Total number of likes.
    val comments: Int? = null, //    Total number of comments.
    @SerializedName("user_id")
    val userId: Long? = null, //    User ID of the contributor. Profile URL: https://pixabay.com/users/{ USERNAME }-{ ID }/
    val user: String? = null,//    User name of the contributor. Profile URL: https://pixabay.com/users/{ USERNAME }-{ ID }/
    val userImageURL: String? = null //    Profile picture URL (250 x 250 px).
) {

    companion object {
        const val POPULAR = "popular"
        const val LATEST = "latest"

        const val LANGS =
            "cs, da, de, en, es, fr, id, it, hu, nl, no, pl, pt, ro, sk, fi, sv, tr, vi, th, bg, ru, el, ja, ko, zh"
        const val CATEGORIES =
            "backgrounds, fashion, nature, science, education, feelings, health, people, religion, places, animals, industry, computer, food, sports, transportation, travel, buildings, business, music"
    }

    @Retention(AnnotationRetention.SOURCE) //Enums have cleaner readability but are quite heavy
    @StringDef(POPULAR, LATEST)
    annotation class Order

}