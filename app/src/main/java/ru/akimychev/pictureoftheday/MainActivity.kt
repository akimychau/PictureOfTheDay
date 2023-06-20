package ru.akimychev.pictureoftheday

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import ru.akimychev.pictureoftheday.databinding.ActivityMainBinding
import ru.akimychev.pictureoftheday.utils.THEME
import ru.akimychev.pictureoftheday.utils.THEME_PREFERENCES
import ru.akimychev.pictureoftheday.view.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentTheme = R.style.MyCyanTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        currentTheme = getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
            .getInt(THEME, currentTheme)
        setTheme(currentTheme)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance()).commit()
        }
    }
}