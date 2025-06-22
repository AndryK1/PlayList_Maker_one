package com.practicum.playlist_maker_one.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.ActivitySearchBinding
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.search.SearchState
import com.practicum.playlist_maker_one.ui.search.TrackAdapter
import com.practicum.playlist_maker_one.ui.search.TrackHistoryAdapter
import com.practicum.playlist_maker_one.ui.search.view_model.SearchViewModel


const val DELAYED = 1000L

class SearchActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var binding: ActivitySearchBinding
    private var viewModel : SearchViewModel? = null
    private var lastSearchQuery: String = ""

    private val adapter = TrackAdapter(this, emptyList())
    private val historyAdapter = TrackHistoryAdapter(this, emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getFactory()).get(
            SearchViewModel::class.java)

        viewModel?.observeState()?.observe(this){
            renderState(it)
        }

        context = this

        val recyclerView = binding.recyclerViewSearch
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

        binding.clearButton.visibility = View.GONE
        viewModel?.loadHistory()
        showHistory()

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                lastSearchQuery = s?.toString() ?: ""
                viewModel?.searchDebounce(lastSearchQuery)
            }

        })

        binding.clearButton.setOnClickListener{
            viewModel?.searchClear()
            binding.searchBar.setText("")
            hideKeyboard(it)
            allViewGone()
            recyclerView.visibility = View.GONE
        }

        binding.historyClear.setOnClickListener{
            viewModel?.historyClear()
            allViewGone()
            historyAdapter.notifyDataSetChanged()
        }

        binding.refreshButton.setOnClickListener{
            viewModel?.searchDebounce(lastSearchQuery)
        }

        findViewById<MaterialToolbar>(R.id.searchToolbar).setNavigationOnClickListener {
            finish()
        }
        if (savedInstanceState != null){
            binding.searchBar.setText(savedInstanceState.getString("searchText"))
        }

    }

    private fun renderState(state: SearchState){
        when(state){
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> {
                adapter.updateData(state.movies)
                showContent()
            }
            is SearchState.NothingFound -> showNothingFoundMessage()
            is SearchState.InternetError -> showErrorMessage()
            is SearchState.History -> {
                if(state.history.isEmpty()){
                    allViewGone()
                }
                else{
                    historyAdapter.updateData(state.history)
                    showHistory()
                }
            }
        }
    }

    fun allViewGone() {
        binding.apply {
            progressBar.visibility = View.GONE
            nothingFoundText.visibility = View.GONE
            nothingFoundImage.visibility = View.GONE
            internetProblemText.visibility = View.GONE
            internetProblemImage.visibility = View.GONE
            refreshButton.visibility = View.GONE
            historySearchText.visibility = View.GONE
            historyClear.visibility = View.GONE
        }
        binding.historyRecyclerView.visibility = View.GONE
        binding.recyclerViewSearch.visibility = View.GONE
    }

    private fun showLoading(){
        allViewGone()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent(){
        allViewGone()
        binding.recyclerViewSearch.visibility = View.VISIBLE
    }

    private fun showNothingFoundMessage() {
        allViewGone()
        binding.apply {
            nothingFoundText.visibility = View.VISIBLE
            nothingFoundImage.visibility = View.VISIBLE
        }

    }

    private fun showErrorMessage(){
        allViewGone()

        val text1 = getString(R.string.something_went_wrong) + "\n\n"
        val text2 = getString(R.string.check_internet)

        val spannableString =
            SpannableString(text1 + text2).apply {}//форматирование текста внутри textView
        binding.apply {
            internetProblemText.text = spannableString
            internetProblemText.visibility = View.VISIBLE
            internetProblemImage.visibility = View.VISIBLE
            refreshButton.visibility = View.VISIBLE
        }
    }

    private fun showHistory(){
        allViewGone()
        binding.apply {
            historyRecyclerView.visibility = View.VISIBLE
            historyClear.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", lastSearchQuery)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchBar.setText(savedInstanceState.getString("searchText"))
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}