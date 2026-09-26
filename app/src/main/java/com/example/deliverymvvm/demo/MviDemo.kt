package com.example.deliverymvvm.demo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// ============================================
// 1. MODEL (Entidad)
// ============================================
data class DemoCartItem(
    val name: String,
    val price: Double,
    val quantity: Int = 1
) {
    val subtotal: Double get() = price * quantity
}

// ============================================
// 2. STATE (Todo el estado de la pantalla)
// ============================================
data class DemoCartViewState(
    val items: List<DemoCartItem> = emptyList(),
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val orderSuccess: Boolean = false,
    val errorMessage: String? = null
)

// ============================================
// 3. INTENT (Acciones del usuario)
// ============================================
sealed class DemoCartIntent {
    object LoadCart : DemoCartIntent()
    object ConfirmOrder : DemoCartIntent()
}

// ============================================
// 4. REPOSITORY (Simulado)
// ============================================
class DemoDeliveryRepository {

    suspend fun getCartItems(): List<DemoCartItem> {
        delay(500)
        return listOf(
            DemoCartItem(name = "Pizza Margarita", price = 12.5, quantity = 1),
            DemoCartItem(name = "Refresco", price = 2.0, quantity = 2),
            DemoCartItem(name = "Postre", price = 4.5, quantity = 1)
        )
    }

    suspend fun placeOrder(items: List<DemoCartItem>): Boolean {
        delay(800)
        return items.isNotEmpty()
    }
}

// ============================================
// 5. VIEWMODEL (MVI: procesa Intents, emite States)
// ============================================
class MviCartViewModel(private val repository: DemoDeliveryRepository) {

    private val _state = MutableStateFlow(DemoCartViewState())
    val state: StateFlow<DemoCartViewState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun processIntent(intent: DemoCartIntent) {
        when (intent) {
            is DemoCartIntent.LoadCart -> loadCart()
            is DemoCartIntent.ConfirmOrder -> submitOrder()
        }
    }

    private fun loadCart() {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val items = repository.getCartItems()
                val total = items.sumOf { it.subtotal }
                _state.value = _state.value.copy(
                    items = items,
                    total = total,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    private fun submitOrder() {
        scope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val success = repository.placeOrder(_state.value.items)
                _state.value = _state.value.copy(
                    isLoading = false,
                    orderSuccess = success,
                    errorMessage = if (!success) "No se pudo procesar la orden" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    orderSuccess = false,
                    errorMessage = e.message
                )
            }
        }
    }
}

// ============================================
// 6. VIEW (Simulada con println)
// ============================================
class MviCartView(private val viewModel: MviCartViewModel) {

    fun setup() {
        println(">> [VIEW] Iniciando pantalla...")

        Thread {
            repeat(20) {
                renderUI(viewModel.state.value)
                Thread.sleep(200)
            }
        }.start()

        viewModel.processIntent(DemoCartIntent.LoadCart)

        Thread {
            Thread.sleep(2000)
            println("\n>> [VIEW] Usuario toca 'Confirmar Orden'")
            viewModel.processIntent(DemoCartIntent.ConfirmOrder)
        }.start()
    }

    private fun renderUI(state: DemoCartViewState) {
        val status = buildString {
            append("\n──────── ESTADO ACTUAL ────────\n")
            append("Items: ${state.items.size}\n")
            append("Total: $${String.format("%.2f", state.total)}\n")
            append("Loading: ${state.isLoading}\n")
            append("Order success: ${state.orderSuccess}\n")
            append("Error: ${state.errorMessage ?: "ninguno"}\n")
            append("───────────────────────────────")
        }
        println(status)
    }
}
