package com.example.deliverymvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverymvvm.databinding.ItemCartBinding

class CartAdapter(
    private var items: List<CartItem> = emptyList(),
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun submitList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.binding.lblCartName.text = item.name
        holder.binding.lblCartQty.text = item.quantity.toString()
        holder.binding.lblCartSubtotal.text = "$${String.format("%.2f", item.subtotal)}"

        holder.binding.btnPlus.setOnClickListener { onIncrease(item) }
        holder.binding.btnMinus.setOnClickListener { onDecrease(item) }
        holder.binding.btnDelete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount() = items.size
}