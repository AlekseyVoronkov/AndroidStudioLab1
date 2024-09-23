package com.example.attributes

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val editText = findViewById<EditText>(R.id.editText)
        val buttonBlack = findViewById<Button>(R.id.btnBlackText)
        val buttonRed = findViewById<Button>(R.id.btnRedText)
        val buttonSize8 = findViewById<Button>(R.id.btnSizeEight)
        val buttonSize24 = findViewById<Button>(R.id.btnSize8x3)
        val buttonWhiteBG = findViewById<Button>(R.id.btnWhiteBackground)
        val buttonYellowBG = findViewById<Button>(R.id.btnYellowBackground)

        buttonBlack.setOnClickListener {
            editText.setTextColor(Color.BLACK)
        }

        buttonRed.setOnClickListener {
            editText.setTextColor(Color.RED)
        }

        buttonSize8.setOnClickListener {
            editText.textSize = 8f
        }

        buttonSize24.setOnClickListener {
            editText.textSize = 24f
        }

        buttonWhiteBG.setOnClickListener {
            editText.setBackgroundColor(Color.WHITE)
        }

        buttonYellowBG.setOnClickListener {
            editText.setBackgroundColor(Color.YELLOW)
        }
    }
}