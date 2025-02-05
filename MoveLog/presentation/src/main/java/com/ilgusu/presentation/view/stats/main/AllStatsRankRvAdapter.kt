package com.ilgusu.presentation.view.stats.main

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.domain.model.stats.TopRecord
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemRankBinding
import com.ilgusu.presentation.util.OnClickRvItemListener

class AllStatsRankRvAdapter : ListAdapter<TopRecord, RecyclerView.ViewHolder>(newsDiffCallback) {

    companion object {
        private val newsDiffCallback = object : DiffUtil.ItemCallback<TopRecord>() {
            override fun areItemsTheSame(
                oldItem: TopRecord,
                newItem: TopRecord,
            ): Boolean {
                return oldItem.keyword == newItem.keyword
            }

            override fun areContentsTheSame(
                oldItem: TopRecord,
                newItem: TopRecord,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRankBinding.inflate(layoutInflater, parent, false)
        return NewsKeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsKeywordViewHolder).bind(getItem(position))
    }

    inner class NewsKeywordViewHolder(private val binding: ItemRankBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopRecord) {

            binding.tvRank.text = item.rank.toString()
            binding.tvNoun.text = item.keyword
            binding.tvCount.text = changeTextColor(itemView.context, item.count.toString())

            val trendResourceId = when (item.trend) {
                "up", "down" -> R.drawable.ic_triangle
                else -> R.drawable.ic_rank_same
            }

            binding.ivTrend.setImageResource(trendResourceId)
            when (item.trend) {
                "up" -> {
                    binding.ivTrend.rotation = 180f
                    binding.ivTrend.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.red)
                    )
                }

                "down" -> {

                    binding.ivTrend.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.secondary)
                    )
                }

                else -> {
                    binding.ivTrend.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.gray_b0)
                    )
                }
            }

            itemView.setOnClickListener {
                onClickRvItemListener.onClick(item.keyword)
            }
        }
    }

    private fun changeTextColor(context: Context, text: String) = buildSpannedString {
        color(ContextCompat.getColor(context, R.color.primary)) {
            append(text)
        }
        append("건")
    }

    private lateinit var onClickRvItemListener: OnClickRvItemListener<String>

    fun setOnRvItemClickListener(onClickRvItemListener: OnClickRvItemListener<String>) {
        this.onClickRvItemListener = onClickRvItemListener
    }
}