package com.practicum.playlist_maker_one.ui.settings.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlist_maker_one.R

private val imageSize = 45.dp
private val imagePaddingVertical = 8.dp
private val minTextSize = 11.sp
private val edgeIconSize = 24.dp

@Composable
fun BuildTracksItem(imageUrl: String?, songName: String, artistName: String, trackDuration: String, onTrackClick: () -> Unit){
    val nameTextColor = colorResource(R.color.adaptiveCommonText)
    val secondaryColor = colorResource(R.color.YP_Text_Gray)

    var isImageLoadSuccessful by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = imagePaddingVertical,
                bottom = imagePaddingVertical,
                start = 13.dp
            )
            .clickable(
                onClick = onTrackClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(imageUrl != null && isImageLoadSuccessful){
            AsyncImage(
                modifier = Modifier
                    .size(width = imageSize, height = imageSize)
                    .clip(RoundedCornerShape(8.dp)),
                model = imageUrl,
                contentDescription = null,
                onSuccess = {
                    isImageLoadSuccessful = true
                },
                onError = {
                    isImageLoadSuccessful = false
                }
            )
        } else{
            Image(
                modifier = Modifier
                    .size(width = imageSize, height = imageSize)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(R.drawable.ic_placeholder_45),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = songName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    color = nameTextColor
                ),
                maxLines = 1
            )
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier.weight(1f,false),
                    text = artistName,
                    style = TextStyle(
                        fontSize = minTextSize,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    ),
                    color = secondaryColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Icon(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(R.drawable.ic_dot_13),
                    tint = secondaryColor,
                    contentDescription = null,
                )
                Text(
                    text = trackDuration,
                    style = TextStyle(
                        fontSize = minTextSize,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    ),
                    color = secondaryColor
                )

            }
        }
        Icon(
            modifier = Modifier
                .size(edgeIconSize)
                .wrapContentSize(Alignment.Center),
            painter = painterResource(R.drawable.ic_arrowright_8),
            contentDescription = null,
            tint = secondaryColor
        )

    }
}

@Preview(showSystemUi = true)
@Composable
fun BuildTracksItemPreview(){
    BuildTracksItem(null, "tim tim", "tim", "0:00", onTrackClick = {})
}