package com.practicum.playlist_maker_one.ui.search.activity

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.query
import coil.compose.AsyncImage
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.search.SearchState
import com.practicum.playlist_maker_one.ui.search.view_model.SearchViewModel
import com.practicum.playlist_maker_one.ui.settings.activity.BuildTracksItem
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent

private val minPadding = 8.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchActivity(
    viewModel: SearchViewModel? = null,
    onTrackClick: (TrackData) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    // не удалять
    val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    val savedQuery = viewModel?.getLastQuery() ?: ""
    var query by remember { mutableStateOf(savedQuery) }

    val currentState by viewModel?.stateLiveData?.collectAsState() ?: remember {
        mutableStateOf(SearchState.History(emptyList()))
    }


    LaunchedEffect(Unit) {
        if (savedQuery.isEmpty()) {
            viewModel?.loadHistory()
        }
    }


    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            delay(1000)
            viewModel?.searchDebounce(query)
        } else if (query.isEmpty() && savedQuery.isNotEmpty()) {

            viewModel?.loadHistory()
        }
    }

    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    Scaffold(
        modifier = Modifier.background(backgroundColor)
            .systemBarsPadding(),
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
                        text = stringResource(R.string.search),
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
                .imeNestedScroll()
        ) {

            Box(
                modifier = Modifier
                    .padding(horizontal = minPadding, vertical = minPadding)
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(
                        color = colorResource(R.color.searchBg),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_16),
                        contentDescription = null,
                        tint = colorResource(R.color.TextGrey_Black),
                        modifier = Modifier
                            .padding(end = dimensionResource(R.dimen.paddingSearch).value.dp, start = dimensionResource(R.dimen.paddingSearch).value.dp)
                    )

                    BasicTextField(
                        value = query,
                        onValueChange = { newQuery ->
                            query = newQuery
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        textStyle = TextStyle(
                            color = colorResource(R.color.black),
                            fontSize = dimensionResource(R.dimen.textNormalSize).value.sp,
                            fontFamily = FontFamily(Font(R.font.ys_display_regular))
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                viewModel?.searchDebounce(query)
                            },
                            onSearch = {
                                keyboardController?.hide()
                                viewModel?.searchDebounce(query)
                            }
                        ),
                        cursorBrush = SolidColor(colorResource(R.color.YP_blue)),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (query.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.search),
                                        color = colorResource(R.color.TextGrey_Black),
                                        fontSize = dimensionResource(R.dimen.textNormalSize).value.sp,
                                        fontFamily = FontFamily(Font(R.font.ys_display_regular))
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (query.isNotEmpty()) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clear_16),
                            contentDescription = null,
                            tint = colorResource(R.color.TextGrey_Black),
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.paddingSearch).value.dp)
                                .clickable {
                                    query = ""
                                    viewModel?.searchDebounce("")
                                }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                currentState?.let { state ->
                    RenderState(
                        state = state,
                        onClickInternet = { viewModel?.searchDebounce(query) },
                        onClickHistory = { viewModel?.historyClear() },
                        onTrackClick = { track ->
                            viewModel?.onTrackClicked(track) // Сохраняем в историю
                            onTrackClick(track)
                        }
                    )
                }
            }
        }
    }
}
@Composable
private fun RenderState(state: SearchState, onClickInternet: () -> Unit, onClickHistory: () -> Unit, onTrackClick: (TrackData) -> Unit){
    when(state){
        is SearchState.Loading -> ShowLoading()
        is SearchState.Content -> {
            ShowTrackList(state.tracks, onTrackClick = onTrackClick)
        }
        is SearchState.NothingFound -> ShowNothingFoundMessage()
        is SearchState.InternetError -> ShowInternetErrorMessage(
            onClick = onClickInternet,
        )
        is SearchState.History -> {
            if(state.history.isNotEmpty()){
                ShowHistory(state.history, onClick = onClickHistory,
                    onTrackClick = onTrackClick)
            }
        }
    }
}

@Composable
fun ShowLoading(){

    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = colorResource(R.color.YP_blue),
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ShowHistory(history : List<TrackData>, onClick: () -> Unit, onTrackClick: (TrackData) -> Unit){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(backgroundColor)
                .padding(
                    start = dimensionResource(R.dimen.borders).value.dp,
                    end = dimensionResource(R.dimen.borders).value.dp,
                    top = 20.dp
                ),
            text = stringResource(R.string.searchHistory),
            style = TextStyle(
                fontSize = dimensionResource(R.dimen.historyTextSize).value.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                color = textColor
            ),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 12.dp)
                .background(backgroundColor)
        ) {
            items(
                items = history
            ) { track ->
                BuildTracksItem(
                    imageUrl = track.formatedArtworkUrl100,
                    songName = track.trackName,
                    artistName = track.artistName ?: stringResource(R.string.nothing_found),
                    trackDuration = track.trackFormatedTime ?: "0:00",
                    onTrackClick = { onTrackClick(track) }
                )
            }
        }

        CustomButton(
            onClick = onClick,
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp),
            text = stringResource(R.string.clearHistory)
        )
    }
}

@Composable
fun ShowInternetErrorMessage(onClick : () -> Unit){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 174.dp)
            .background(backgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(dimensionResource(R.dimen.errorImageHeight).value.dp)
                .width(dimensionResource(R.dimen.errorImageHeight).value.dp),
            painter = painterResource(R.drawable.ic_on_failure_music_120),
            contentDescription = stringResource(R.string.check_internet)
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "${stringResource(R.string.something_went_wrong )} \n\n ${stringResource(R.string.check_internet )}",
            style = TextStyle(
                color = textColor,
                fontFamily = FontFamily(Font(R.font.ys_text_medium)),
                fontSize = dimensionResource(R.dimen.errorTextSize).value.sp,
                textAlign = TextAlign.Center
            )
        )

        CustomButton(
            onClick = { onClick() },
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 58.dp),
            text = stringResource(R.string.refresh),
        )
    }
}


@Composable
fun ShowNothingFoundMessage(){
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 174.dp)
            .background(backgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .height(dimensionResource(R.dimen.errorImageHeight).value.dp)
                .width(dimensionResource(R.dimen.errorImageHeight).value.dp),
            painter = painterResource(R.drawable.ic_nothing_found_120),
            contentDescription = stringResource(R.string.nothing_found)
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.nothing_found),
            style = TextStyle(
                color = textColor,
                fontFamily = FontFamily(Font(R.font.ys_text_medium)),
                fontSize = dimensionResource(R.dimen.errorTextSize).value.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun ShowTrackList(tracks: List<TrackData>, onTrackClick: (TrackData) -> Unit){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        items(
             items =  tracks
        ){ track ->
            BuildTracksItem(
                imageUrl = track.formatedArtworkUrl100,
                songName = track.trackName,
                artistName = track.artistName ?: stringResource(R.string.nothing_found),
                trackDuration = track.trackFormatedTime ?: "0:00",
                onTrackClick = { onTrackClick(track) }
            )
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

@Preview
@Composable
fun SearchActivityPreview(){
    SearchActivity(onTrackClick = {})
}

@Preview(showSystemUi = true)
@Composable
fun ShowNothingFoundMessagePreview(){
    ShowNothingFoundMessage()
}

@Preview(showSystemUi = true)
@Composable
fun ShowInternetErrorMessagePreview(){
    ShowInternetErrorMessage(onClick = {})
}
