package com.example.catapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var catAdapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        catAdapter = CatAdapter { imageId -> favouriteImage(imageId) }
        recyclerView.adapter = catAdapter

        findViewById<Button>(R.id.viewFavouritesButton).setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreImages()
                }
            }
        })

        fetchCatImages()
    }

    private fun fetchCatImages() {
        val apiKey = resources.getString(R.string.api)
        val url = "https://api.thecatapi.com/v1/images/search?limit=10&page=$page&api_key=$apiKey"
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val catImages = mutableListOf<CatImage>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val imageUrl = jsonObject.getString("url")
                    val imageId = jsonObject.getString("id")
                    catImages.add(CatImage(imageId, imageUrl))
                }
                catAdapter.setCatImages(catImages)
            },
            { error ->
                error.printStackTrace()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun loadMoreImages() {
        page++
        fetchCatImages()
    }

    private fun favouriteImage(imageId: String) {
        val apiKey = resources.getString(R.string.api)
        val url = "https://api.thecatapi.com/v1/favourites"
        val body = JSONObject().apply {
            put("image_id", imageId)
            put("sub_id", "user-123") // Optional unique id of your user
        }

        val request = object : JsonObjectRequest(Request.Method.POST, url, body,
            { response ->
                // Handle success response
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

data class CatImage(val id: String, val url: String)
