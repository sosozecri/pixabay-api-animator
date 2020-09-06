package com.zecri.withingstest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zecri.withingstest.R
import com.zecri.withingstest.ui.animation.AnimationFragment
import com.zecri.withingstest.ui.search.SearchFragment
import com.zecri.withingstest.util.getErrorMessage
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    //----------------------------------------------------------------------------
    // Properties
    //----------------------------------------------------------------------------

    /**
     * Display the search bar & its results
     */
    lateinit var searchFragment: SearchFragment

    /**
     * Display the animation based on selected images
     */
    lateinit var animationFragment: AnimationFragment

    //----------------------------------------------------------------------------
    // Lifecycle
    //----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupFragments()

        if (savedInstanceState == null) {
            navigateTo(searchFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    //----------------------------------------------------------------------------
    // Initialization
    //----------------------------------------------------------------------------
    /**
     * Create all the fragments
     */
    private fun setupFragments() {

        searchFragment = SearchFragment.newInstance().apply {
            onNext = { navigateTo(animationFragment) }
            onError = { error -> onError(error) }
        }

        animationFragment = AnimationFragment.newInstance().apply {
            onPrevious = { onBackPressed() }
            onError = { error -> onError(error) }
        }
    }

    //----------------------------------------------------------------------------
    // Methods
    //----------------------------------------------------------------------------

    /**
     * Replace the container with the
     * @param fragment
     */
    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Display an error message in a snackbar according to the
     * @param exception value
     */
    fun onError(exception: Throwable?) {
        val errorMessage = resources.getErrorMessage(exception)
        Snackbar.make(container, errorMessage, Snackbar.LENGTH_LONG)
            .show()
    }
}