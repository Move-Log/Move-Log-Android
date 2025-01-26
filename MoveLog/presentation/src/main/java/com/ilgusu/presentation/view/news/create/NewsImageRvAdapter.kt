package com.ilgusu.presentation.view.news.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemUploadImageBinding
import com.ilgusu.presentation.util.OnClickRvItemListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewsImageRvAdapter : ListAdapter<ImageInfo, RecyclerView.ViewHolder>(newsDiffCallback) {

    private var selectedItem: ImageInfo? = null
    fun getSelectedItem(): ImageInfo? = selectedItem
    fun resetSelectedItem() {
        selectedItem = null
        notifyDataSetChanged()
    }

    companion object {
        private val newsDiffCallback = object : DiffUtil.ItemCallback<ImageInfo>() {
            override fun areItemsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }

            override fun areContentsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemUploadImageBinding.inflate(layoutInflater, parent, false)
        return NewsImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsImageViewHolder).bind(getItem(position))
    }

    inner class NewsImageViewHolder(private val binding: ItemUploadImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageInfo) {

            binding.tvDate.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (item.imageUrl == selectedItem?.imageUrl) R.color.secondary else R.color.gray_66
                )
            )

            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(binding.ivNewsImage)

            val dateTime = LocalDateTime.parse(item.createdAt)
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            binding.tvDate.text = dateTime.format(formatter)

            binding.root.setOnClickListener {
                selectedItem = item
                onClickRvItemListener.onClick(item)
                notifyDataSetChanged()
            }
        }
    }

    private lateinit var onClickRvItemListener: OnClickRvItemListener<ImageInfo>

    fun setOnRvItemClickListener(onClickRvItemListener: OnClickRvItemListener<ImageInfo>) {
        this.onClickRvItemListener = onClickRvItemListener
    }
}