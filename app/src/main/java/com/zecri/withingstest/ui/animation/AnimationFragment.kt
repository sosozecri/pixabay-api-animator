package com.zecri.withingstest.ui.animation

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zecri.withingstest.R
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.ui.PixabayViewModel
import com.zecri.withingstest.util.ErrorNotifier
import com.zecri.withingstest.util.Navigable
import kotlinx.android.synthetic.main.animation_fragment.*
import java.util.*

class AnimationFragment : Fragment(), Navigable, ErrorNotifier<Throwable?> {

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
     * View model responsible of the pixabay medias lifecycle survival
     */
    private val pixabayViewModel: PixabayViewModel by activityViewModels()

    companion object {
        const val ANIMATION_DURATION = 1500L

        fun newInstance() = AnimationFragment()
    }

    //----------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.animation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animationImageView.clearAnimation() //avoid memory leak due to animation listener
        animationImageView.animate().setListener(null)
    }

    //----------------------------------------------------------------------------
    // Initialization
    //----------------------------------------------------------------------------

    private fun setupViewModel() {
        val images = pixabayViewModel.selectedImages.value
        if (images?.isEmpty() != false) { //if null or empty
            onError?.invoke(IllegalArgumentException())
            onPrevious?.invoke()
            return
        }
        animateImages(images)
    }

    //----------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------

    /**
     * Animate all selected images
     */
    private fun animateImages(images: List<PixabayImage>) {
        val imagesQueue = LinkedList(images)
        animateImage(imagesQueue)
    }

    /**
     * Animate the first image of the queue with a fading effect
     */
    private fun animateImage(queue: LinkedList<PixabayImage>) {
        if (queue.isEmpty())
            return

        val image = queue.poll()
        image?.webformatURL.let { url ->
            Picasso.get().load(url).fetch(object : Callback {
                override fun onSuccess() {
                    animationImageView.alpha = 0f
                    Picasso.get().load(url).into(animationImageView)
                    animationImageView.animate().setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {}
                        override fun onAnimationEnd(animation: Animator?) {
                            animateImage(queue)
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}

                    }).setDuration(ANIMATION_DURATION)
                        .alpha(1f)
                        .start()
                }

                override fun onError(exception: Exception?) { onError?.invoke(exception)}
            })
        }

    }
}