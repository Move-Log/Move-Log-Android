package com.ilgusu.presentation.view.news.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.presentation.databinding.DialogSearchWordBinding
import com.ilgusu.presentation.util.OnClickRvItemListener
import com.ilgusu.presentation.util.UiState
import com.ilgusu.presentation.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NounSearchDialog(
    private val onConfirm: (RecommendKeyword) -> Unit,
) : DialogFragment() {

    private var _binding: DialogSearchWordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NounSearchViewModel by viewModels()
    private lateinit var rvAdapter: NounSearchRvAdapter

    override fun onStart() {
        super.onStart()

        dialog?.let {
            it.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
            it.window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
            it.window?.setDimAmount(0f)
            it.setCancelable(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSearchWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setListener()
        setObserver()
    }

    private fun setView() {
        rvAdapter = NounSearchRvAdapter().apply {
            setOnRvItemClickListener(object : OnClickRvItemListener<RecommendKeyword> {
                override fun onClick(item: RecommendKeyword) {
                    onConfirm(item)
                    dismiss()
                }
            })
        }

        binding.rvSearchResult.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener { dismiss() }

        binding.ibClear.setOnClickListener { binding.etSearch.text.clear() }

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ibClear.visibility = View.VISIBLE
                binding.ibSearch.visibility = View.GONE
            } else {
                binding.ibClear.visibility = View.GONE
                binding.ibSearch.visibility = View.VISIBLE
            }
        }

        binding.root.setOnClickListener {
            binding.etSearch.clearFocus()
            requireContext().hideKeyboard(binding.etSearch)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    viewModel.search(s.toString())
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {}
                is UiState.Success -> {
                    rvAdapter.submitList(it.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
