package com.example.nestedlayouts

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var value = 1
    private var pressCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        val layoutParams = constraintLayout.layoutParams
        layoutParams.height = (layoutParams.height - 40) // Decrease height by 40 pixels
        constraintLayout.layoutParams = layoutParams


        val button = findViewById<Button>(R.id.rollButton)

        button.setOnClickListener {
            updateTextViews()
        }

    }
    private fun updateTextViews() {
        ++value
        ++pressCount

        if (pressCount >= 3) {
            pressCount = 0
        }

        updateFirstLayout()
        updateSecondLayout()
        updateConstraintLayout()
    }

    private fun updateFirstLayout() {
        val textView1: TextView = findViewById(R.id.ll1TextView1)
        val textView2: TextView = findViewById(R.id.ll1TextView2)
        val textView3: TextView = findViewById(R.id.ll1TextView3)

        when (pressCount){
            0 -> {
                textView1.text = value.toString()
                textView2.text = ""
                textView3.text = ""
            }
            1 -> {
                textView2.text = value.toString()
                textView1.text = ""
                textView3.text = ""
            }
            2 -> {
                textView3.text = value.toString()
                textView2.text = ""
            }
        }
    }

    private fun updateSecondLayout() {
        val textView1: TextView = findViewById(R.id.ll2TextView1)
        val textView2: TextView = findViewById(R.id.ll2TextView2)
        val textView3: TextView = findViewById(R.id.ll2TextView3)

        when (pressCount){
            0 -> {
                textView1.text = value.toString()
                textView2.text = ""
                textView3.text = ""
            }
            1 -> {
                textView2.text = value.toString()
                textView1.text = ""
                textView3.text = ""
            }
            2 -> {
                textView3.text = value.toString()
                textView2.text = ""
            }
        }
    }

    private fun updateConstraintLayout() {
        val textView1: TextView = findViewById(R.id.clTextView1)
        val textView2: TextView = findViewById(R.id.clTextView2)
        val textView3: TextView = findViewById(R.id.clTextView3)

        when (pressCount){
            0 -> {
                textView1.text = value.toString()
                textView2.text = ""
                textView3.text = ""
            }
            1 -> {
                textView2.text = value.toString()
                textView1.text = ""
                textView3.text = ""
            }
            2 -> {
                textView3.text = value.toString()
                textView2.text = ""
            }
        }
    }
}