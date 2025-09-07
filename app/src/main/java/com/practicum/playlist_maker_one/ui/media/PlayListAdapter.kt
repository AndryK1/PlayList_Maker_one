package com.practicum.playlist_maker_one.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.ui.search.TrackViewHolder

class PlayListAdapter(
    private val playLists: List<PlayListData>
): RecyclerView.Adapter<PlayListViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_play_list, parent, false)
        return PlayListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PlayListViewHolder,
        position: Int
    ) {
        holder.bind(playLists[position])
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}