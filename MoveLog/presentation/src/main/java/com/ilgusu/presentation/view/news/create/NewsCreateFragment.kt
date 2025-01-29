package com.ilgusu.presentation.view.news.create

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilgusu.domain.enum.RecordOption
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentNewsCreateBinding
import com.ilgusu.presentation.util.ImageUtil
import com.ilgusu.presentation.util.OnClickRvItemListener
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class NewsCreateFragment : BaseFragment<FragmentNewsCreateBinding>() {

    private val viewModel: NewsCreateViewModel by viewModels()
    private lateinit var newsKeywordRvAdapter: NewsKeywordRvAdapter
    private lateinit var newsImageRvAdapter: NewsImageRvAdapter
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setFile(ImageUtil.createImageFile(requireContext(), uri))
                binding.viewCreate3.tvSelectedData.text = uri.path?.substringAfterLast("/")
            }
        }

    override fun initView() {
        binding.stepProgressView.setCurrentStep(viewModel.currentStep.value!!, 4)

        // 추가 작업 필요
        binding.viewCreate1.tvDirectTitle.visibility = View.GONE
        binding.viewCreate1.tvSelectedData.visibility = View.GONE
        binding.viewCreate1.ivSelectedData.visibility = View.GONE
    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            if (viewModel.currentStep.value == 1) {
                lifecycleScope.launch {
                    navigationManager.navigate(
                        NavigationCommand.Back
                    )
                }
            } else {
                viewModel.setCurrentStep(viewModel.currentStep.value!! - 1)
                setHeadlineType(null)
            }
        }

        binding.btnNext.setOnClickListener {
            if (binding.btnNext.textColors.defaultColor == ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            ) {
                val currentStep = viewModel.currentStep.value!!

                if (currentStep == 3 && viewModel.selectedFile.value == null) {
                    var file: File

                    runBlocking(Dispatchers.IO) {
                        val futureTarget = Glide.with(requireContext())
                            .asFile()
                            .load(newsImageRvAdapter.getSelectedItem()?.imageUrl)
                            .submit()

                        file = futureTarget.get()
                    }

                    viewModel.setFile(file)
                }

                if (currentStep == 4) {
                    LoggerUtil.i(
                        """
                        Keyword: ${viewModel.selectedKeyword.value}
                        HeadlineType: ${viewModel.headlineType.value}
                        Image: ${viewModel.selectedFile.value?.path}
                        Headline: ${viewModel.selectedHeadline.value}
                    """.trimIndent()
                    )

                    createNews()
                } else {
                    viewModel.setCurrentStep(currentStep + 1)
                }
            }
        }


        binding.viewCreate2.tvHeadlineType1.setOnClickListener { setHeadlineType("첫 도전") }
        binding.viewCreate2.tvHeadlineType2.setOnClickListener { setHeadlineType("오랜만에 다시") }
        binding.viewCreate2.tvHeadlineType3.setOnClickListener { setHeadlineType("꾸준히 이어온 기록") }
        binding.viewCreate2.tvHeadlineType4.setOnClickListener { setHeadlineType("끊어낸 습관") }

        binding.viewCreate3.tvSelectedData.setOnClickListener {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.viewCreate3.ivSelectedData.setOnClickListener {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.viewCreate4.tvOption1.setOnClickListener {
            binding.viewCreate4.etHeadline.clearFocus()
            if(binding.viewCreate4.tvOption1.text.isNotBlank()) {
                viewModel.setHeadline(
                    binding.viewCreate4.tvOption1.text.toString()
                )
            }
        }

        binding.viewCreate4.tvOption2.setOnClickListener {
            binding.viewCreate4.etHeadline.clearFocus()
            if(binding.viewCreate4.tvOption2.text.isNotBlank()) {
                viewModel.setHeadline(
                    binding.viewCreate4.tvOption2.text.toString()
                )
            }
        }

        binding.viewCreate4.root.setOnClickListener { binding.viewCreate4.etHeadline.clearFocus() }

        binding.viewCreate4.ibClear.setOnClickListener {
            viewModel.setHeadline(null)
            binding.viewCreate4.etHeadline.text.clear()
        }

        binding.viewCreate4.etHeadline.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.viewCreate4.ibClear.visibility =
                    if (s.isNullOrBlank()) View.GONE else View.VISIBLE
                s?.let {
                    if (s.length >= 6) viewModel.setHeadline(s.toString()) else viewModel.setHeadline(
                        null
                    )
                }
            }
        })
    }

    private fun createNews() {
        val bundle = Bundle().apply {
            putInt("keywordId", viewModel.selectedKeyword.value?.keywordId!!)
            putString("headline", viewModel.selectedHeadline.value)
            putSerializable("path", viewModel.selectedFile.value?.absolutePath)
        }

        lifecycleScope.launch {
            navigationManager.navigate(
                NavigationCommand.ToRouteWithId(
                    R.id.action_newsCreateFragment_to_newsResultFragment, bundle
                )
            )
        }
    }

    private fun setHeadlineType(type: String?) {
        viewModel.setHeadlineType(type)
        val selectedRes = R.drawable.shape_recommend_keyword_selected
        val unselectedRes = R.drawable.shape_recommend_keyword

        binding.viewCreate2.apply {
            tvHeadlineType1.setBackgroundResource(if (type == "첫 도전") selectedRes else unselectedRes)
            tvHeadlineType2.setBackgroundResource(if (type == "오랜만에 다시") selectedRes else unselectedRes)
            tvHeadlineType3.setBackgroundResource(if (type == "꾸준히 이어온 기록") selectedRes else unselectedRes)
            tvHeadlineType4.setBackgroundResource(if (type == "끊어낸 습관") selectedRes else unselectedRes)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.currentStep.observe(viewLifecycleOwner) {
            binding.stepProgressView.setCurrentStep(it, 4)
            setStepView(it)
        }

        viewModel.selectedKeyword.observe(viewLifecycleOwner) { setButtonColor(it != null) }
        viewModel.headlineType.observe(viewLifecycleOwner) { setButtonColor(it != null) }
        viewModel.selectedFile.observe(viewLifecycleOwner) {
            setButtonColor(it != null)
        }
        viewModel.selectedHeadline.observe(viewLifecycleOwner) {
            val selectedRes = R.drawable.shape_recommend_keyword_selected
            val unselectedRes = R.drawable.shape_recommend_keyword

            binding.viewCreate4.tvOption1.setBackgroundResource(if (binding.viewCreate4.tvOption1.text.toString() == it) selectedRes else unselectedRes)
            binding.viewCreate4.tvOption2.setBackgroundResource(if (binding.viewCreate4.tvOption2.text.toString() == it) selectedRes else unselectedRes)

            setButtonColor(it != null)
        }

        viewModel.recommendKeywordState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    newsKeywordRvAdapter.submitList(it.data)
                }
            }
        }

        viewModel.recentImageInfo.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    newsImageRvAdapter.submitList(it.data)
                }
            }
        }

        viewModel.headlineState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    binding.viewCreate4.tvOption1.text = it.data[0]
                    binding.viewCreate4.tvOption2.text = it.data[1]
                }
            }
        }
    }

    private fun setButtonColor(isAvailable: Boolean) {
        val textColor = ContextCompat.getColor(
            requireContext(),
            if (isAvailable) R.color.white else R.color.gray_66
        )
        val tintColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (isAvailable) R.color.primary else R.color.gray_e1
            )
        )

        binding.btnNext.setTextColor(textColor)
        binding.btnNext.backgroundTintList = tintColor
    }

    private fun setStepView(step: Int) {
        binding.viewCreate1.root.visibility = if (step == 1) View.VISIBLE else View.GONE
        binding.viewCreate2.root.visibility = if (step == 2) View.VISIBLE else View.GONE
        binding.viewCreate3.root.visibility = if (step == 3) View.VISIBLE else View.GONE
        binding.viewCreate4.root.visibility = if (step == 4) View.VISIBLE else View.GONE

        when (step) {
            1 -> {
                if (viewModel.selectedKeyword.value == null) {
                    newsKeywordRvAdapter = NewsKeywordRvAdapter().apply {
                        setOnRvItemClickListener(object : OnClickRvItemListener<RecommendKeyword> {
                            override fun onClick(item: RecommendKeyword) {
                                viewModel.setKeyword(item)
                            }
                        })
                    }

                    binding.viewCreate1.rvRecommendData.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    binding.viewCreate1.rvRecommendData.adapter = newsKeywordRvAdapter
                    viewModel.recommendKeyword()
                } else {
                    newsKeywordRvAdapter.resetSelectedItem()
                }

                binding.tvNoun.visibility = View.GONE
                binding.llChip.visibility = View.GONE
                binding.tvHeadline.visibility = View.GONE
                viewModel.resetData()
            }

            2 -> {
                binding.tvNoun.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary
                    )
                )

                binding.tvHeadline.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.secondary
                    )
                )

                val selectedKeyword = viewModel.selectedKeyword.value

                binding.tvHeadline.visibility = View.GONE
                binding.llChip.visibility = View.VISIBLE
                binding.tvNoun.visibility = View.VISIBLE
                viewModel.setFile(null)

                val chipRes = when (selectedKeyword?.verb) {
                    RecordOption.EAT.koValue -> R.drawable.ic_fork_knife
                    RecordOption.DO.koValue -> R.drawable.ic_hand_peace
                    RecordOption.GO.koValue -> R.drawable.ic_foot_prints
                    else -> null
                }

                chipRes?.let { binding.ivChip.setImageResource(it) }
                binding.tvChip.text = selectedKeyword?.verb
                binding.tvNoun.text = selectedKeyword?.noun
            }

            3 -> {
                if (viewModel.selectedFile.value == null) {
                    newsImageRvAdapter = NewsImageRvAdapter().apply {
                        setOnRvItemClickListener(object : OnClickRvItemListener<ImageInfo> {
                            override fun onClick(item: ImageInfo) {
                                setButtonColor(item.imageUrl.isNotBlank())
                            }
                        })
                    }

                    binding.viewCreate3.rvRecommendData.layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    binding.viewCreate3.rvRecommendData.adapter = newsImageRvAdapter
                    viewModel.getRecentImages()
                } else {
                    newsImageRvAdapter.resetSelectedItem()
                }

                binding.tvNoun.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_1c
                    )
                )

                binding.tvHeadline.visibility = View.VISIBLE
                binding.newsOverlay.visibility = View.INVISIBLE
                binding.ivNews.setImageResource(0)
                viewModel.setHeadline(null)

                binding.tvHeadline.text = viewModel.headlineType.value
            }

            4 -> {
                viewModel.recommendHeadlines()
                binding.newsOverlay.visibility = View.VISIBLE
                binding.tvNoun.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                binding.tvHeadline.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

                Glide.with(requireContext())
                    .load(viewModel.selectedFile.value)
                    .into(binding.ivNews)
            }
        }
    }
}