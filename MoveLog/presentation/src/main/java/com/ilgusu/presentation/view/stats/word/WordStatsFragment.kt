package com.ilgusu.presentation.view.stats.word

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentStatsWordBinding
import com.ilgusu.presentation.util.MarginItemDecoration
import com.ilgusu.presentation.util.OnClickRvItemListener
import com.ilgusu.presentation.util.UiState
import com.ilgusu.presentation.util.dpToPx
import com.ilgusu.presentation.util.hideKeyboard
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordStatsFragment: BaseFragment<FragmentStatsWordBinding>() {

    private lateinit var recommendWordRvAdapter: RecommendWordRvAdapter
    private lateinit var searchResultRvAdapter: SearchResultRvAdapter

    private val viewModel : WordStatsViewModel by viewModels()

    override fun initView() {
        viewModel.fetchRecentRecordWords()
        setupRecyclerView()
    }

    override fun initListener() {
        super.initListener()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    viewModel.searchWord(s.toString())
                }
            }

        })

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ibClear.visibility = View.VISIBLE
                binding.ibSearch.visibility = View.GONE
            }
            else {
                binding.ibClear.visibility = View.GONE
                binding.ibSearch.visibility = View.VISIBLE
            }
        }
        binding.clWord.setOnClickListener {
            binding.etSearch.clearFocus()
            requireContext().hideKeyboard(binding.etSearch)
        }

        binding.ibClear.setOnClickListener {
            binding.etSearch.text.clear()
        }

        binding.ivBack.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(NavigationCommand.Back)
            }
        }
    }

    override fun setObserver() {
        viewModel.getRecentRecordWordState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Error -> LoggerUtil.e("최근 검색 기록 조회 실패: ${it.message}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    recommendWordRvAdapter.list = it.data.toMutableList()
                    recommendWordRvAdapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.searchWordState.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Error -> LoggerUtil.e("전체 단어 검색 기록 조회 실패: ${it.message}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvSearchResult.visibility = if (it.data.isEmpty()) View.GONE else View.VISIBLE
                    searchResultRvAdapter.list = it.data.toMutableList()
                    searchResultRvAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recommendWordRvAdapter = RecommendWordRvAdapter().apply {
            setOnRvItemClickListener(object : OnClickRvItemListener<WordIdStats> {
                override fun onClick(item: WordIdStats) {
                    binding.etSearch.setText(item.noun)
                }
            })
        }
        binding.rvRecommendWord.apply {
            addItemDecoration(MarginItemDecoration(requireContext().dpToPx(16f).toInt()))
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendWordRvAdapter
        }
        searchResultRvAdapter = SearchResultRvAdapter().apply {
            setOnRvItemClickListener(object : OnClickRvItemListener<WordIdStats> {
                override fun onClick(item: WordIdStats) {
                    lifecycleScope.launch {
                        navigationManager.navigate(
                            NavigationCommand.ToRouteWithId(R.id.action_wordStatsFragment_to_wordStatsResultFragment, Bundle().apply {
                                putInt("keywordId", item.keywordId)
                                putString("noun", item.noun)
                            })
                        )
                    }
                }
            })
        }
        binding.rvSearchResult.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchResultRvAdapter
        }
    }
}