package com.ilgusu.presentation.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.SnackbarCustomBinding

object CustomToast {
    fun makeToast(context: Context?, msg: String, type: Int = 0): Toast {
        val binding = SnackbarCustomBinding.inflate(LayoutInflater.from(context))

        binding.tvMsg.text = msg
        when(type) {
            0 -> binding.groupIcon.visibility = View.GONE
            1 -> {
                binding.groupIcon.visibility = View.VISIBLE
                binding.ivSnack.setImageResource(R.drawable.icon_check)
            }
            2 -> {
                binding.groupIcon.visibility = View.VISIBLE
                binding.ivSnack.setImageResource(R.drawable.icon_warning )
            }
        }

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}