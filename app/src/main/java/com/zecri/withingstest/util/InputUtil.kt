package com.zecri.withingstest.util

import android.os.Bundle
import com.zecri.withingstest.data.model.PixabayMedia
import com.zecri.withingstest.data.source.remote.LANG
import com.zecri.withingstest.data.source.remote.PAGE
import com.zecri.withingstest.data.source.remote.PER_PAGE
import com.zecri.withingstest.data.source.remote.Q
import java.io.IOException

const val SPACE = " "
const val DOUBLE_SPACE = "  "
const val COMMA = ","
const val TAG_SPLITTER = COMMA + SPACE
const val SEARCH_TERMS_SPLITTER = "+"

/**
 * Exception describing a not found situation
 */
class EmptyResponseException(message: String? = null) : IOException(message)

fun createBundle(searchTerms: String) =
    Bundle().apply {
        putString(Q, searchTerms)
    }

fun throwExceptionWhenInvalid(bundle: Bundle, maximumBundleSize: Int) {

    if (bundle.size() > maximumBundleSize) {
        throw IllegalArgumentException()
    }

    if (bundle.containsKey(LANG) && bundle.getString(LANG)
            ?.let { it in PixabayMedia.LANGS } != true
    ) {
        throw IllegalArgumentException(LANG)
    }
    if (bundle.containsKey(PAGE) && bundle.getInt(PAGE) < 1) {
        throw IllegalArgumentException(PAGE)
    }
    if (bundle.containsKey(PER_PAGE) && bundle.getInt(PER_PAGE) !in 3..200) {
        throw IllegalArgumentException(PER_PAGE)
    }
    //TODO CONTINUE CHECK WITH OTHER PARAMETERS...
}

fun formatToUrlEncoded(searchTerm: String): String? {
    var _searchTerm = searchTerm.replace(COMMA, SPACE)

    while (DOUBLE_SPACE in _searchTerm) {
        _searchTerm = _searchTerm.replace(DOUBLE_SPACE, SPACE)
    }
    if (_searchTerm.endsWith(SPACE)) {
        _searchTerm = _searchTerm.removeSuffix(SPACE)
    }
    if (_searchTerm.startsWith(SPACE)) {
        _searchTerm = _searchTerm.removePrefix(SPACE)
    }

    if (_searchTerm.isEmpty()) {
        return null
    }

    return _searchTerm.replace(SPACE, SEARCH_TERMS_SPLITTER)
}