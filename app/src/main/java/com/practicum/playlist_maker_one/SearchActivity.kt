package com.practicum.playlist_maker_one

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private val adapter = TrackAdapter(this, listOfSongs)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        TrackHistoryManager.initializeHistory(this)
        val historyAdapter = TrackHistoryAdapter(this, TrackHistoryManager.getTrackHistory())

        val nothing: TextView = findViewById(R.id.nothingFoundText)
        val errorText: TextView = findViewById(R.id.internetProblemText)
        val nothingImage: ImageView = findViewById(R.id.nothingFoundImage)
        val internetError: ImageView = findViewById(R.id.internetProblemImage)
        val refreshButton: Button = findViewById(R.id.refreshButton)
        //
        val searchHistoryText: TextView = findViewById(R.id.historySearchText)
        val clearHistory: Button = findViewById(R.id.historyClear)
        //
        fun allViewGone()
        {
            nothing.visibility = View.GONE
            errorText.visibility = View.GONE
            nothingImage.visibility = View.GONE
            internetError.visibility = View.GONE
            refreshButton.visibility = View.GONE
        }

        fun historyGone()
        {
            searchHistoryText.visibility = View.GONE
            clearHistory.visibility = View.GONE
        }
        fun historyVisible()
        {
            searchHistoryText.visibility = View.VISIBLE
            clearHistory.visibility = View.VISIBLE
        }
        allViewGone()

        if(TrackHistoryManager.getTrackHistory().isEmpty())
        {
            historyGone()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearch)
        recyclerView.adapter = adapter
        //
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecyclerView.adapter = historyAdapter
        //
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
                if(s.isNullOrEmpty()){
                    if(TrackHistoryManager.getTrackHistory().isNotEmpty())
                    {
                        historyVisible()
                        historyRecyclerView.visibility = View.VISIBLE
                        historyAdapter.notifyDataSetChanged()
                    }
                    else
                    {
                        historyGone()
                        historyRecyclerView.visibility = View.GONE
                    }
                }
                else{
                    historyGone()
                    historyRecyclerView.visibility = View.GONE
                }
            }

        })

        fun search() {
            if (editedText.toString().isNullOrEmpty()) {
                listOfSongs.clear()
                allViewGone()
                recyclerView.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
            else{
                itunesService.search(editedText.text.toString()).enqueue(object :
                    Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            listOfSongs.clear()
                            val results = response.body()?.results ?: emptyList()
                            if (results.isNotEmpty() ) {
                                listOfSongs.addAll(results)
                                recyclerView.visibility = View.VISIBLE
                                allViewGone()
                            } else {
                                listOfSongs.clear()
                                recyclerView.visibility = View.GONE
                                nothing.visibility = View.VISIBLE
                                nothingImage.visibility = View.VISIBLE
                                errorText.visibility = View.GONE
                                internetError.visibility = View.GONE
                                refreshButton.visibility = View.GONE
                            }
                            adapter.notifyDataSetChanged()
                        }
                        else
                        {
                            listOfSongs.clear()
                            recyclerView.visibility = View.GONE
                            nothing.visibility = View.VISIBLE
                            nothingImage.visibility = View.VISIBLE
                            errorText.visibility = View.GONE
                            internetError.visibility = View.GONE
                            refreshButton.visibility = View.GONE
                            adapter.notifyDataSetChanged()
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
                        nothing.visibility = View.GONE
                        nothingImage.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        errorText.visibility = View.VISIBLE
                        internetError.visibility = View.VISIBLE
                        refreshButton.visibility = View.VISIBLE
                    }

                })
            }
        }

        editedText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(editedText.toString().isNotEmpty())
                {
                    search()
                    true
                }
                true
            }
            false
        }

        clearInput.setOnClickListener{
            editedText.setText("")
            hideKeyboard(it)
            allViewGone()
            recyclerView.visibility = View.GONE

        }

        clearHistory.setOnClickListener{
            TrackHistoryManager.deliteHistory(this)
            historyGone()
            historyAdapter.notifyDataSetChanged()
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