package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import com.example.tripsync.databinding.FragmentPlanMemoBinding

class PlanMemoFragment(private val context: Context) : Dialog(context) {

    private lateinit var binding: FragmentPlanMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlanMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogCancelBtn.setOnClickListener {
            dismiss()
        }

        dialogSaveBtn.setOnClickListener {
            if(dialogText.text.isNullOrBlank()) {
                Toast.makeText(context, "여행 계획을 정해주세요!!", Toast.LENGTH_SHORT).show()
            } else {
                dismiss()
            }
        }
    }
}