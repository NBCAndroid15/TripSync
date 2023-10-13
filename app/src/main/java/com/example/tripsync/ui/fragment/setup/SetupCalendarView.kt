package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.Calendar

class SetupCalendarView(private val selectedDates: Set<CalendarDay>,
                        private val onDateSelected: (Set<CalendarDay>) -> Unit ) : DialogFragment() {

    private lateinit var binding: SetupCalendarviewBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetupCalendarviewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        initView()

        return dialog
    }

    private fun initView() = with(binding) {
        setupCalendar.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE
        setupCalendar.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                val selectedDates = widget.selectedDates
                Log.d("SetupCalendarView", "Selected Dates: $selectedDates")
                (parentFragment as? SetupFragment)?.onDateSelected(selectedDates.toSet())

                dismiss()
            }
        })
    }

}