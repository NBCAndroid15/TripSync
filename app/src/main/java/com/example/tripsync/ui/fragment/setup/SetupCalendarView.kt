package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class SetupCalendarView : DialogFragment() {

    private lateinit var binding: SetupCalendarviewBinding
    private lateinit var calendarView: MaterialCalendarView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetupCalendarviewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        return dialog
    }



}