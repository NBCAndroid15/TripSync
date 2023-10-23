package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter


class SetupCalendarView(
                        private val onDateSelected: (Set<CalendarDay>) -> Unit ) : DialogFragment() {

    private lateinit var binding: SetupCalendarviewBinding
    private var temporarySelectedDates = mutableSetOf<CalendarDay>()
    private val selectedDates = mutableSetOf<CalendarDay>()





    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetupCalendarviewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        initView()
        binding.okayBtn.setOnClickListener {
            selectedDates.clear()
            selectedDates.addAll(temporarySelectedDates)
            onDateSelected(selectedDates)
            dialog.dismiss()
            dialog.dismiss()
        }



        return dialog
    }


    private fun initView() = with(binding) {

        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        setupCalendar.setOnRangeSelectedListener { widget, dates ->
            temporarySelectedDates = dates.toMutableSet()
        }
//        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
//        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
//
//        setupCalendar.setOnRangeSelectedListener { widget, dates ->
//            val selectedDate = widget.selectedDates
//            (parentFragment as? SetupFragment)?.onDateSelected(selectedDate.toSet())
//            val distinctSelectedDates = if (selectedDate.isNotEmpty()) {
//                val startDate = selectedDate[0]
//                val endDate = selectedDate[selectedDate.size - 1]
//                if (startDate == endDate) {
//                    setOf(startDate)
//                } else {
//                    selectedDate.toSet()
//                }
//            } else {
//                emptySet()
//            }
//            doSomethingWithDistinctDates(distinctSelectedDates)
//            temporarySelectedDates.clear()
//            temporarySelectedDates.addAll(selectedDate)        }
    }
    private fun doSomethingWithDistinctDates(distinctSelectedDates: Set<CalendarDay>) {
        for (date in distinctSelectedDates) {
            val year = date.year
            val month = date.month
            val day = date.day
            Log.d("SetupCalendarView", "Selected Date: $year-$month-$day")
        }

    }


}