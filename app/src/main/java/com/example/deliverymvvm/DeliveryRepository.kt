package com.example.deliverymvvm

class DeliveryRepository {

    // Simulamos una fuente de datos (podría ser una API o BD en un caso real)
    fun getCartItems(): List<CartItem> {
        return listOf(
            CartItem(name = "Pizza Margarita", price = 12.5, quantity = 1),
            CartItem(name = "Refresco", price = 2.0, quantity = 2),
            CartItem(name = "Postre", price = 4.5, quantity = 1)
        )
    }

    // Simulamos el envío de la orden con un callback
    fun placeOrder(items: List<CartItem>, callback: (Boolean) -> Unit) {
        // En un caso real sería una llamada a una API (Retrofit, Ktor, etc.)
        val success = items.isNotEmpty()
        callback(success)
    }
}