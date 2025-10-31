package com.practicum.playlist_maker_one.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentSearchBinding
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.main.activity.RootActivity
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import com.practicum.playlist_maker_one.ui.search.SearchState
import com.practicum.playlist_maker_one.ui.search.TrackAdapter
import com.practicum.playlist_maker_one.ui.search.TrackHistoryAdapter
import com.practicum.playlist_maker_one.ui.search.view_model.SearchViewModel
import org.koin.android.ext.android.inject

import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSearch : Fragment() {

    companion object{

        const val DELAYED = 1000L
    }
    private lateinit var context: Context
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel(ownerProducer = { requireActivity() })
    private var lastSearchQuery: String = ""

    private lateinit var adapter : TrackAdapter
    private lateinit var historyAdapter : TrackHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as RootActivity).registerInternetReceiver()
    }

    override fun onStop() {
        (requireActivity() as RootActivity).unregisterInternetReceiver()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner){
            renderState(it)
        }

        context = requireActivity()

        adapter = TrackAdapter(
            onItemClick = { track ->
                viewModel.onTrackClicked(track)
                navigateToPlayer(track)
            },
            track = emptyList()
        )
        historyAdapter = TrackHistoryAdapter(
            onItemClick = {track -> viewModel.onTrackClicked(track)
                navigateToPlayer(track) }, emptyList()
        )

        val recyclerView = binding.recyclerViewSearch
        recyclerView.adapter = adapter

        //
        val historyRecyclerView = binding.historyRecyclerView
        historyRecyclerView.adapter = historyAdapter
        //

        binding.clearButton.visibility = View.GONE
        binding.searchBar.setText(viewModel.getLastQuery())
        renderState(viewModel.getLastState())

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                lastSearchQuery = s?.toString() ?: ""
                viewModel.searchDebounce(lastSearchQuery)
            }

        })

        binding.clearButton.setOnClickListener{
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


        if (savedInstanceState != null){
            binding.searchBar.setText(savedInstanceState.getString("searchText"))
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", binding.searchBar.text.toString())
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

    private fun navigateToPlayer(track : TrackData){
        findNavController().navigate(R.id.action_fragmentSearch_to_audioFragment,
            AudioFragment.createTrack(track))
    }

    private fun allViewGone() {
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
            historySearchText.visibility = View.VISIBLE
            historyRecyclerView.visibility = View.VISIBLE
            historyClear.visibility = View.VISIBLE
        }
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
