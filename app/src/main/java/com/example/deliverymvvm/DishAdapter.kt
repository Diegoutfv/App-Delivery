package com.example.deliverymvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliverymvvm.databinding.ItemDishBinding

class DishAdapter(
    private val dishes: List<Dish>,
    private val onAddClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(val binding: ItemDishBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.binding.lblDishName.text = dish.name
        holder.binding.lblDishPrice.text = "$${String.format("%.2f", dish.price)}"
        holder.binding.btnAdd.setOnClickListener { onAddClick(dish) }
    }

    override fun getItemCount() = dishes.size
}