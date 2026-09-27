package com.example.deliverymvvm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliverymvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: CartViewModel by viewModels {
        CartViewModelFactory(DeliveryRepository())
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        observeViewModel()
        setupListeners()

        viewModel.loadDishes()
        viewModel.loadCart()
    }

    private fun setupRecyclerViews() {
        binding.recyclerDishes.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(
            onIncrease = { item -> viewModel.increaseItem(item) },
            onDecrease = { item -> viewModel.decreaseItem(item) },
            onDelete   = { item -> viewModel.removeItem(item) }
        )
        binding.recyclerCart.adapter = cartAdapter
    }

    private fun observeViewModel() {
        viewModel.dishes.observe(this) { dishes ->
            binding.recyclerDishes.adapter = DishAdapter(dishes) { dish ->
                viewModel.addToCart(dish)
                Toast.makeText(this, "${dish.name} agregado", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.cartItems.observe(this) { items ->
            cartAdapter.submitList(items)
        }

        viewModel.totalPrice.observe(this) { total ->
            binding.lblTotal.text = "$${String.format("%.2f", total)}"
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.spinner.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnCheckout.isEnabled = !loading
        }

        viewModel.checkoutResult.observe(this) { success ->
            when (success) {
                true -> Toast.makeText(this, "¡Orden enviada!", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(this, "Error al procesar la orden", Toast.LENGTH_SHORT).show()
                null -> { }
            }
        }
    }

    private fun setupListeners() {
        binding.btnCheckout.setOnClickListener {
            viewModel.checkout()
        }
    }
}