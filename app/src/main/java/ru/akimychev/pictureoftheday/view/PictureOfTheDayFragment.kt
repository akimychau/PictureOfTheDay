package ru.akimychev.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.akimychev.pictureoftheday.MainActivity
import ru.akimychev.pictureoftheday.R
import ru.akimychev.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import ru.akimychev.pictureoftheday.makeGone
import ru.akimychev.pictureoftheday.makeVisible
import ru.akimychev.pictureoftheday.view.drawer.BottomNavigationDrawerFragment
import ru.akimychev.pictureoftheday.view.settings.SettingsFragment
import ru.akimychev.pictureoftheday.viewmodel.AppState
import ru.akimychev.pictureoftheday.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment(), MenuProvider {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        chipsListener()

        wikiSearch()

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        addMenuProvider()
    }

    private fun addMenuProvider() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (requireActivity() as MainActivity).setSupportActionBar(binding.bottomAppBar)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chipsListener() {
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
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong. Try it later",
                        Toast.LENGTH_SHORT
                    )
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

    private fun setBottomSheetBehavior(bottomSheet: LinearLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun wikiSearch() {
        binding.inputLayout.setEndIconOnClickListener {
            if (binding.inputEditText.text?.length!! <= binding.inputLayout.counterMaxLength) {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data =
                        Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
                })
            } else {
                Toast.makeText(
                    requireContext(),
                    "Character limit exceeded",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.app_bar_fav -> {}
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager.beginTransaction().hide(this)
                    .add(R.id.container, SettingsFragment.newInstance()).addToBackStack("")
                    .commit()
            }

            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, tag)
                }
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}