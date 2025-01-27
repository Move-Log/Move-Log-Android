package com.ilgusu.presentation.view.news.recent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemRecentNewsDefaultBinding
import com.ilgusu.presentation.databinding.ItemRecentNewsFirstBinding
import com.ilgusu.presentation.util.DateUtil
import com.ilgusu.util.LoggerUtil

class NewsRecentRvAdapter: ListAdapter<NewsContent, RecyclerView.ViewHolder>(newsDiffCallback) {

    companion object {
        private const val VIEW_TYPE_FIRST = 0
        private const val VIEW_TYPE_DEFAULT = 1

        private val newsDiffCallback = object : DiffUtil.ItemCallback<NewsContent>() {
            override fun areItemsTheSame(oldItem: NewsContent, newItem: NewsContent): Boolean {
                return oldItem.newsId == newItem.newsId
            }

            override fun areContentsTheSame(oldItem: NewsContent, newItem: NewsContent): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FIRST -> FirstViewHolder.from(parent)
            VIEW_TYPE_DEFAULT-> DefaultViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FirstViewHolder -> holder.bind(getItem(position))
            is DefaultViewHolder -> holder.bind(getItem(position))
        }
    }

    class FirstViewHolder private constructor(private val binding: ItemRecentNewsFirstBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsContent) {
            binding.tvNewsTitle.text = item.headLine
            Glide.with(itemView)
                .load(item.newsImageUrl)
                .transform(RoundedCorners(8))
                .into(binding.ivNewsImage)

            binding.tvNewsInfo.text = buildSpannedString {
                append(item.verb + " · " + item.noun + " · ")
                color(itemView.context.getColor(R.color.primary)) {
                    append(DateUtil.getRelativeTime(item.createdAt))
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): FirstViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemRecentNewsFirstBinding.inflate(layoutInflater, parent, false)
                return FirstViewHolder(view)
            }
        }
    }

    class DefaultViewHolder private constructor(private val binding: ItemRecentNewsDefaultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsContent) {
            binding.tvNewsTitle.text = item.headLine
            Glide.with(itemView)
                .load(item.newsImageUrl)
                .transform(RoundedCorners(2))
                .into(binding.ivNewsImage)

            LoggerUtil.i(item.createdAt)
            LoggerUtil.i(DateUtil.getRelativeTime(item.createdAt))

            binding.tvNewsInfo.text = buildSpannedString {
                append(item.verb + " · " + item.noun + " · ")
                color(itemView.context.getColor(R.color.primary)) {
                    append(DateUtil.getRelativeTime(item.createdAt))
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): DefaultViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemRecentNewsDefaultBinding.inflate(layoutInflater, parent, false)
                return DefaultViewHolder(view)
            }
        }
    }
}