package ru.akimychev.pictureoftheday.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Single<PictureOfTheDayResponseData>
}