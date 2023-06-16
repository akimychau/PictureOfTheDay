package ru.akimychev.pictureoftheday.view.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.akimychev.pictureoftheday.R
import ru.akimychev.pictureoftheday.databinding.BottomNavigationLayoutBinding

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_archive -> Toast.makeText(context, "Archive", Toast.LENGTH_SHORT)
                    .show()

                R.id.navigation_send -> Toast.makeText(context, "Send", Toast.LENGTH_SHORT)
                    .show()

                R.id.navigation_exit -> Toast.makeText(context, "Exit", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}