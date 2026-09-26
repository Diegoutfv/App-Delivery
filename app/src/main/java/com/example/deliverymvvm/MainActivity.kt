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

    // Instanciamos el ViewModel con su Factory (porque tiene dependencia repository)
    private val viewModel: CartViewModel by viewModels {
        CartViewModelFactory(DeliveryRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setupListeners()

        // Primer "intent": cargar el carrito
        viewModel.loadCart()
    }

    private fun observeViewModel() {
        // Observamos los items del carrito
        viewModel.cartItems.observe(this) { items ->
            // Por ahora solo mostramos cuántos items hay
            Toast.makeText(this, "Items cargados: ${items.size}", Toast.LENGTH_SHORT).show()
        }

        // Observamos el total
        viewModel.totalPrice.observe(this) { total ->
            binding.lblTotal.text = "$${String.format("%.2f", total)}"
        }

        // Observamos el estado de carga
        viewModel.isLoading.observe(this) { loading ->
            binding.spinner.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnCheckout.isEnabled = !loading
        }

        // Observamos el resultado del checkout
        viewModel.checkoutResult.observe(this) { success ->
            when (success) {
                true -> Toast.makeText(this, "¡Orden enviada!", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(this, "Error al procesar la orden", Toast.LENGTH_SHORT).show()
                null -> { /* Sin resultado aún */ }
            }
        }
    }

    private fun setupListeners() {
        binding.btnCheckout.setOnClickListener {
            viewModel.checkout()
        }
    }
}