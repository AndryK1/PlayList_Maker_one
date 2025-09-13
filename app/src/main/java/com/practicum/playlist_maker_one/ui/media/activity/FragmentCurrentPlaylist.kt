package com.practicum.playlist_maker_one.ui.media.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentCurrentPlaylistBinding
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.viewModel.CurrentPlaylistViewModel
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import com.practicum.playlist_maker_one.ui.search.TrackAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.http.GET


class FragmentCurrentPlaylist : Fragment() {

    private lateinit var binding : FragmentCurrentPlaylistBinding
    private val viewModel: CurrentPlaylistViewModel by viewModel()
    private var playList: PlayListData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.leaveButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.moreOptionsBottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.observeTracks().observe(viewLifecycleOwner){
            playList = it
            setView()
        }

        playList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(EXTRA_PLAYLIST, PlayListData::class.java)
        } else {
            @Suppress("DEPRECATION")
            (arguments?.getParcelable(EXTRA_PLAYLIST))
        }

        //слушатель от результата редактирования
        parentFragmentManager.setFragmentResultListener("edit_playlist_request", viewLifecycleOwner){ requestKey, result ->
            if(requestKey == "edit_playlist_request"){
                val updatedPlaylist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.getParcelable("updated_playlist", PlayListData::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.getParcelable("updated_playlist")
                }

                updatedPlaylist?.let {
                    playList = it
                    viewModel.putPlaylist(it)
                    setView()
                }
            }
        }

        viewModel.putPlaylist(playList!!)

        binding.moreOptionsButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.moreOptionsBottomSheet.setOnClickListener {  }// для нейтрализации нажатия на элементы под bottomSheet через него самого

        binding.shareText.setOnClickListener{
            share()
        }

        binding.shareButton.setOnClickListener {
            share()
        }

        binding.editText.setOnClickListener {
            navigateToEdit(playList!!)
        }

        binding.deletePlaylistText.setOnClickListener {
            val playlistAlertMessage = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.wantToDeletePlaylist)
                .setNegativeButton(R.string.no){dialog, which -> }
                .setPositiveButton(R.string.yes)
                { dialog, which ->
                    viewModel.deletePlaylist(playList!!)
                    findNavController().navigateUp()
                }.show()

        }
    }

    private fun makeShareMessage(playlist: PlayListData) : String{

        return buildString {
            append(playlist.name)

            if(playlist.description.isNotEmpty()){
                append(playlist.description)
            }

            if(playlist.tracksCount > 0){
                append(resources.getQuantityString(R.plurals.tracks_count, playlist.tracksCount, playlist.tracksCount))
            }

            playlist.tracks.forEachIndexed { index, track ->
                val number = index + 1
                val artist = track.artistName
                val trackName = track.trackName
                val trackDuration = track.trackFormatedTime

                append("$number. $artist - $trackName ($trackDuration)")
            }
        }
    }

    private fun share(){
        playList?.tracks?.size?.let { it1 ->
            if(it1 > 0){
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        makeShareMessage(playList!!)
                    )
                }
                startActivity(Intent.createChooser(intent, getString(R.string.sharePlayList)))
            }else{
                val message = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.thereIsNoTracks)
                    .setPositiveButton(R.string.ok){dialog, witch ->}
                    .create()

                message.show()

                message.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private fun setView(){
        val paddingInPx = 24.dpToPx(requireContext())

        if(playList?.imageUrl != ""){
            binding.PlaylistImage.setPadding(0,0,0,0)
            Glide.with(this)
                .load(playList?.imageUrl)
                .transform(CenterCrop())
                .into(binding.PlaylistImage)
        }
        else{
            binding.PlaylistImage.setPadding(paddingInPx,paddingInPx,paddingInPx,paddingInPx)
            binding.PlaylistImage.setImageResource(R.drawable.ic_placeholder_45)
        }

        val tracksCount  = playList?.tracksCount ?: 0
        val generalTime = playList!!.tracks.map { it.trackFormatedTime }
        val formatedTime = viewModel.getGeneralTime(generalTime)
        binding.name.text = playList?.name
        if(playList?.tracks?.isEmpty() == true){
            binding.emptyTracksImage.isVisible = true
            binding.noTracksText.isVisible = true
            binding.recyclerView.isVisible = false
        }else{
            binding.emptyTracksImage.isVisible = false
            binding.noTracksText.isVisible = false
            binding.recyclerView.isVisible = true
        }
        binding.description.text = playList?.description
        binding.duration.text = resources.getQuantityString(R.plurals.general_minutes, formatedTime, formatedTime)
        binding.tracksCount.text = resources.getQuantityString(R.plurals.tracks_count, tracksCount, tracksCount)

        binding.recyclerView.adapter = TrackAdapter(
            onItemClick = { track ->
                viewModel.onTrackClicked(track)
                navigateToPlayer(track)
            },
            track = playList?.tracks!!,
            onLongClickListener = { track ->
                deleteTrack(track)
            }
        )
        // bottomSheet moreOptions

        if(playList?.imageUrl != ""){
            binding.playlistBottomIcon.setPadding(0,0,0,0)
            Glide.with(this)
                .load(playList?.imageUrl)
                .transform(CenterCrop())
                .into(binding.playlistBottomIcon)
        }
        else{
            binding.playlistBottomIcon.setImageResource(R.drawable.ic_placeholder_45)
        }

        binding.playlistBottomName.text = playList?.name
        binding.trackBottomCount.text = resources.getQuantityString(R.plurals.tracks_count, tracksCount, tracksCount)
    }

    private fun navigateToPlayer(track : TrackData){
        findNavController().navigate(R.id.action_fragmentCurrentPlaylist_to_audioFragment,
            AudioFragment.createTrack(track))
    }

    private fun navigateToEdit(playlist: PlayListData){
        findNavController().navigate(R.id.action_fragmentCurrentPlaylist_to_fragmentEditPlaylist,
            FragmentEditPlaylist.createPlaylist(playlist))
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun deleteTrack(track: TrackData){
        val deleteAlert = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track_alert))
            .setNegativeButton(R.string.no){ dialog, which -> }
            .setPositiveButton(R.string.yes){dialog, which -> viewModel.deleteTrack(track)}.show()
    }

    companion object{
        private const val EXTRA_PLAYLIST ="extra_playlist"

        fun createPlaylist(playList: PlayListData) : Bundle = bundleOf(EXTRA_PLAYLIST to playList)

    }
}