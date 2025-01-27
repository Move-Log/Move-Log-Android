package com.ilgusu.presentation.view.news.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.domain.enum.RecordOption
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemRecommendKeywordBinding
import com.ilgusu.presentation.util.OnClickRvItemListener

class NewsKeywordRvAdapter :
    ListAdapter<RecommendKeyword, RecyclerView.ViewHolder>(newsDiffCallback) {

    private var selectedItem: RecommendKeyword? = null
    fun getSelectedItem(): RecommendKeyword? = selectedItem
    fun resetSelectedItem() {
        selectedItem = null
        notifyDataSetChanged()
    }

    companion object {
        private val newsDiffCallback = object : DiffUtil.ItemCallback<RecommendKeyword>() {
            override fun areItemsTheSame(
                oldItem: RecommendKeyword,
                newItem: RecommendKeyword
            ): Boolean {
                return oldItem.keywordId == newItem.keywordId
            }

            override fun areContentsTheSame(
                oldItem: RecommendKeyword,
                newItem: RecommendKeyword
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRecommendKeywordBinding.inflate(layoutInflater, parent, false)
        return NewsKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsKeywordViewHolder).bind(getItem(position))
    }

    inner class NewsKeywordViewHolder(private val binding: ItemRecommendKeywordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendKeyword) {
            binding.root.background = ContextCompat.getDrawable(
                itemView.context, if (item.keywordId == selectedItem?.keywordId) {
                    R.drawable.shape_recommend_keyword_selected
                } else R.drawable.shape_recommend_keyword
            )

            val chipRes = when (item.verb) {
                RecordOption.EAT.koValue -> R.drawable.ic_fork_knife
                RecordOption.DO.koValue -> R.drawable.ic_hand_peace
                RecordOption.GO.koValue -> R.drawable.ic_foot_prints
                else -> null
            }

            chipRes?.let { binding.ivType.setImageResource(it) }
            binding.tvType.text = item.verb
            binding.tvNoun.text = item.noun

            binding.root.setOnClickListener {
                selectedItem = item
                onClickRvItemListener.onClick(item)
                notifyDataSetChanged()
            }
        }
    }

    private lateinit var onClickRvItemListener: OnClickRvItemListener<RecommendKeyword>

    fun setOnRvItemClickListener(onClickRvItemListener: OnClickRvItemListener<RecommendKeyword>) {
        this.onClickRvItemListener = onClickRvItemListener
    }
}