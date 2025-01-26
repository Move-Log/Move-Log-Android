package com.ilgusu.presentation.view.calendar

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
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemRecordedNewsBinding
import com.ilgusu.presentation.util.dpToPx

class RecordRvAdapter :
    ListAdapter<RecordCalendarContent, RecyclerView.ViewHolder>(recordDiffCallback) {
    companion object {
        private val recordDiffCallback = object : DiffUtil.ItemCallback<RecordCalendarContent>() {
            override fun areItemsTheSame(
                oldItem: RecordCalendarContent,
                newItem: RecordCalendarContent
            ): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(
                oldItem: RecordCalendarContent,
                newItem: RecordCalendarContent
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
        fun bind(item: RecordCalendarContent) {
            binding.tvTitle.text = item.noun
            binding.tvRecordTime.text = item.createdAt
            binding.tvChip.text = item.verb
            if (item.verb == "했어요") {
                binding.ivChip.setBackgroundResource(R.drawable.ic_hand_peace)
            }
            else if (item.verb == "먹었어요") {
                binding.ivChip.setBackgroundResource(R.drawable.ic_fork_knife)
            }
            else {
                binding.ivChip.setBackgroundResource(R.drawable.ic_foot_prints)
            }
            Glide.with(binding.ivNewsImg)
                .load(item.recordImageUrl)
                .listener(object:RequestListener<Drawable>{
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
}