package com.example.tripsync.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val selectDateButton = binding.appCompatButton // Assuming you have a button with this ID in your XML layout

        selectDateButton.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the selected date as needed
            // You can use 'selectedDate' as per your requirements
        }, year, month, day)
        datePickerDialog.show()
    }

    companion object {
        fun newInstance(): SetupFragment {
            return SetupFragment()
        }
    }
}





