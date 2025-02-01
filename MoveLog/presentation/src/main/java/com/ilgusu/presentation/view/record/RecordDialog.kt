package com.ilgusu.presentation.view.record

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.ilgusu.presentation.databinding.DialogRecordSaveBinding
import com.ilgusu.presentation.util.dpToPx
import com.ilgusu.presentation.util.pxToDp

class RecordDialog(
    context: Context,
    private val onConfirm: () -> Unit,
    private val onCancel: () -> Unit
) {
    private val dialog = Dialog(context)
    private val binding: DialogRecordSaveBinding = DialogRecordSaveBinding.inflate(LayoutInflater.from(context))

    init {
        dialog.setContentView(binding.root)

        binding.btnYes.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        binding.btnNo.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }

        dialog.let {
            it.window?.setLayout(context.dpToPx(300f).toInt(), WRAP_CONTENT)
            it.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent))
            it.setCancelable(false)
        }
    }

    fun show() {
        dialog.show()
    }
}