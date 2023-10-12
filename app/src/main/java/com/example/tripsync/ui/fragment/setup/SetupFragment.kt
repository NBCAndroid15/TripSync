package com.example.tripsync.ui.fragment.setup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tripsync.databinding.FragmentSetupBinding
import java.util.Calendar

class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
    private val binding: FragmentSetupBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    companion object {
        fun newInstance(): SetupFragment {
            return SetupFragment()
        }
    }

    private fun initView() = with(binding) {
        setupTitleBtn.setOnClickListener {
            val setupTitleDialog = SetupTitleDialog(requireContext()) { title ->
                setupTitleBtn.text = title
            }
            setupTitleDialog.show()
        }

        setupDateBtn.setOnClickListener {
            val setupCalendarView = SetupCalendarView()
            setupCalendarView.show(childFragmentManager, "SetupCalendarView")
        }

    }
}





