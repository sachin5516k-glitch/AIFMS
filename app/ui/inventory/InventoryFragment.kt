package com.ai.franchise.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ai.franchise.R
import com.ai.franchise.data.repository.InventoryRepository

class InventoryFragment : Fragment() {

    private lateinit var viewModel: InventoryViewModel
    private lateinit var adapter: InventoryAdapter
    private lateinit var btnSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)
        
        viewModel = InventoryViewModel(InventoryRepository())
        
        val rvInventory = view.findViewById<RecyclerView>(R.id.rvInventory)
        btnSubmit = view.findViewById(R.id.btnSubmitStock)

        rvInventory.layoutManager = LinearLayoutManager(context)
        adapter = InventoryAdapter(emptyList()) { id, value ->
            viewModel.setClosingStock(id, value)
        }
        rvInventory.adapter = adapter

        btnSubmit.setOnClickListener {
            viewModel.items.value?.getOrNull()?.let { items ->
                viewModel.submitLog(items)
            }
        }

        viewModel.items.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                adapter.updateItems(it)
            }.onFailure {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.submitResult.observe(viewLifecycleOwner) { result ->
             result.onSuccess {
                 Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                 // Optionally clear inputs or refresh
             }.onFailure {
                 Toast.makeText(context, "Submit Failed: ${it.message}", Toast.LENGTH_SHORT).show()
             }
        }

        // Load items on start
        viewModel.fetchItems()

        return view
    }
}
