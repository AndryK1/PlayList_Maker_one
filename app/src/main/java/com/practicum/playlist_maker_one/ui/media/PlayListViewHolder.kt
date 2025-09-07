package com.practicum.playlist_maker_one.ui.media

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.practicum.playlist_maker_one.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.domain.entity.PlayListData

class PlayListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    val placeholderImage : ImageView = itemView.findViewById(R.id.placeholderImage)
    val image : ImageView = itemView.findViewById(R.id.image)
    val name : TextView = itemView.findViewById(R.id.playListName)
    val trackCount : TextView = itemView.findViewById(R.id.trackCount)

    fun bind(playList : PlayListData){

        if(playList.imageUrl == ""){
            setImageVisible(false, playList.imageUrl)
        }
        else{
            setImageVisible(true, playList.imageUrl)
        }

        name.text = playList.name
        trackCount.text = "${playList.tracksCount} треков"
    }

    private fun setImageVisible(state: Boolean, imagePath: String){
        if(state){
            placeholderImage.visibility = View.GONE
            image.visibility = View.VISIBLE
            Glide.with(itemView.context)
                .load(imagePath)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(8)
                    )
                )
                .into(image)
        }
        else{
            placeholderImage.visibility = View.VISIBLE
            image.visibility = View.GONE
        }
    }
}