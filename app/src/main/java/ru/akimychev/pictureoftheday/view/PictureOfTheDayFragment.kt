package ru.akimychev.pictureoftheday.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.akimychev.pictureoftheday.R
import ru.akimychev.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import ru.akimychev.pictureoftheday.makeGone
import ru.akimychev.pictureoftheday.makeVisible
import ru.akimychev.pictureoftheday.viewmodel.AppState
import ru.akimychev.pictureoftheday.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendRequest()
        renderData()

        binding.chipToday.setOnClickListener {
            viewModel.sendRequest(viewModel.getDaysValues().today)
        }
        binding.chipYesterday.setOnClickListener {
            viewModel.sendRequest(viewModel.getDaysValues().yesterday)
        }
        binding.chipDayBeforeYesterday.setOnClickListener {
            viewModel.sendRequest(viewModel.getDaysValues().dayBeforeYesterday)
        }
    }

    private fun renderData() {
        viewModel.getLiveData().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Error -> {
                    binding.progressBar.makeGone()
                    binding.imageView.load(R.drawable.ic_baseline_report_gmailerrorred_24)
                    view?.findViewById<TextView>(R.id.bottomSheetDescription)?.text =
                        getString(R.string.not_ready_description)
                    view?.findViewById<TextView>(R.id.bottomSheetDescriptionHeader)?.text =
                        getString(R.string.not_ready)
                    Toast.makeText(requireContext(),
                        "Something went wrong. Try it later",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                is AppState.Success -> {
                    with(appState) {
                        binding.imageView.load(pictureOfTheDayResponseData.url) {
                            placeholder(R.drawable.ic_baseline_wallpaper_24)
                            error(R.drawable.ic_baseline_report_gmailerrorred_24)
                        }
                        binding.progressBar.makeGone()
                        binding
                        view?.findViewById<TextView>(R.id.bottomSheetDescription)?.text =
                            pictureOfTheDayResponseData.explanation
                        view?.findViewById<TextView>(R.id.bottomSheetDescriptionHeader)?.text =
                            pictureOfTheDayResponseData.title
                    }
                }
                AppState.Loading -> {
                    binding.progressBar.makeVisible()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}