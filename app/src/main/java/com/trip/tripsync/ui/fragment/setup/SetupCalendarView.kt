package com.trip.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.trip.tripsync.R
import com.trip.tripsync.databinding.SetupCalendarviewBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import org.threeten.bp.DayOfWeek


class SetupCalendarView(
    private val onDateSelected: (Set<CalendarDay>) -> Unit
) : DialogFragment() {

    private lateinit var binding: SetupCalendarviewBinding

    // 선택한 날짜를 저장
    private var selectedDates = mutableSetOf<CalendarDay>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SetupCalendarviewBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        initView()
        binding.okayBtn.setOnClickListener {
            val currentDate = CalendarDay.today()
            val pastDate = selectedDates.firstOrNull()

            if (pastDate != null && pastDate.isBefore(currentDate) ) {
                Log.d("cal", pastDate.toString())
                Toast.makeText(requireActivity(), "지난 날은 일정에 포함될 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                onDateSelected(selectedDates)
                dialog.dismiss()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }


        return dialog
    }

    private fun initView() = with(binding) {

        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        setupCalendar.setOnDateChangedListener { _, date, selected ->
                if (selected) {
                    selectedDates.clear()
                    selectedDates.add(date)
                } else {
                    selectedDates.remove(date)
                }
        }

        setupCalendar.setOnRangeSelectedListener { _, dates ->

                selectedDates.clear()
                selectedDates.addAll(dates)

        }

        setupCalendar.addDecorators(
            SunDecorator(),
            SatDecorator(),
            TodayDecorator(requireContext()),
            SelectedMonthDecorator(CalendarDay.today().month),
            DayDecorator(requireContext()),
        )
    }

    private inner class DayDecorator(context: Context) : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(context,R.drawable.calendar_selector)
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return true
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }
    }

    private class TodayDecorator(context: Context): DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(context,R.drawable.calendar_circle_gray)
        private var date = CalendarDay.today()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }
        override fun decorate(view: DayViewFacade?) {
            view?.setBackgroundDrawable(drawable!!)
        }
    }


    class SatDecorator: DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            val saturday = day.date.with(DayOfWeek.SATURDAY).dayOfMonth
            return saturday == day.day
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(object : ForegroundColorSpan(Color.BLUE){})
        }
    }

    class SunDecorator: DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val sunday = day.date.with(DayOfWeek.SUNDAY).dayOfMonth
            return sunday == day.day
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(object :ForegroundColorSpan(Color.RED){})
        }
    }

    private inner class SelectedMonthDecorator(val selectedMonth : Int) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.month != selectedMonth
        }
        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_medium)))
        }
    }

}


