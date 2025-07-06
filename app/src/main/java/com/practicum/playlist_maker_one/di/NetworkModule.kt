package com.practicum.playlist_maker_one.di

import com.practicum.playlist_maker_one.data.search.network.ItunesAPI
import com.practicum.playlist_maker_one.data.search.network.RetrofitNetworkClient
import com.practicum.playlist_maker_one.domain.api.NetworkClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val itunesBaseUrl = "https://itunes.apple.com"

val networkModule = module{

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ItunesAPI> {
        get<Retrofit>().create(ItunesAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
}