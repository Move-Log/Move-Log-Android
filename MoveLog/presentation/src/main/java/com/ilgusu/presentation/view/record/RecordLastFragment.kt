package com.ilgusu.presentation.view.record

import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentRecordLastBinding
import com.ilgusu.presentation.util.DateUtil
import com.ilgusu.presentation.util.ImageUtil
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RecordLastFragment : BaseFragment<FragmentRecordLastBinding>() {

    private val viewModel: RecordLastViewModel by viewModels()
    private var timeJob: Job? = null
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                showLoadingDialog()

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val degrees = ImageUtil.getOrientationOfImage(requireContext(), uri)
                        val file = ImageUtil.createImageFile(requireContext(), uri, degrees = degrees.toFloat())

                        launch(Dispatchers.Main) {
                            viewModel.setImageFile(file)
                            binding.ivPhoto.visibility = View.VISIBLE
                            Glide.with(requireContext())
                                .load(uri)
                                .into(binding.ivPhoto)
                            dismissLoadingDialog()
                        }
                    } catch (e: ImageUtil.ImageSizeExceededException) {
                        launch(Dispatchers.Main) {
                            dismissLoadingDialog()
                            showToast("최대 이미지 크기는 5MB 입니다", 2)
                        }
                    }
                }
            }
        }

    override fun initView() {
        binding.stepProgressView.setCurrentStep(3, 3)
        setTime()

        arguments?.let {
            viewModel.setRecordType(it.getInt("type", 0))
            viewModel.setRecordWord(it.getString("word", ""))
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.recordType.observe(viewLifecycleOwner) { type ->
            val typeImages = listOf(
                R.drawable.ic_hand_peace,
                R.drawable.ic_foot_prints,
                R.drawable.ic_fork_knife
            )
            val typeTexts = listOf(
                R.string.record_type_0,
                R.string.record_type_1,
                R.string.record_type_2
            )
            binding.ivType.setImageResource(typeImages[type])
            binding.tvRecordType.setText(typeTexts[type])
        }

        viewModel.recordWord.observe(viewLifecycleOwner) { word ->
            binding.tvRecordWord.text = word
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    showLoadingDialog()
                }

                is UiState.Error -> {
                    showToast(it.message, 2)
                    dismissLoadingDialog()
                }

                is UiState.Success -> {
                    dismissLoadingDialog()
                    RecordResultDialog(
                        requireContext(),
                        onMoveHome = { navigate(NavigationRoutes.Home) },
                        onMoveNews = { navigate(NavigationRoutes.NewsRecent) }
                    ).show()
                }
            }
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
        binding.tvToday.text = DateUtil.getCurrentDate("yyyy년 MM월 dd일 E요일")
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

    override fun initListener() {
        super.initListener()

        binding.btnSelectPhoto.setOnClickListener {
            imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSave.setOnClickListener {
            RecordDialog(
                requireContext(),
                onConfirm = { viewModel.doRecord(true) },
                onCancel = {}
            ).show()
        }

        binding.btnBack.setOnClickListener {
            timeJob?.cancel()

            lifecycleScope.launch {
                navigationManager.navigate(NavigationCommand.Back)
            }
        }
    }

    private fun navigate(routes: NavigationRoutes) {
        timeJob?.cancel()
        lifecycleScope.launch {
            navigationManager.navigate(
                NavigationCommand.ToRouteAndClear(routes)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timeJob?.cancel()
    }
}