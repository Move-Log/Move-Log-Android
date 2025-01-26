package com.ilgusu.presentation.view.record

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentRecordBinding
import com.ilgusu.presentation.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RecordFragment : BaseFragment<FragmentRecordBinding>() {

    private val viewModel: RecordViewModel by viewModels()
    private var timeJob: Job? = null

    override fun initView() {
        binding.stepProgressView.setCurrentStep(if (viewModel.recordWord.value.isNullOrBlank()) 1 else 2)
        setTime()
        binding.ibClear.visibility = View.GONE

        viewModel.recordWord.value?.let {
            binding.etRecordWord.setText(it)
            binding.groupRecordWord.visibility = View.VISIBLE
        }
        viewModel.recordType.value?.let {
            setRecordType(it)
            changeRecordTypeColor()
        }
    }

    private fun setTime() {
        timeJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                updateTimeDisplay()
                delay(1000)
            }
        }
    }

    private fun updateTimeDisplay() {
        binding.tvToday.text = DateUtil.getKoreanDateWithDay()
        val currentTime = getFormattedTime()

        binding.icTodayBoard.apply {
            tvHour1.text = currentTime[0][0].toString()
            tvHour2.text = currentTime[0][1].toString()
            tvMinute1.text = currentTime[1][0].toString()
            tvMinute2.text = currentTime[1][1].toString()
            tvSecond1.text = currentTime[2][0].toString()
            tvSecond2.text = currentTime[2][1].toString()
        }
    }

    private fun getFormattedTime(): List<String> {
        val currentTime =
            SimpleDateFormat("HH mm ss", Locale.getDefault()).format(Date()).split(" ")
        return currentTime
    }

    private fun isFinish(): Boolean {
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.secondary)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.gray_66)

        return listOf(binding.ivType0, binding.ivType1, binding.ivType2)
            .any { it.imageTintList?.defaultColor == secondaryColor || it.imageTintList?.defaultColor == defaultColor }
    }

    override fun initListener() {
        super.initListener()

        binding.apply {
            btnBack.setOnClickListener { onBackButtonClick() }
            btnNext.setOnClickListener { onNextButtonClick() }

            llType0.setOnClickListener { setRecordType(0) }
            llType1.setOnClickListener { setRecordType(1) }
            llType2.setOnClickListener { setRecordType(2) }

            etRecordWord.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) showKeyboardAndFocus(v)
            }

            etRecordWord.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    editable?.let {
                        ibClear.visibility = if (it.isNotBlank()) View.VISIBLE else View.GONE
                        viewModel.setRecordWord(editable.toString())
                        setNextButton()
                    }
                }

                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            })

            root.setOnClickListener { etRecordWord.clearFocus() }
            etRecordWord.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) hideKeyboard()
            }
            ibClear.setOnClickListener {
                etRecordWord.text.clear()
                viewModel.setRecordWord("")
            }
        }
    }

    private fun onBackButtonClick() {
        if (isFinish() && binding.stepProgressView.getCurrentStep() == 2) {
            resetStep()
        } else {
            navigateBack()
        }
    }

    private fun resetStep() {
        binding.apply {
            stepProgressView.setCurrentStep(1)
            setNextButton()
            groupRecordWord.visibility = View.GONE
            setTypesClickable(true)
            changeRecordTypeColor()
            binding.etRecordWord.text.clear()
        }
    }

    private fun navigateBack() {
        lifecycleScope.launch {
            navigationManager.navigate(NavigationCommand.Back)
        }
    }

    private fun onNextButtonClick() {
        if (isFinish() && binding.etRecordWord.text.isNotBlank()) {
            navigateToStep3()
        } else {
            startStep2()
        }
    }

    private fun navigateToStep3() {
        timeJob?.cancel()
        val bundle = Bundle().apply {
            putInt("type", findSelectedType())
            putString("word", binding.etRecordWord.text.toString())
        }

        lifecycleScope.launch {
            navigationManager.navigate(
                NavigationCommand.ToRouteWithId(
                    R.id.action_recordFragment_to_recordLastFragment, bundle
                )
            )
        }
    }

    private fun findSelectedType(): Int {
        listOf(
            binding.ivType0,
            binding.ivType1,
            binding.ivType2
        ).forEachIndexed { index, imageView ->
            if (imageView.imageTintList!!.defaultColor == ContextCompat.getColor(
                    requireContext(),
                    R.color.gray_66
                )
            ) {
                return index
            }
        }

        return 0
    }

    private fun startStep2() {
        binding.apply {
            stepProgressView.setCurrentStep(2)
            setNextButton()
            groupRecordWord.visibility = View.VISIBLE
            setTypesClickable(false)
            changeRecordTypeColor()
        }
    }

    private fun changeRecordTypeColor() {
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.secondary)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.gray_66)

        if (binding.stepProgressView.getCurrentStep() == 2) {
            listOf(binding.ivType0, binding.ivType1, binding.ivType2).forEach { imageView ->
                if (imageView.imageTintList?.defaultColor == secondaryColor) {
                    imageView.imageTintList = ColorStateList.valueOf(defaultColor)
                }
            }
        } else {
            listOf(binding.ivType0, binding.ivType1, binding.ivType2).forEach { imageView ->
                if (imageView.imageTintList?.defaultColor == defaultColor) {
                    imageView.imageTintList = ColorStateList.valueOf(secondaryColor)
                }
            }
        }
    }

    private fun setRecordType(typeNum: Int) {
        val secondaryColor = ContextCompat.getColor(requireContext(), R.color.secondary)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.gray_c9)

        listOf(
            binding.ivType0,
            binding.ivType1,
            binding.ivType2
        ).forEachIndexed { index, imageView ->
            imageView.imageTintList = ColorStateList.valueOf(
                if (index == typeNum) secondaryColor else defaultColor
            )
        }
        viewModel.setRecordType(typeNum)
        setNextButton()
    }

    private fun setTypesClickable(isClickable: Boolean) {
        binding.llType0.isClickable = isClickable
        binding.llType1.isClickable = isClickable
        binding.llType2.isClickable = isClickable
    }

    private fun nextButtonState(): Boolean {
        val currentStep = binding.stepProgressView.getCurrentStep()
        return (isFinish() && currentStep == 1) || (currentStep == 2 && binding.etRecordWord.text.isNotBlank())
    }

    private fun setNextButton() {
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        val textColor = ContextCompat.getColor(requireContext(), R.color.white)
        val defaultColor = ContextCompat.getColor(requireContext(), R.color.gray_e1)
        val defaultTextColor = ContextCompat.getColor(requireContext(), R.color.gray_66)

        binding.btnNext.apply {
            backgroundTintList =
                ColorStateList.valueOf(if (nextButtonState()) primaryColor else defaultColor)
            setTextColor(if (nextButtonState()) textColor else defaultTextColor)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timeJob?.cancel()
    }
}