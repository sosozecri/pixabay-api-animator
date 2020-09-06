package com.zecri.withingstest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zecri.withingstest.R
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.source.remote.API_BASE_URL
import com.zecri.withingstest.ui.PixabayViewModel
import com.zecri.withingstest.util.*
import kotlinx.android.synthetic.main.layout_footer.*
import kotlinx.android.synthetic.main.layout_header_search.*
import kotlinx.android.synthetic.main.layout_header_selection.*
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(), Navigable, ErrorNotifier<Throwable?> {

    //----------------------------------------------------------------------------
    // Properties
    //----------------------------------------------------------------------------

    /**
     * Interfaces used for fragment & activity coordination
     */
    override var onNext: (() -> Unit)? = null
    override var onPrevious: (() -> Unit)? = null
    override var onError: ((Throwable?) -> Unit)? = null

    /**
     * View model responsible for the pixabay medias lifecycle survival
     */
    private val pixabayViewModel: PixabayViewModel by activityViewModels()

    /**
     * Bundle created from the input data entered by the user
     */
    private val parameters: Bundle
        get() = createBundle(searchEditText.text.toString())

    companion object {
        const val MINIMUM_NUMBER_FOR_ANIMATION = 2
        const val MINIMUM_NUMBER_FOR_PREVIEW = 1

        fun newInstance() = SearchFragment()
    }

    //----------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()

    }

    //----------------------------------------------------------------------------
    // Initialization
    //----------------------------------------------------------------------------

    private fun setup() {
        setupSearchHeader()
        setupSelectionHeader()
        setupViewModel()
        setupRecyclerView()
        setupFooter()
    }

    /**
     * Set up the search header subviews
     */
    private fun setupSearchHeader() {
        val clickListener = OnClickListener {
            if (context?.isOnline() == false) {
                showError(NoConnectivityException())
                return@OnClickListener
            }
            hideKeyboard()
            showLoading()
            pixabayViewModel.loadImages(parameters)

        }
        okButton.setOnClickListener(clickListener)
        searchEditText.setOnEditorActionListener { _, actionId, _ -> //launch onclick action if the user clicks on the system keyboard OK button
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clickListener.onClick(okButton)
            }
            false
        }
    }

    /**
     * Set up the selection header subviews
     */
    private fun setupSelectionHeader() {
        selectionHeader.hide()

        pixabayViewModel.selectedImages.observe(viewLifecycleOwner) { selected ->
            val counter = selected.size

            animationButton.run { //hide animation button if counter < 2
                if (counter >= MINIMUM_NUMBER_FOR_ANIMATION) { show() }
                else { hide() }
            }
            selectionHeader.run {//hide selection header if counter < 1
                if (counter >= MINIMUM_NUMBER_FOR_PREVIEW) { show() }
                else { hide() }
            }
            searchHeader.run {//hide search header if counter > 0
                if (counter >= MINIMUM_NUMBER_FOR_PREVIEW) { hide() }
                else { show() }
            }
            val selectionCounterText = resources.getQuantityString( //get singular or plural accordind to the counter value
                    R.plurals.item_selection_counter,
                    counter
                )
            val formattedText = String.format(selectionCounterText, counter)
            selectionCounter.text = formattedText
        }
    }

    /**
     * Set up the pixabay view model
     */
    private fun setupViewModel() {
        pixabayViewModel.images.observe(viewLifecycleOwner) { result ->
            when {
                result.isSuccess -> result.getOrNull()?.let { showImages(it) }
                result.isFailure -> showError(result.exceptionOrNull())
            }
        }
    }

    /**
     * Set up the recycler view with an empty value
     */
    private fun setupRecyclerView() {
        val alreadySelectedImages = pixabayViewModel.selectedImages.value
        val alreadyLoadedImages = pixabayViewModel.images.value?.getOrNull() ?: arrayListOf()
        if (alreadyLoadedImages.isEmpty()) {
            updateRecyclerView(alreadyLoadedImages, alreadySelectedImages)
        }
    }

    /**
     * Set up the footer subviews
     */
    private fun setupFooter() {
        animationButton.hide()
        animationButton.setOnClickListener {
            val selected = pixabayViewModel.selectedImages.value ?: return@setOnClickListener
            if (selected.size >= MINIMUM_NUMBER_FOR_ANIMATION) {
                onNext?.invoke()
            }
        }
        val credits = getString(R.string.credits)
        creditsUrl.text = String.format(credits, API_BASE_URL)
        creditsLogo.setOnClickListener {
            context?.openUrl(
                url = API_BASE_URL,
                offlineExecutable = { showError(NoConnectivityException()) })
        }
    }

    //----------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------

    /**
     * Update the recycler view content with new values
     */
    private fun updateRecyclerView(
        images: List<PixabayImage>,
        alreadySelectedImages: List<PixabayImage>?
    ) {

        val onImageSelectedAction =
            { image: PixabayImage -> pixabayViewModel.switchSelectionState(image) }

        val pixabayImageAdapter = PixabayImageAdapter(
            images = images,
            onImageSelectedAction = onImageSelectedAction,
            alreadySelectedItems = alreadySelectedImages
        )
        imagesRecyclerView.apply {
            setHasFixedSize(true) // use this setting to improve performance
            layoutManager = LinearLayoutManager(activity)
            adapter = pixabayImageAdapter
        }
    }

    /**
     * Create bundle from the input data entered by the user
     */
    private fun showError(exception: Throwable?) {
        hideLoading()
        onError?.invoke(exception)
    }

    /**
     * Show the images in the recycler view
     */
    private fun showImages(images: List<PixabayImage>) {
        if (images.isEmpty()) {
            return
        }
        val alreadySelectedImages = pixabayViewModel.selectedImages.value
        updateRecyclerView(images, alreadySelectedImages)
        hideLoading()
    }

    private fun showLoading() {
        progressBar.show()
        okButton.disable()
    }

    private fun hideLoading() {
        progressBar.hide()
        okButton.enable()

    }
}