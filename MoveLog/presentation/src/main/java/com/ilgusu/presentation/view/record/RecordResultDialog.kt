package com.ilgusu.presentation.view.record

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.ilgusu.presentation.databinding.DialogRecordResultBinding
import com.ilgusu.presentation.util.dpToPx

class RecordResultDialog(
    context: Context,
    private val onMoveHome: () -> Unit,
    private val onMoveNews: () -> Unit
) {
    private val dialog = Dialog(context)
    private val binding: DialogRecordResultBinding = DialogRecordResultBinding.inflate(LayoutInflater.from(context))

    init {
        dialog.setContentView(binding.root)

        binding.btnMoveHome.setOnClickListener {
            onMoveHome()
            dialog.dismiss()
        }

        binding.btnMoveNews.setOnClickListener {
            onMoveNews()
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