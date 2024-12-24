package com.practicum.playlist_maker_one

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private var textInput : String? = null
    private lateinit var editedText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val closeButton = findViewById<ImageButton>(R.id.exitButtonSearch)
        editedText = findViewById(R.id.search_bar)
        val clearInput = findViewById<ImageView>(R.id.clearButton)

        clearInput.visibility = View.GONE

        editedText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearInput.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                textInput = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearInput.setOnClickListener{
            editedText.setText("")
        }

        closeButton.setOnClickListener{
            finish()
        }
        if (savedInstanceState != null){
            editedText.setText(savedInstanceState.getString("searchText"))
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", textInput)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editedText.setText(savedInstanceState.getString("searchText"))
    }
}