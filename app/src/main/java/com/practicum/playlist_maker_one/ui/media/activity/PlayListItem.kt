package com.practicum.playlist_maker_one.ui.media.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlist_maker_one.R


@Composable
fun PlaylistItem(
    imageUrl: String?,
    playListName: String,
    trackCount: String,
    onPlaylistClick: () -> Unit
) {
    val nameTextColor = colorResource(R.color.adaptiveCommonText)
    val secondaryColor = colorResource(R.color.YP_Text_Gray)

    var isImageLoadSuccessful by remember { mutableStateOf(true) }

    val textStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
        fontSize = 12.sp,
        color = nameTextColor
    )

    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = onPlaylistClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(164.dp)
                .padding(
                    start = 4.dp,
                    top = dimensionResource(R.dimen.minMargin).value.dp,
                    end = 4.dp
                )
        ) {
            if (imageUrl != null && isImageLoadSuccessful) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    model = imageUrl,
                    contentDescription = null,
                    onSuccess = {
                        isImageLoadSuccessful = true
                    },
                    onError = {
                        isImageLoadSuccessful = false
                    }
                )
            }else{
            Image(
                modifier = Modifier
                    .size(104.dp)
                    .align(Alignment.Center),
                painter = painterResource(R.drawable.ic_placeholder_45),
                contentDescription = null
            )
            }

        }

        Text(
            modifier = Modifier
                .width(160.dp)
                .padding(horizontal = 4.dp),
            text = playListName,
            style = textStyle,
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .width(160.dp)
                .padding(
                    start = 4.dp,
                    end = 4.dp,
                    bottom = dimensionResource(R.dimen.minMargin).value.dp
                ),
            text = trackCount,
            style = textStyle,
            maxLines = 1
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PlaylistItemPreview() {
    PlaylistItem(
        imageUrl = null,
        playListName = "My Playlist",
        trackCount = "15 треков",
        onPlaylistClick = {}
    )
}