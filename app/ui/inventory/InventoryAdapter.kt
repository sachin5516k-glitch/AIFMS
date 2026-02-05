package com.ai.franchise.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ai.franchise.databinding.ItemInventoryRowBinding

class InventoryAdapter(
    private val onStockChanged: () -> Unit
) : ListAdapter<InventoryItem, InventoryAdapter.InventoryViewHolder>(InventoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding = ItemInventoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    // Explicitly expose items list for ViewModel validation
    fun getItems(): List<InventoryItem> = currentList

    inner class InventoryViewHolder(private val binding: ItemInventoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InventoryItem) {
            binding.tvItemName.text = item.name
            binding.tvOpeningStock.text = "Opening: ${item.openingStock}"
            
            // Clear previous listeners
            binding.etClosingStock.onFocusChangeListener = null
            
            // Initial Set
            if (binding.etClosingStock.text.toString() != (item.closingStock?.toString() ?: "")) {
                binding.etClosingStock.setText(item.closingStock?.toString() ?: "")
            }

            // Text Watcher
            binding.etClosingStock.doAfterTextChanged { 
                 // We are modifying the item in place. In strict immutable ListAdapter this is bad,
                 // but for form input buffers it is pragmatic.
                val input = it.toString().toDoubleOrNull()
                item.closingStock = input
                
                if (input != null) {
                    item.variance = input - item.openingStock
                    binding.tvVariance.text = "Change: ${String.format("%.2f", item.variance)}"
                } else {
                    binding.tvVariance.text = "Var: 0.0"
                }
                
                onStockChanged()
            }
        }
    }
}

class InventoryDiffCallback : DiffUtil.ItemCallback<InventoryItem>() {
    override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        // Since we mutate closingStock in place for form logic, simple equality check works for list refreshes
        // unless we need to detect closing stock changes from external sources.
        return oldItem == newItem
    }
}
