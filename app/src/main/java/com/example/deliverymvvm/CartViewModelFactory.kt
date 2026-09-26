package com.example.deliverymvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartViewModelFactory(
    private val repository: DeliveryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}