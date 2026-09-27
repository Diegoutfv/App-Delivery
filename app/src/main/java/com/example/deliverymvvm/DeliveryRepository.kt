package com.example.deliverymvvm

class DeliveryRepository {

    fun getAvailableDishes(): List<Dish> {
        return listOf(
            Dish(1, "Pizza Margarita", 12.5),
            Dish(2, "Hamburguesa", 8.0),
            Dish(3, "Tacos al Pastor", 6.5),
            Dish(4, "Refresco", 2.0),
            Dish(5, "Postre", 4.5),
            Dish(6, "Ensalada César", 7.0)
        )
    }

    fun getCartItems(): List<CartItem> {
        return listOf(
            CartItem(name = "Pizza Margarita", price = 12.5, quantity = 1),
            CartItem(name = "Refresco", price = 2.0, quantity = 2),
            CartItem(name = "Postre", price = 4.5, quantity = 1)
        )
    }

    fun placeOrder(items: List<CartItem>, callback: (Boolean) -> Unit) {
        val success = items.isNotEmpty()
        callback(success)
    }
}