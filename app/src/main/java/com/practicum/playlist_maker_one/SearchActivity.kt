package com.practicum.playlist_maker_one

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//
class SearchActivity : AppCompatActivity() {
    private var textInput : String? = null
    private lateinit var editedText: EditText
    private var listOfSongs : ArrayList<TrackData> = ArrayList()
    private val itunesBaseUrl = "https://itunes.apple.com"



    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(itunesAPI::class.java)
    private val adapter = TrackAdapter(listOfSongs)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val nothing: TextView = findViewById(R.id.nothingFoundText)
        val errorText: TextView = findViewById(R.id.internetProblemText)
        val nothingImage: ImageView = findViewById(R.id.nothingFoundImage)
        val internetError: ImageView = findViewById(R.id.internetProblemImage)
        val refreshButton: Button = findViewById(R.id.refreshButton)
        nothing.visibility = View.GONE
        errorText.visibility = View.GONE
        nothingImage.visibility = View.GONE
        internetError.visibility = View.GONE
        refreshButton.visibility = View.GONE

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearch)
        recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        editedText = findViewById(R.id.search_bar)
        val clearInput = findViewById<ImageView>(R.id.clearButton)
        clearInput.visibility = View.GONE

        editedText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearInput.isVisible = !s.isNullOrEmpty()


            }

            override fun afterTextChanged(s: Editable?) {

                if (s.isNullOrEmpty()) {
                    listOfSongs.clear()
                    nothing.visibility = View.GONE
                    nothingImage.visibility = View.GONE
                    errorText.visibility = View.GONE
                    internetError.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                    return
                }
                else if (editedText.text.isNotEmpty()) {
                    search()
                }

            }


            fun search() {
                itunesService.search(editedText.text.toString()).enqueue(object :
                    Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        when (response.code()) {
                            200 -> {
                                listOfSongs.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    listOfSongs.addAll(response.body()?.results ?: emptyList())
                                    recyclerView.visibility = View.VISIBLE
                                    nothing.visibility = View.GONE
                                    errorText.visibility = View.GONE
                                    nothingImage.visibility = View.GONE
                                    internetError.visibility = View.GONE
                                    refreshButton.visibility = View.GONE
                                    adapter.notifyDataSetChanged()
                                }
                            }

                            else -> {
                                recyclerView.visibility = View.GONE
                                nothing.visibility = View.VISIBLE
                                nothingImage.visibility = View.VISIBLE
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        val textView: TextView = findViewById(R.id.internetProblemText)

                        val text1 = getString(R.string.something_went_wrong) + "\n\n"
                        val text2 = getString(R.string.check_internet)

                        val spannableString =
                            SpannableString(text1 + text2).apply {}//форматирование текста внутри textView
                        refreshButton.setOnClickListener{search()}
                        textView.text = spannableString
                        recyclerView.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        internetError.visibility = View.VISIBLE
                        refreshButton.visibility = View.VISIBLE
                    }

                })
            }
        })



        clearInput.setOnClickListener{
            editedText.setText("")
            hideKeyboard(it)
        }

        findViewById<MaterialToolbar>(R.id.searchToolbar).setNavigationOnClickListener {
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}