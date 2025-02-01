package com.ilgusu.presentation.view.news.calendar

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemRecordedNewsBinding
import com.ilgusu.presentation.util.DateUtil
import com.ilgusu.presentation.util.dpToPx

class NewsRecordRvAdapter : ListAdapter<NewsContent, RecyclerView.ViewHolder>(recordDiffCallback) {
    companion object {
        private val recordDiffCallback = object : DiffUtil.ItemCallback<NewsContent>() {
            override fun areItemsTheSame(
                oldItem: NewsContent,
                newItem: NewsContent
            ): Boolean {
                return oldItem.newsId == newItem.newsId
            }

            override fun areContentsTheSame(
                oldItem: NewsContent,
                newItem: NewsContent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRecordedNewsBinding.inflate(layoutInflater, parent, false)
        return RecordCalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecordCalendarViewHolder).bind(getItem(position))
    }

    inner class RecordCalendarViewHolder(private val binding: ItemRecordedNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsContent) {
            binding.tvTitle.text = item.noun
            binding.tvRecordTime.text = DateUtil.extractTimeFlexible(item.createdAt)

            binding.tvChip.text = item.verb
            when (item.verb) {
                "했어요" -> {
                    binding.ivChip.setBackgroundResource(R.drawable.ic_hand_peace)
                }

                "먹었어요" -> {
                    binding.ivChip.setBackgroundResource(R.drawable.ic_fork_knife)
                }

                "갔어요" -> {
                    binding.ivChip.setBackgroundResource(R.drawable.ic_foot_prints)
                }
            }

            Glide.with(binding.ivNewsImg)
                .load(item.newsImageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.ivNewsImg.setBackgroundResource(0)
                        return false
                    }
                })
                .transform(RoundedCorners(itemView.context.dpToPx(8f).toInt()))
                .into(binding.ivNewsImg)
        }
    }

    fun submitList(list: List<NewsContent>, doClear: Boolean = false) {
        val temp = currentList.toMutableList()
        temp.addAll(list)

        submitList(if(doClear) emptyList() else temp.distinct())
    }
}