package ru.akimychev.pictureoftheday.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.akimychev.pictureoftheday.disposeBy
import ru.akimychev.pictureoftheday.model.DaysValues
import ru.akimychev.pictureoftheday.model.RepositoryImpl
import ru.akimychev.pictureoftheday.subscribeByDefault

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
    private val daysValues: DaysValues = DaysValues(),
) : ViewModel() {

    private val bag = CompositeDisposable()

    fun getDaysValues(): DaysValues {
        return daysValues
    }

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