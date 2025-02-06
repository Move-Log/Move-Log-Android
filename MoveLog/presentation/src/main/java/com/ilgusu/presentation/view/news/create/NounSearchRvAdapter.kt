package com.ilgusu.presentation.view.news.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.domain.enum.RecordOption
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemNounSearchResultBinding
import com.ilgusu.presentation.util.OnClickRvItemListener

class NounSearchRvAdapter :
    ListAdapter<RecommendKeyword, RecyclerView.ViewHolder>(nounSearchDiffCallback) {

    companion object {
        private val nounSearchDiffCallback = object : DiffUtil.ItemCallback<RecommendKeyword>() {
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
        val view = ItemNounSearchResultBinding.inflate(layoutInflater, parent, false)
        return NewsImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsImageViewHolder).bind(getItem(position))
    }

    inner class NewsImageViewHolder(private val binding: ItemNounSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendKeyword) {
            binding.tvNoun.text = item.noun
            binding.tvChip.text = item.verb

            val imageResource = when (item.verb) {
                RecordOption.GO.koValue -> R.drawable.ic_foot_prints
                RecordOption.DO.koValue -> R.drawable.ic_hand_peace
                RecordOption.EAT.koValue -> R.drawable.ic_fork_knife
                else -> R.drawable.ic_foot_prints
            }

            binding.ivChip.setImageResource(imageResource)

            itemView.setOnClickListener {
                onClickRvItemListener.onClick(item)
            }
        }
    }

    private lateinit var onClickRvItemListener: OnClickRvItemListener<RecommendKeyword>

    fun setOnRvItemClickListener(onClickRvItemListener: OnClickRvItemListener<RecommendKeyword>) {
        this.onClickRvItemListener = onClickRvItemListener
    }
}