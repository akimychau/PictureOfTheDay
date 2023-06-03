package ru.akimychev.pictureoftheday.model

import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.akimychev.pictureoftheday.BuildConfig

class RepositoryImpl : Repository {
    private val baseUrl = "https://api.nasa.gov/"

    private val pictureOfTheDayAPI: PictureOfTheDayAPI by lazy {
        retrofit.create(PictureOfTheDayAPI::class.java)
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    fun getPictureOfTheDayAPI(date: String?): Single<PictureOfTheDayResponseData> {
        return pictureOfTheDayAPI.getPictureOfTheDay(BuildConfig.NASA_API_KEY, date)
    }
}