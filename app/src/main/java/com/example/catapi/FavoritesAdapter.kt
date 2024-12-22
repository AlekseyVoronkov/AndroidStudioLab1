package com.example.catapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {

    private var favouriteImages: List<FavouriteImage> = emptyList()

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.favouriteImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favouriteImage = favouriteImages[position]
        Glide.with(holder.itemView.context).load(favouriteImage.url).into(holder.imageView)
    }

    override fun getItemCount(): Int = favouriteImages.size

    fun setFavouriteImages(images: List<FavouriteImage>) {
        favouriteImages = images
        notifyDataSetChanged()
    }
}