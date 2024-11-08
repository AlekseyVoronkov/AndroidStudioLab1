package com.example.newactivity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.newactivity.R

class PicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pic_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.piclayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val picLink = intent.getIntExtra("picLink", R.mipmap.pic)

        val imageView = findViewById<ImageView>(R.id.picView)

        Glide.with(this)
            .load(picLink)
            .centerCrop()
            .into(imageView)

        supportActionBar?.title = "Картинка"
    }
}