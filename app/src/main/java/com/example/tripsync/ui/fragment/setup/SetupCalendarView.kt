package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
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
        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        setupCalendar.setOnRangeSelectedListener { widget, dates ->
            val selectedDates = widget.selectedDates
            (parentFragment as? SetupFragment)?.onDateSelected(selectedDates.toSet())
            val distinctSelectedDates = if (selectedDates.isNotEmpty()) {
                val startDate = selectedDates[0]
                val endDate = selectedDates[selectedDates.size - 1]
                if (startDate == endDate) {
                    setOf(startDate)
                } else {
                    selectedDates.toSet()
                }
            } else {
                emptySet()
            }

            doSomethingWithDistinctDates(distinctSelectedDates)
            dismiss()
        }
    }

    private fun doSomethingWithDistinctDates(distinctSelectedDates: Set<CalendarDay>) {
        for (date in distinctSelectedDates) {
            val year = date.year
            val month = date.month
            val day = date.day
            Log.d("SetupCalendarView", "Selected Date: $year-$month-$day")
        }

    }



//        setupCalendar.setOnDateChangedListener { widget, date, selected ->

//
//            dismiss()
//        }

}