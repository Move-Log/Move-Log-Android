package com.ilgusu.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ilgusu.presentation.databinding.ItemMyRecentNewsRvBinding

class RvMyRecentNewsAdapter : RecyclerView.Adapter<RvMyRecentNewsAdapter.ViewHolder>() {
    var list = mutableListOf<String>()

    inner class ViewHolder(private val binding: ItemMyRecentNewsRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : String) {
            Glide.with(binding.imgRv)
                .load(item)
                .transform(RoundedCorners(40))
                .into(binding.imgRv)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMyRecentNewsRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}