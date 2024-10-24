package com.example.gson

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import timber.log.Timber
import timber.log.Timber.Forest.plant
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        plant(Timber.DebugTree())
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
            .build()
        var links: List<String>

        Thread {

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("error")

                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()

                val wrapper = gson.fromJson(response.body()?.string(), Wrapper::class.java)

                wrapper.photos.photo.forEachIndexed { index, photo ->
                    if (index % 5 == 0) {
                        Timber.d("id: ${photo.id}, owner: ${photo.owner}, secret = ${photo.secret}, server = ${photo.server}, farm = ${photo.farm}, title: ${photo.title}, ispublic = ${photo.ispublic}, isfriend = ${photo.isfriend},isfamily= ${photo.isfamily}")
                    }
                }
                links = wrapper.photos.photo.map { photo ->
                    "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg\n"
                }
                runOnUiThread {
                    displayImageList(this, links)
                }
            }
        }.start()
    }

    private fun displayImageList(inputContext: Context, imageUrlList: List<String>) {
        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = PhotoAdapter(inputContext, imageUrlList)
    }
}

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<Photo>
)

data class Wrapper(
    val photos: PhotoPage
)

class PhotoAdapter(private val context: Context, private val photos: List<String>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rViewIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]


        Glide.with(context).load(photo).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Image URL", photo)
            clipboard.setPrimaryClip(clip)
            Timber.i("Image URL copied: $photo")
        }
    }

    override fun getItemCount(): Int = photos.size
}

