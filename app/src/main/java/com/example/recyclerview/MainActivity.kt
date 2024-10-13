package com.example.recyclerview

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val colorList: ArrayList<ColorData> = arrayListOf(
            ColorData("Red", "#FF0000"),
            ColorData("Dark Green", "#038d00"),
            ColorData("Orange", "#ff8d00"),
            ColorData("Yellow", "#ffff00"),
            ColorData("Gray", "#212500"),
            ColorData("Blue", "#2125f0"),
            ColorData("Violet", "#451048"),
            ColorData("Black", "#000000")
        )

        val adapter = MyAdapter(this, colorList, this)
        recyclerView.adapter = adapter
    }

    override fun onCellClick(colorName: String) {
        Toast.makeText(this, "IT’S $colorName", Toast.LENGTH_SHORT).show()
    }
}

private fun AddRandomColor(array: ArrayList<ColorData>){
    val limit = array.size
    val random = (0..limit).random()
    array.add(array[random])
}

data class ColorData(
    val colorName: String,
    val colorHex: String
)

interface CellClickListener {
    fun onCellClick(colorName: String)
}

class MyAdapter(
    private val context: Context,
    private val list: ArrayList<ColorData>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var picture: View? = null
        var smallTextView: TextView? = null

        init {
            picture = itemView.findViewById(R.id.picture)
            smallTextView = itemView.findViewById(R.id.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorData = list[position]
        holder.smallTextView?.text = colorData.colorName
        holder.picture?.setBackgroundColor(Color.parseColor(colorData.colorHex))

        // Set the click listener
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClick(colorData.colorName)
        }
    }
}
