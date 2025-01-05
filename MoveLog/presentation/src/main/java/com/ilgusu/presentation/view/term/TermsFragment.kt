package com.ilgusu.presentation.view.term

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentTermsBinding
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>() {

    private val viewModel: TermsViewModel by viewModels()

    override fun initView() {
        setTextViewHighlighting()
    }

    override fun initListener() {
        super.initListener()

        binding.llCbPrivacy.setOnClickListener {
            if (binding.cbPrivacy.isChecked) {
                binding.cbPrivacy.isChecked = false
                binding.cbAll.isChecked = false
            } else {
                binding.cbPrivacy.isChecked = true
                binding.cbAll.isChecked = true
            }
        }

        binding.llCbAll.setOnClickListener {
            if (binding.cbAll.isChecked) {
                binding.cbPrivacy.isChecked = false
                binding.cbAll.isChecked = false
            } else {
                binding.cbPrivacy.isChecked = true
                binding.cbAll.isChecked = true
            }
        }

        binding.cbAll.setOnCheckedChangeListener { _, _ ->
            if (binding.cbAll.isChecked) {
                binding.btnStart.isClickable = true
                binding.btnStart.alpha = 1f
            } else {
                binding.btnStart.isClickable = false
                binding.btnStart.alpha = 0.6f
            }
        }

        binding.btnStart.setOnClickListener {
            viewModel.signUp()
        }
    }

    private fun setTextViewHighlighting() {
        val originalText = binding.tvTermSubTitle.text
        val spannableString = SpannableString(originalText)

        val lastIndex = originalText.length - 1

        spannableString.setSpan(
            ForegroundColorSpan(requireContext().getColor(R.color.red)),
            lastIndex,
            originalText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTermSubTitle.text = spannableString
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Error -> {
                    showToast(it.message)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    lifecycleScope.launch {
                        navigationManager.navigate(
                            NavigationCommand.ToRoute(NavigationRoutes.Home)
                        )
                    }
                }
            }
        }
    }
}