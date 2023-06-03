package ru.akimychev.pictureoftheday.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DaysValues{

    @SuppressLint("SimpleDateFormat")
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date()).toString()

    @RequiresApi(Build.VERSION_CODES.O)
    val yesterday = LocalDate.parse(today).minusDays(1).toString()

    @RequiresApi(Build.VERSION_CODES.O)
    val dayBeforeYesterday = LocalDate.parse(today).minusDays(2).toString()
}
