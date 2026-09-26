package com.example.deliverymvvm.demo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

// ============================================================
// 1. DOMAIN LAYER
//    Sin dependencias de frameworks, UI, ni Android.
//    Solo reglas de negocio puras.
// ============================================================

// Entidad del dominio
data class CleanCartItem(
    val name: String,
    val price: Double,
    val quantity: Int = 1
) {
    val subtotal: Double get() = price * quantity
}

data class CleanOrder(
    val id: String,
    val items: List<CleanCartItem>,
    val address: String,
    val deliveryFee: Double,
    val total: Double
)

// Contrato del repositorio (abstracción — no implementación)
interface CleanOrderRepository {
    suspend fun sendOrder(order: CleanOrder): Boolean
}

// Caso de Uso: contiene la lógica de negocio de "hacer un pedido"
// - Valida que el carrito no esté vacío
// - Calcula costo de envío (gratis si > 50.0)
// - Crea la entidad Order
// - Delega el envío al repositorio
class PlaceOrderUseCase(private val repository: CleanOrderRepository) {

    suspend fun execute(items: List<CleanCartItem>, address: String): Result<CleanOrder> {
        if (items.isEmpty()) {
            return Result.failure(Exception("El carrito está vacío"))
        }

        val subtotal = items.sumOf { it.subtotal }
        val deliveryFee = if (subtotal > 50.0) 0.0 else 5.0
        val total = subtotal + deliveryFee

        val order = CleanOrder(
            id = UUID.randomUUID().toString(),
            items = items,
            address = address,
            deliveryFee = deliveryFee,
            total = total
        )

        val isSuccess = repository.sendOrder(order)
        return if (isSuccess) {
            Result.success(order)
        } else {
            Result.failure(Exception("El servidor rechazó la orden"))
        }
    }
}

// ============================================================
// 2. DATA LAYER
//    Implementación concreta del repositorio (simulada).
// ============================================================

class CleanOrderRepositoryImpl : CleanOrderRepository {
    override suspend fun sendOrder(order: CleanOrder): Boolean {
        delay(800) // Simulamos llamada a API
        println(">> [DATA] Enviando orden ${order.id.take(8)}...")
        println("   Items: ${order.items.size}")
        println("   Delivery fee: $${order.deliveryFee}")
        println("   Total: $${order.total}")
        return true
    }
}

// ============================================================
// 3. PRESENTATION LAYER
//    El ViewModel depende del UseCase (no del repositorio directo).
// ============================================================

class CleanCartViewModel(private val placeOrderUseCase: PlaceOrderUseCase) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun onConfirmClicked(items: List<CleanCartItem>, address: String) {
        scope.launch {
            println("\n>> [PRESENTATION] Usuario confirma orden...")

            val result = placeOrderUseCase.execute(items, address)

            result.onSuccess { order ->
                println("✅ [PRESENTATION] Orden creada con éxito:")
                println("   ID: ${order.id}")
                println("   Total final: $${order.total}")
            }.onFailure { error ->
                println("❌ [PRESENTATION] Error: ${error.message}")
            }
        }
    }
}

// ============================================================
// 4. MAIN (punto de entrada para ejecutar si se desea)
//    En Android Studio puede que no se ejecute desde `app`,
//    pero sirve como demostración del flujo.
// ============================================================

fun main() {
    println("╔══════════════════════════════════════════════╗")
    println("║   DEMO CLEAN ARCHITECTURE - Delivery         ║")
    println("╚══════════════════════════════════════════════╝")

    // 1. Montamos las dependencias (en Android esto sería con Hilt/Koin)
    val repository: CleanOrderRepository = CleanOrderRepositoryImpl()
    val useCase = PlaceOrderUseCase(repository)
    val viewModel = CleanCartViewModel(useCase)

    // 2. Simulamos datos del carrito
    val items = listOf(
        CleanCartItem(name = "Pizza Margarita", price = 12.5, quantity = 1),
        CleanCartItem(name = "Refresco", price = 2.0, quantity = 2),
        CleanCartItem(name = "Postre", price = 4.5, quantity = 1)
    )

    // 3. El usuario confirma
    viewModel.onConfirmClicked(items, address = "Av. Reforma 123, CDMX")

    Thread.sleep(2000)
    println("\n✓ Demo Clean Architecture finalizada.")
    println("  Flujo: View → ViewModel → UseCase → Repository → Data")
}