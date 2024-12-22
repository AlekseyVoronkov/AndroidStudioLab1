package com.example.catapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter(private val onFavouriteClick: (String) -> Unit) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    private var catImages: List<CatImage> = emptyList()

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.catImageView)
        val favouriteButton: Button = itemView.findViewById(R.id.favouriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val catImage = catImages[position]
        Glide.with(holder.itemView.context).load(catImage.url).into(holder.imageView)
        holder.favouriteButton.setOnClickListener { onFavouriteClick(catImage.id) }
    }

    override fun getItemCount(): Int = catImages.size

    fun setCatImages(images: List<CatImage>) {
        catImages = images
        notifyDataSetChanged()
    }
}