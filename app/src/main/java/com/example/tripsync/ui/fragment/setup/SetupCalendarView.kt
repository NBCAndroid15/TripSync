package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupCalendarviewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter



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

//        initView()
//        binding.okayBtn.setOnClickListener {
//
//            onDateSelected(selectedDates)
//            dialog.dismiss()
//        }

        initDatePicker()

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = 300

        dialog?.window?.setLayout(width, height)
    }


//    private fun initView() = with(binding) {
//
//        setupCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
//        setupCalendar.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
//
//        setupCalendar.setOnDateChangedListener { _, date, selected ->
//            if (selected) {
//                selectedDates.add(date)
//            } else {
//                selectedDates.remove(date)
//            }
//        }
//
//        setupCalendar.setOnRangeSelectedListener { widget, dates ->
//            selectedDates.clear()
//            selectedDates.addAll(dates)
//        }
//
//    }

    private fun initDatePicker()= with(binding) {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("여행")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

        dateRangePicker.show(parentFragmentManager, "tag")
    }


}


