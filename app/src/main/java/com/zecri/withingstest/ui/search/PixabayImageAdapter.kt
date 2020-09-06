package com.zecri.withingstest.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zecri.withingstest.R
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.util.select
import com.zecri.withingstest.util.unselect
import kotlinx.android.synthetic.main.item_image.view.*

/**
 * Provide a binding from Pixabay image set to views that are displayed in the recycler view
 */
class PixabayImageAdapter(
    private val images: List<PixabayImage>,
    private val onImageSelectedAction: ((PixabayImage) -> Unit),
) : RecyclerView.Adapter<PixabayImageAdapter.PixabayImageViewHolder>() {

    /**
     *  Describe a Pixabay image item view and metadata about its place within the RecyclerView
     */
    inner class PixabayImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Load & set the Pixabay image item view content
         */
        fun bind(image: PixabayImage) {
            Picasso.get().load(image.webformatURL).into(itemView.imageView)
            itemView.setOnClickListener { onImageSelectedAction(image) }
        }

        /**
         * Update the Pixabay image item view content
         */
        fun update(isSelected: Boolean) {
            if (isSelected)
                itemView.select()
            else
                itemView.unselect()
        }
    }

    /**
     * Create each Pixabay image item view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PixabayImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return PixabayImageViewHolder(view)
    }

    /**
     * Replace the content of each Pixabay image item view
     */
    override fun onBindViewHolder(holder: PixabayImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    /**
     * Replace the content of each Pixabay image item view
     */
    fun setSelectedViewHolder(holder: PixabayImageViewHolder, isSelected: Boolean) {
        holder.update(isSelected)
    }

    override fun getItemCount() = images.size

}