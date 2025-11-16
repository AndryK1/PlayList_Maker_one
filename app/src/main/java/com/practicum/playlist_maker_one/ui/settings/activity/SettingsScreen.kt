package com.practicum.playlist_maker_one.ui.settings.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import androidx.core.net.toUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsActivity(
    context: Context = LocalContext.current,
    viewModel : SettingsViewModel
){


    val nightModeState = viewModel.nightStateLiveData.collectAsState().value
    val sharingState = viewModel.sharingStateLiveData.collectAsState().value

    //пытаемся форсировать изменение цвета при смене тем
    val configuration = LocalConfiguration.current
    val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK


    sharingState?.let { state ->
        LaunchedEffect(state) {
            when(state){
                is SettingsState.Share -> onShareClick(state.text, context)
                is SettingsState.Support -> onSupportClick(state.email,state.subject, state.text, context)
                is SettingsState.Agreement -> onAgreementClick(state.url,context)
            }
        }
    }
    val backgroundColor = colorResource(R.color.adaptiveBackGroundWhite)
    val textColor = colorResource(R.color.adaptiveCommonText)

    val settingsTextStyle = TextStyle(
        fontFamily = FontFamily( Font(R.font.ys_display_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = textColor
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
                        text = stringResource(R.string.options),
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
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 20.dp,
                            bottom = 20.dp,
                            end = 16.dp
                        )
                        .weight(1f)
                        .wrapContentSize(Alignment.CenterStart)
                        ,
                    text = stringResource(R.string.dark_theme),
                    style = settingsTextStyle
                )
                Switch(
                    modifier = Modifier.padding(16.dp),
                    checked = nightModeState,
                    onCheckedChange = { viewModel.switchDarkTheme(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(R.color.YP_blue),
                        checkedTrackColor = colorResource(R.color.YPBlueLight),

                        uncheckedThumbColor = colorResource(R.color.YP_Text_Gray),
                        uncheckedTrackColor = colorResource(R.color.LightGrey)
                    )
                )
            }
            BuildItems(
                iconId = R.drawable.ic_share_24,
                settingsTextStyle = settingsTextStyle,
                text = stringResource(R.string.share_app),
                onClick = { viewModel.onShareClicked() }
            )
            BuildItems(
                iconId = R.drawable.ic_support_24,
                settingsTextStyle = settingsTextStyle,
                text = stringResource(R.string.support),
                onClick = { viewModel.onSupportClicked() }
            )
            BuildItems(
                iconId = R.drawable.ic_arrowright_8,
                settingsTextStyle = settingsTextStyle,
                text = stringResource(R.string.users_pact),
                onClick = { viewModel.onAgreementClicked() }
            )
        }
    }

}


@Composable
fun BuildItems(iconId: Int, settingsTextStyle : TextStyle, text : String, onClick : () -> Unit){
    val iconColor = colorResource(R.color.YP_Text_Gray)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick() }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 20.dp,
                    bottom = 20.dp,
                    end = 16.dp
                )
                .weight(1f)
                .wrapContentSize(Alignment.CenterStart)
            ,
            text = text,
            style = settingsTextStyle
        )
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(Alignment.CenterStart),
            painter = painterResource(iconId),
            contentDescription = text,
            tint = iconColor
        )
    }
}

private fun onShareClick(text: String, context: Context){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            text
        )
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app)))
}

private fun onSupportClick(email: String, subject : String, text: String, context: Context){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, email)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(intent)
}

private fun onAgreementClick(url: String, context: Context){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
    }
    context.startActivity(intent)
}


@Preview(showSystemUi = true)
@Composable
fun SettingsActivityPreview(){

}