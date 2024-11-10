package com.example.gson

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private val scope = CoroutineScope(Dispatchers.Main)
    lateinit var mStartForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Timber.plant(Timber.DebugTree())
        fetchImages()

        mStartForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK) {
                        val snackbar = Snackbar.make(
                            findViewById(R.id.main),
                            "Картинка добавлена в избранное",
                            Snackbar.LENGTH_LONG
                        )

                        snackbar.setAction("Открыть") {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(result.data?.getStringExtra("favPicLink") ?: "")
                            )
                            startActivity(browserIntent)
                        }
                        snackbar.show()
                    }
                }
            }


    private fun fetchImages() {
        val request = Request.Builder()
            .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
            .build()

        scope.launch(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("Error fetching images")

            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

            val wrapper = gson.fromJson(response.body()?.string(), Wrapper::class.java)

            wrapper.photos.photo.forEachIndexed { index, photo ->
                if (index % 5 == 0) {
                    Timber.d("id: ${photo.id}, owner: ${photo.owner}, secret = ${photo.secret}, server = ${photo.server}, farm = ${photo.farm}, title: ${photo.title}, ispublic = ${photo.ispublic}, isfriend = ${photo.isfriend},isfamily= ${photo.isfamily}")
                }
            }

            val links = wrapper.photos.photo.map { photo ->
                "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"

            }

            withContext(Dispatchers.Main) {
                displayImageList(links)
            }
        }
    }

    private fun displayImageList(imageUrlList: List<String>) {
        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = PhotoAdapter(imageUrlList, this)
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

class PhotoAdapter(private val photos: List<String>, private val listener: MainActivity) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rViewIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val photo = photos[position]
        Glide.with(holder.imageView.context).load(photo).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val clipboard =
                holder.imageView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Image URL", photo)
            clipboard.setPrimaryClip(clip)
            Timber.i("Image URL copied: $photo")

            val intent = Intent(
                it.context,
                PicViewer::class.java
            ).apply {
                putExtra("picLink", photo)

            }
            listener.mStartForResult.launch(intent)
        }
    }

    override fun getItemCount(): Int = photos.size

}
