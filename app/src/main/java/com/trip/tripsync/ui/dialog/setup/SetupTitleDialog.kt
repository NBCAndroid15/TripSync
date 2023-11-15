package com.trip.tripsync.ui.dialog.setup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import com.trip.tripsync.databinding.SetupTitleDialogBinding

class SetupTitleDialog (context: Context,
                        private val okCallback: (String) -> Unit,) : Dialog(context) {

    private lateinit var binding: SetupTitleDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SetupTitleDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))

        dialogCancelBtn.setOnClickListener {
            dismiss()
        }

        dialogSaveBtn.setOnClickListener {
            if(dialogText.text.isNullOrBlank()) {
                Toast.makeText(context, "여행 제목을 정해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val setupTitle = dialogText.text.toString()
                okCallback(setupTitle)
                dismiss()
            }
        }
    }
}