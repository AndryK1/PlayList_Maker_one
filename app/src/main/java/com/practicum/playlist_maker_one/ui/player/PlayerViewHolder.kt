package com.practicum.playlist_maker_one.ui.player

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.PlayListData

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image : ImageView = itemView.findViewById(R.id.image)
    val name : TextView = itemView.findViewById(R.id.name)
    val trackCount : TextView = itemView.findViewById(R.id.tracksCount)

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

            Glide.with(itemView.context)
                .load(R.drawable.ic_placeholder_45)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(8)
                    )
                )
                .into(image)
        }
    }
}