package com.zecri.withingstest.ui

import android.os.Bundle
import androidx.lifecycle.*
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.source.PixabayMediaRepository
import com.zecri.withingstest.data.source.remote.Q
import com.zecri.withingstest.util.EmptyResponseException

internal class PixabayViewModel(
    private val repository: PixabayMediaRepository = PixabayMediaRepository(),
    ) : ViewModel() {

    //----------------------------------------------------------------------------
    // Properties
    //----------------------------------------------------------------------------

    /**
     * Search request parameters formatted as a bundle
     */
    private var searchParameters = MutableLiveData<Bundle>()

    /**
     * Livedata responsible of selected images lifecycle survival
     */
    internal var selectedImages = MutableLiveData<MutableList<PixabayImage>>()

    /**
     * Livedata responsible of images lifecycle survival
     */
    internal var imagesResult: LiveData<Result<List<PixabayImage>?>> =
        searchParameters.switchMap { parameters -> //switch map used to get images livedata notified when search parameters value changes
            liveData { //no need to pass any Dispatchers.IO scheduler because Retrofit does that for you (since Retrofit 2.6.0).
                try {
                    val result = repository.getImages(parameters)
                    if (result?.isEmpty() == true) {
                        throw EmptyResponseException(parameters.getString(Q))
                    }
                    emit(Result.success(result))

                } catch (exception: Exception) {
                    emit(Result.failure(exception))
                }
            }
        }

    /**
     * Wrapper function to avoid chaining call to get the images from imagesResult
     */
    fun getImages() : List<PixabayImage>? = imagesResult.value?.getOrNull()

    /**
     * Update the search parameters value
     * Every time the search input changes and is validated by the button click,
     * the images livedata is notified and performs search result using the new search terms
     */
    fun loadImages(parameters: Bundle? = null) {
        searchParameters.value = parameters
    }

    //----------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------

    /**
     * Add a selected image to the selected list, or remove it if already existing
     * @return :
     * false if the image was already selected and is turned to be unselected
     * true if the image was not selected before and is turned to be selected
     */
    fun switchSelectionState(image: PixabayImage): Boolean {
        val value = selectedImages.value
        if (value == null) {
            selectedImages.value = mutableListOf(image)
            return true
        }

        val isAlreadySelected = image in value
        if (isAlreadySelected) {
            value -= image
        } else {
            value += image
        }
        selectedImages.value = value //mandatory call to notify the observers
        return !isAlreadySelected
    }

}