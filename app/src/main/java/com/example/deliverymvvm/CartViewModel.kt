package com.example.deliverymvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel(private val repository: DeliveryRepository) : ViewModel() {

    // Estado interno mutable (solo el ViewModel puede modificarlo)
    private val _cartItems = MutableLiveData<List<CartItem>>()
    // Estado expuesto al exterior como inmutable (la View solo lee)
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // null = sin resultado aún | true = éxito | false = error
    private val _checkoutResult = MutableLiveData<Boolean?>()
    val checkoutResult: LiveData<Boolean?> = _checkoutResult

    fun loadCart() {
        _isLoading.value = true
        val items = repository.getCartItems()
        _cartItems.value = items
        _totalPrice.value = items.sumOf { it.subtotal }
        _isLoading.value = false
    }

    fun checkout() {
        _isLoading.value = true
        val items = _cartItems.value ?: emptyList()
        repository.placeOrder(items) { success ->
            _isLoading.value = false
            _checkoutResult.value = success
        }
    }
}