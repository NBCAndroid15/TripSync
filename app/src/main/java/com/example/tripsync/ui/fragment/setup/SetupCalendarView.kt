package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.type.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.util.Calendar


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


    }

//    class TodayDecorator: DayViewDecorator {
//        private var date = CalendarDay.today()
//
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            return day?.equals(date)!!
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.addSpan(StyleSpan(Typeface.BOLD))
//            view?.addSpan(RelativeSizeSpan(1.4f))
//            view?.addSpan(ForegroundColorSpan(Color.parseColor("#1D872A")))
//        }
//    }
//
//
//    class SatDecorator: DayViewDecorator {
//        private val calendar = Calendar.getInstance()
//
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            day?.copyTo(calendar)
//            val weekDay = calendar[Calendar.DAY_OF_WEEK]
//            return weekDay == Calendar.SATURDAY
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.addSpan(ForegroundColorSpan(R.color.main_defalt))
//        }
//    }
//
//    class SunDecorator: DayViewDecorator {
//        private val calendar = Calendar.getInstance()
//
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            day?.copyTo(calendar)
//            val weekDay = calendar[Calendar.DAY_OF_WEEK]
//            return weekDay == Calendar.SUNDAY
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.addSpan(ForegroundColorSpan(Color.RED))
//        }
//    }




}


