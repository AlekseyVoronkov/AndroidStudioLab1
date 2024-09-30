package com.example.complexevent

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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

        val editText = findViewById<EditText>(R.id.UserEditText)
        val button = findViewById<Button>(R.id.SaveButton)
        val textView = findViewById<TextView>(R.id.TextView)
        val progressBar = findViewById<ProgressBar>(R.id.ProgressBar)

        button.setOnClickListener {
            val checkBox = findViewById<CheckBox>(R.id.SaveCheckBox)

            if (checkBox.isChecked) {
                progressBar.progress += progressBar.max / 10
                textView.text = editText.text

            }
        }
    }
}
