package com.practicum.playlist_maker_one.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.PlayListViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PlayerAdapter(
    private val playLists: List<PlayListData>,
    private val interactor: PlayListInteractor,
    private val trackId: Long,
    private val coroutineScope: CoroutineScope,
    val onItemClick: (playlistName: String, result : Boolean) -> Unit
) : RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int
    ) {
        holder.bind(playLists[position])

        val playList = playLists[position]
        val isAlreadyHave = playList.tracksIds.any { trackId == it }
        holder.itemView.setOnClickListener {
            try{
                coroutineScope.launch {
                    if(!isAlreadyHave){
                        interactor.addTrackToPlayList(trackId, playList)
                        onItemClick(playList.name, true)
                    }
                    else{
                        onItemClick(playList.name, false)
                    }
                }
            }catch (e: Exception) {
                onItemClick(playList.name, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}