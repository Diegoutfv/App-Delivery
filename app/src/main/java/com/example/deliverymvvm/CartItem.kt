package com.example.deliverymvvm

data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int = 1
) {
    val subtotal: Double
        get() = price * quantity
}