package com.practicum.playlist_maker_one.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlist_maker_one.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor


const val DELAYED = 1000L

class SearchActivity : AppCompatActivity() {
    private var textInput: String? = null
    private lateinit var editedText: EditText
    private val trackManager = Creator.getTrackManager()
    private lateinit var trackInteractor: TrackRepositoryInteractor
    private val mapper = Creator.getMapper()
    private var listOfSongs: ArrayList<TrackData> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var context: Context


    private val adapter = TrackAdapter(this, listOfSongs)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        context = this
        trackManager.initializeHistory(this)
        trackInteractor = Creator.provideTrackUseCase(
            Creator.provideTrackRepository(
                Creator.provideNetworkClient()
            )
        )

        val historyAdapter = TrackHistoryAdapter(
            this,
            trackManager.getTrackHistory().map { mapper.map(it) })//из дто в domain

        val nothing: TextView = findViewById(R.id.nothingFoundText)
        val errorText: TextView = findViewById(R.id.internetProblemText)
        val nothingImage: ImageView = findViewById(R.id.nothingFoundImage)
        val internetError: ImageView = findViewById(R.id.internetProblemImage)
        val refreshButton: Button = findViewById(R.id.refreshButton)
        //
        val searchHistoryText: TextView = findViewById(R.id.historySearchText)
        val clearHistory: Button = findViewById(R.id.historyClear)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        //
        fun allViewGone() {
            nothing.visibility = View.GONE
            errorText.visibility = View.GONE
            nothingImage.visibility = View.GONE
            internetError.visibility = View.GONE
            refreshButton.visibility = View.GONE
        }

        fun historyGone() {
            searchHistoryText.visibility = View.GONE
            clearHistory.visibility = View.GONE
        }

        allViewGone()

        if (trackManager.getTrackHistory().isEmpty()) {
            historyGone()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearch)
        recyclerView.adapter = adapter
        fun historyVisible() {
            searchHistoryText.visibility = View.VISIBLE
            clearHistory.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
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

        fun nothingFoundMessage() {
            recyclerView.visibility = View.GONE
            nothing.visibility = View.VISIBLE
            nothingImage.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            internetError.visibility = View.GONE
            refreshButton.visibility = View.GONE

        }

        fun search() {
            val query = editedText.text.toString()
            if (query.isNullOrEmpty()) {
                listOfSongs.clear()
                allViewGone()
                recyclerView.visibility = View.GONE
                adapter.notifyDataSetChanged()
            } else {
                allViewGone()
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                trackInteractor.execute(query) { result ->
                    runOnUiThread {
                   result.onSuccess { trackList ->
                       progressBar.visibility = View.GONE
                       listOfSongs.clear()
                       if (trackList.isNotEmpty() ) {
                           listOfSongs.addAll(trackList)
                           recyclerView.visibility = View.VISIBLE
                           allViewGone()
                       } else {
                           listOfSongs.clear()
                           nothingFoundMessage()
                       }
                       adapter.notifyDataSetChanged()
                   }
                    result.onFailure {
                        progressBar.visibility = View.GONE
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
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }

        val searchRunnable = Runnable { search() }

        fun startSearch(){
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed ( searchRunnable, DELAYED )
        }

        editedText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearInput.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                if(editedText.text.isNullOrEmpty()){
                    allViewGone()
                    recyclerView.visibility = View.GONE
                    trackInteractor.destroy()
                    val currentHistory = trackManager.getTrackHistory()
                    if(currentHistory.isNotEmpty()) {
                        historyVisible()
                        historyRecyclerView.visibility = View.VISIBLE
                        historyAdapter.updateData(currentHistory.map { mapper.map(it) })
                    } else {
                        historyGone()
                        historyRecyclerView.visibility = View.GONE
                    }
                } else {
                    startSearch()
                    historyGone()
                    historyRecyclerView.visibility = View.GONE
                }
            }

        })

        clearInput.setOnClickListener{
            trackInteractor.destroy()
            editedText.setText("")
            hideKeyboard(it)
            allViewGone()
            recyclerView.visibility = View.GONE

        }

        clearHistory.setOnClickListener{
            trackManager.deliteHistory(this)
            Creator.getSharedPrefs().saveHistory(trackManager.getTrackHistory(), this)
            historyGone()
            historyRecyclerView.visibility = View.GONE
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