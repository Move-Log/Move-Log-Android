package com.ilgusu.presentation.view.stats.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.presentation.databinding.ItemSearchResultBinding
import com.ilgusu.presentation.util.OnClickRvItemListener

class SearchResultRvAdapter : RecyclerView.Adapter<SearchResultRvAdapter.ViewHolder>() {
    var list = mutableListOf<WordIdStats>()

    private lateinit var onRvItemClickListener: OnClickRvItemListener<WordIdStats>

    fun setOnRvItemClickListener(listener: OnClickRvItemListener<WordIdStats>) {
        onRvItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : WordIdStats) {
            binding.tvSearchResultItem.text = item.noun
            itemView.setOnClickListener {
                onRvItemClickListener.onClick(item)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}