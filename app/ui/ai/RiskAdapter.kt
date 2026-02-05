package com.ai.franchise.ui.ai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ai.franchise.databinding.ItemAiRiskBinding

class RiskAdapter(
    private val onDisputeClicked: (RiskItem) -> Unit
) : ListAdapter<RiskItem, RiskAdapter.RiskViewHolder>(RiskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskViewHolder {
        val binding = ItemAiRiskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RiskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RiskViewHolder(private val binding: ItemAiRiskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RiskItem) {
            binding.tvTitle.text = item.title
            binding.tvReason.text = item.reason
            binding.tvRecommendation.text = item.recommendation
            
            // Toggle Expand
            binding.layoutDetails.visibility = if (item.isExpanded) View.VISIBLE else View.GONE
            binding.ivExpand.rotation = if (item.isExpanded) 180f else 0f
            
            binding.root.setOnClickListener {
                // UI Logic: In a real app we might want to do this via ViewModel/ListAdapter modification 
                // to trigger proper animation, but for now simple toggle is fine if we notify change.
                // However, ListAdapter is immutable-ish. Better to copy item or use notifyItemChanged.
                item.isExpanded = !item.isExpanded
                notifyItemChanged(adapterPosition)
            }
            
            binding.btnDispute.setOnClickListener {
                onDisputeClicked(item)
            }
        }
    }
}

class RiskDiffCallback : DiffUtil.ItemCallback<RiskItem>() {
    override fun areItemsTheSame(oldItem: RiskItem, newItem: RiskItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RiskItem, newItem: RiskItem): Boolean {
        return oldItem == newItem
    }
}
