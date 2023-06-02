package ru.akimychev.pictureoftheday.viewmodel

import ru.akimychev.pictureoftheday.model.PictureOfTheDayResponseData

sealed class AppState {
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData): AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}