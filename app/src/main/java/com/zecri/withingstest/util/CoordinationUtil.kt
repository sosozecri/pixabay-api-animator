package com.zecri.withingstest.util

import android.content.res.Resources
import com.zecri.withingstest.R
import retrofit2.HttpException

/**
 * Interfaces used for fragment & activity coordination
 */

interface Navigable {
    var onNext: (() -> Unit)?
    var onPrevious: (() -> Unit)?
}

interface ErrorNotifier<Error : Throwable?> {
    var onError: ((Error) -> Unit)?
}

/**
 * Generate an error message coordinated to the value of
 * @param throwable
 */
fun Resources.getErrorMessage(throwable: Throwable?): String {
    return when {

        throwable is IllegalArgumentException
                || (throwable is HttpException && throwable.isBadRequest()) -> getString(R.string.invalid_input)

        throwable is EmptyResponseException -> String.format(
            getString(R.string.empty_hits),
            throwable.message
        )

        throwable is NoConnectivityException -> getString(R.string.offline)

        throwable?.message?.isNotEmpty() == true -> throwable.message
            ?: getString(R.string.error) // if throwable is non null and throwable message is non null & not empty

        else -> getString(R.string.error)
    }
}

