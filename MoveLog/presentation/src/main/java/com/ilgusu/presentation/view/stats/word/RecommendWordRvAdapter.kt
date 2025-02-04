package com.ilgusu.presentation.view.stats.word

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.presentation.databinding.ItemRecommendWordBinding
import com.ilgusu.presentation.util.OnClickRvItemListener

class RecommendWordRvAdapter : RecyclerView.Adapter<RecommendWordRvAdapter.ViewHolder>() {
    var list = mutableListOf<WordIdStats>()

    private lateinit var onRvItemClickListener: OnClickRvItemListener<WordIdStats>

    fun setOnRvItemClickListener(listener: OnClickRvItemListener<WordIdStats>) {
        onRvItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemRecommendWordBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item : WordIdStats) {
            binding.tvRecommendWordItem.text = "#" + item.noun
            itemView.setOnClickListener {
                onRvItemClickListener.onClick(item)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRecommendWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}