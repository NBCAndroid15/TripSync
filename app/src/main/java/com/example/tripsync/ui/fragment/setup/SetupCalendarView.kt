package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.util.Calendar


class SetupCalendarView(
    private val onDateSelected: (Set<CalendarDay>) -> Unit
) : DialogFragment() {

    private lateinit var binding: SetupCalendarviewBinding

    // 선택한 날짜를 저장
    private var selectedDates = mutableSetOf<CalendarDay>()
    private var isRangeSelecting = false
    private var startDate: CalendarDay? = null
    private var endDate: CalendarDay? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetupCalendarviewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        initView()
        binding.okayBtn.setOnClickListener {

            onDateSelected(selectedDates)
            dialog.dismiss()
        }

        return dialog
    }


    private fun initView() = with(binding) {

        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        setupCalendar.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                if (isRangeSelecting) {
                    val datesInRange = findDatesInRange(startDate!!, date)
                    selectedDates.addAll(datesInRange)
                    isRangeSelecting = false
                } else {
                    selectedDates.clear()
                    startDate = date
                    selectedDates.add(date)
                    isRangeSelecting = true
                }
            } else {
                selectedDates.remove(date)
            }
        }
    }

    private fun findDatesInRange(startDate: CalendarDay, endDate: CalendarDay): List<CalendarDay> {
        val datesInRange = mutableListOf<CalendarDay>()
        var currentDay = startDate

        while (!currentDay.isAfter(endDate)) {
            datesInRange.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }
        return datesInRange
    }

}

private fun CalendarDay.plusDays(days: Int): CalendarDay {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day)
    calendar.add(Calendar.DAY_OF_MONTH, days)
    val newYear = calendar.get(Calendar.YEAR)
    val newMonth = calendar.get(Calendar.MONTH) + 1
    val newDay = calendar.get(Calendar.DAY_OF_MONTH)

    return CalendarDay.from(newYear, newMonth, newDay)
}
