package com.example.mydialer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import timber.log.Timber.Forest.plant

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: ContactsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private var contactsList: List<Contact> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rView)
        searchEditText = findViewById(R.id.et_search)
        searchButton = findViewById(R.id.btn_search)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchButton.setOnClickListener {
            performSearch()
        }
        plant(Timber.DebugTree())
        fetchContacts()
    }

    private fun performSearch() {
        val query = searchEditText.text.toString()
        val filteredContacts = if (query.isEmpty()) {
            contactsList
        } else {
            contactsList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true)
            }
        }
        myAdapter.updateContacts(filteredContacts)
    }


    private fun fetchContacts() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&=download")
            .build()

        Thread {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Error fetching data")

                    val responseBody = response.body()?.string()
                    val gson = GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()

                    val contacts: List<Contact> = gson.fromJson(responseBody, Array<Contact>::class.java).toList()
                    contacts.forEach { contact ->
                        Timber.d("name: ${contact.name}, phone: ${contact.phone}, type: ${contact.type}")
                    }
                    runOnUiThread {
                        myAdapter = ContactsAdapter(this, contacts)
                        recyclerView.adapter = myAdapter
                    }
                    contactsList = contacts
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching contacts")
            }
        }.start()
    }
}

data class Contact(
    val name: String,
    val phone: String,
    val type: String
)

class ContactsAdapter(private val context: Context, private var contacts: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textPhone: TextView = itemView.findViewById(R.id.textPhone)
        val textType: TextView = itemView.findViewById(R.id.textType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.textName.text = contacts[position].name
        holder.textPhone.text = contacts[position].phone
        holder.textType.text = contacts[position].type
    }

    override fun getItemCount(): Int = contacts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
