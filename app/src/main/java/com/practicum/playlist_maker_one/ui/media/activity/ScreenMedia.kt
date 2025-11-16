package com.practicum.playlist_maker_one.ui.media.activity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.FavoritesState
import com.practicum.playlist_maker_one.ui.media.PlayListState
import com.practicum.playlist_maker_one.ui.media.viewModel.FavoritesViewModel
import com.practicum.playlist_maker_one.ui.media.viewModel.PlayListViewModel
import com.practicum.playlist_maker_one.ui.settings.activity.BuildTracksItem
import kotlinx.coroutines.launch


private val imageSize = 45.dp
private val imagePaddingVertical = 8.dp
private val minPadding = 8.dp
private val minTextSize = 11.sp
private val edgeIconSize = 24.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaActivity(
    favoritesViewModel: FavoritesViewModel? = null,
    playListViewModel: PlayListViewModel,
    onTrackClick: (TrackData) -> Unit,
    onNewPlayListClick: () -> Unit,
    onPlayListClick: (PlayListData) -> Unit
) {
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(
        stringResource(R.string.favorites),
        stringResource(R.string.playLists)
    )

    Scaffold(
        modifier = Modifier.background(backgroundColor),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(backgroundColor),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor,
                    actionIconContentColor = textColor
                ),
                title = {
                    Text(
                        text = stringResource(R.string.mediateka),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 14.dp, start = 16.dp),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(
                                Font(R.font.ys_text_medium)
                            )
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {

            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = backgroundColor,
                contentColor = textColor,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                                    fontSize = 14.sp
                                )
                            )
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        FavoritesScreen(
                            viewModel = favoritesViewModel,
                            onTrackClick = {track ->
                                onTrackClick(track) }
                        )
                    }
                    1 -> {
                        PlayListScreen(
                            viewModel = playListViewModel,
                            onButtonClick = { onNewPlayListClick() },
                            onPlayListClick = {playList ->  onPlayListClick(
                                playList
                            )}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayListScreen(viewModel : PlayListViewModel ,onButtonClick : () -> Unit, onPlayListClick: (PlayListData) -> Unit){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)

    LaunchedEffect(key1 = viewModel) {
        viewModel.loadPlaylists()
    }

    val state by viewModel.stateLiveData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            onClick = { onButtonClick() },
            modifier = Modifier
                .wrapContentSize()
                .padding(24.dp),
            text = stringResource(R.string.newPlayList)
        )

        when (state) {
            is PlayListState.Content -> {
                val playLists = (state as PlayListState.Content).playLists

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 колонки
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(playLists) { playList ->
                        PlaylistItem(
                            imageUrl = playList.imageUrl,
                            playListName = playList.name,
                            trackCount = "${playList.tracksCount} треков",
                            onPlaylistClick = {
                                onPlayListClick(playList)
                            }
                        )
                    }
                }
            }
            is PlayListState.NothingFound -> {

                ShowNothingFoundMessage(stringResource(R.string.noPlayListsText))
            }
            null -> {
                ShowNothingFoundMessage(stringResource(R.string.noPlayListsText))
            }
        }
    }
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text : String
) {
    val textColor = colorResource(R.color.adaptiveBackGroundWhite)

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.adaptiveCommonText)
        ),
        shape = RoundedCornerShape(54.dp)
    ) {
        Text(
            text = text,
            fontSize = dimensionResource(R.dimen.refreshText).value.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = textColor
        )
    }
}

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel? = null, onTrackClick : (TrackData) -> Unit){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    //загружаем данные при отображении
    LaunchedEffect(key1 = viewModel) {
        viewModel?.loadLikedTracks()
    }

    val state = if (viewModel != null) {
        viewModel.stateLiveData.collectAsState().value
    } else {
        remember { mutableStateOf<FavoritesState?>(null) }.value
    }

    when (state) {
        is FavoritesState.Content -> {
            val tracks = state.tracks
            if (tracks.isEmpty()) {
                ShowNothingFoundMessage(stringResource(R.string.emptyMedia))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(4.dp)
                ) {
                    items(tracks) { track ->
                        BuildTracksItem(
                            imageUrl = track.formatedArtworkUrl100,
                            songName = track.trackName,
                            artistName = track.artistName,
                            trackDuration = track.trackFormatedTime,
                            onTrackClick = {
                                onTrackClick(track)
                            }
                        )
                    }
                }
            }
        }
        is FavoritesState.NothingFound -> {
            ShowNothingFoundMessage(stringResource(R.string.emptyMedia))
        }
        null -> {
            ShowNothingFoundMessage(stringResource(R.string.emptyMedia))
        }
    }
}

@Composable
fun ShowNothingFoundMessage(text: String){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 106.dp)
            .background(backgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(dimensionResource(R.dimen.errorImageHeight).value.dp)
                .width(dimensionResource(R.dimen.errorImageHeight).value.dp),
            painter = painterResource(R.drawable.ic_nothing_found_120),
            contentDescription = text
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = text,
            style = TextStyle(
                color = textColor,
                fontFamily = FontFamily(Font(R.font.ys_text_medium)),
                fontSize = dimensionResource(R.dimen.errorTextSize).value.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}


