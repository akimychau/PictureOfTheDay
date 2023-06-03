package ru.akimychev.pictureoftheday.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.akimychev.pictureoftheday.disposeBy
import ru.akimychev.pictureoftheday.model.RepositoryImpl
import ru.akimychev.pictureoftheday.subscribeByDefault
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {

    private val bag = CompositeDisposable()

    @SuppressLint("SimpleDateFormat")
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date()).toString()

    @RequiresApi(Build.VERSION_CODES.O)
    val yesterday = LocalDate.parse(today).minusDays(1).toString()

    @RequiresApi(Build.VERSION_CODES.O)
    val dayBeforeYesterday = LocalDate.parse(today).minusDays(2).toString()

    fun getLiveData(): MutableLiveData<AppState> {
        return liveData
    }

    fun sendRequest(date: String? = null) {
        liveData.value = AppState.Loading
        repositoryImpl.getPictureOfTheDayAPI(date)
            .subscribeByDefault()
            .subscribe(
                {
                    liveData.value = AppState.Success(it)
                },
                {
                    liveData.value = AppState.Error(it)
                    it.message?.let { it1 -> Log.e("@@@", it1) }
                }
            ).disposeBy(bag)
    }

    override fun onCleared() {
        super.onCleared()
        bag.dispose()
    }
}