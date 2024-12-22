package com.example.catapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class FavouritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favouritesAdapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.recyclerViewFavourites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        favouritesAdapter = FavouritesAdapter()
        recyclerView.adapter = favouritesAdapter

        fetchFavourites()
    }

    private fun fetchFavourites() {
        val apiKey = resources.getString(R.string.api)
        val url = "https://api.thecatapi.com/v1/favourites"
        val request = object : JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val favouriteImages = mutableListOf<FavouriteImage>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val imageId = jsonObject.getString("image_id")
                    val imageUrl = jsonObject.getJSONObject("image").getString("url")
                    favouriteImages.add(FavouriteImage(imageId, imageUrl))
                }
                favouritesAdapter.setFavouriteImages(favouriteImages)
            },
            { error ->
                error.printStackTrace()
            }) {
            override fun getHeaders(): Map<String, String> {
                return mapOf("x-api-key" to apiKey)
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}


data class FavouriteImage(val id: String, val url: String)
