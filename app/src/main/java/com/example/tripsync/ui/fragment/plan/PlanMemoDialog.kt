package com.example.tripsync.ui.fragment.setup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.tripsync.databinding.FragmentPlanMemoBinding

class PlanMemoDialog(private val context: Context, private val initialText: String) : Dialog(context) {

    private lateinit var binding: FragmentPlanMemoBinding
    private lateinit var onSaveListener: (String) -> Unit
    private lateinit var dialogText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlanMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dialogText = binding.dialogText
        dialogText.setText(initialText)
        initView()

    }


    private fun initView() = with(binding) {
        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogCancelBtn.setOnClickListener {
            dismiss()
        }

        dialogSaveBtn.setOnClickListener {
            val memoText = dialogText.text.toString()
            onSaveListener(memoText)
            dismiss()
        }
    }

    fun setOnSaveListener(listener: (String) -> Unit) {
        onSaveListener = listener
    }
}