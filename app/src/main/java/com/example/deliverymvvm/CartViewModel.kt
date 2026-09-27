package com.example.deliverymvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel(private val repository: DeliveryRepository) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    private val _dishes = MutableLiveData<List<Dish>>(emptyList())
    val dishes: LiveData<List<Dish>> = _dishes

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _checkoutResult = MutableLiveData<Boolean?>()
    val checkoutResult: LiveData<Boolean?> = _checkoutResult

    fun loadCart() {
        _isLoading.value = true
        val items = repository.getCartItems()
        _cartItems.value = items
        _totalPrice.value = items.sumOf { it.subtotal }
        _isLoading.value = false
    }

    fun loadDishes() {
        _dishes.value = repository.getAvailableDishes()
    }

    fun addToCart(dish: Dish) {
        val current = _cartItems.value.orEmpty().toMutableList()
        val index = current.indexOfFirst { it.name == dish.name }

        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(name = dish.name, price = dish.price, quantity = 1))
        }

        _cartItems.value = current
        _totalPrice.value = current.sumOf { it.subtotal }
    }

    fun increaseItem(item: CartItem) {
        val current = _cartItems.value.orEmpty().toMutableList()
        val index = current.indexOfFirst { it.name == item.name }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
            _cartItems.value = current
            _totalPrice.value = current.sumOf { it.subtotal }
        }
    }

    fun decreaseItem(item: CartItem) {
        val current = _cartItems.value.orEmpty().toMutableList()
        val index = current.indexOfFirst { it.name == item.name }
        if (index >= 0) {
            val newQty = current[index].quantity - 1
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cartItems.value = current
            _totalPrice.value = current.sumOf { it.subtotal }
        }
    }

    fun removeItem(item: CartItem) {
        val current = _cartItems.value.orEmpty().toMutableList()
        current.removeAll { it.name == item.name }
        _cartItems.value = current
        _totalPrice.value = current.sumOf { it.subtotal }
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