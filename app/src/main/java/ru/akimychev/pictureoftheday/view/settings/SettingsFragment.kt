package ru.akimychev.pictureoftheday.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.akimychev.pictureoftheday.R
import ru.akimychev.pictureoftheday.databinding.FragmentSettingsBinding
import ru.akimychev.pictureoftheday.utils.THEME
import ru.akimychev.pictureoftheday.utils.THEME_PREFERENCES

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var currentTheme: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cyan.setOnClickListener {
            currentTheme = R.style.MyCyanTheme
            updateTheme()
            saveCurrentTheme()
        }

        binding.green.setOnClickListener {
            currentTheme = R.style.MyGreenTheme
            updateTheme()
            saveCurrentTheme()
        }

        binding.purple.setOnClickListener {
            currentTheme = R.style.MyPurpleTheme
            updateTheme()
            saveCurrentTheme()
        }
    }

    private fun updateTheme() {
        currentTheme?.let { requireActivity().setTheme(it) }
        requireActivity().recreate()
    }

    private fun saveCurrentTheme() {
        val preferences =
            requireActivity().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        currentTheme?.let { preferences.edit().putInt(THEME, it).apply() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}