# Delivery App - Sprint 1

Aplicacion Android de delivery desarrollada con **Kotlin** y **arquitectura MVVM**.

## Equipo

- Diego Luna Gonzalez
- David Luna Gonzalez

**Materia:** Desarrollo Movil Integral
**Grupo:** DGS 1002 2026-3
**Profesora:** Silvia Guadalupe Bernal Fuentes

## Arquitectura

El proyecto implementa **MVVM (Model-View-ViewModel)**:

- **Model**: CartItem, DeliveryRepository
- **ViewModel**: CartViewModel, CartViewModelFactory
- **View**: MainActivity, activity_main.xml

Adicionalmente, la carpeta demo/ contiene ejemplos de **MVI** y **Clean Architecture** como parte de la investigacion comparativa de arquitecturas.

## Funcionalidades del Sprint 1

- [x] Visualizacion del carrito de compras
- [x] Calculo automatico del total
- [x] Confirmacion del pedido
- [ ] Registro con telefono (proximo sprint)
- [ ] Mapa con restaurantes cercanos (proximo sprint)

## Tecnologias

- Android nativo (Kotlin)
- LiveData + ViewModel (Android Jetpack)
- Coroutines
- View Binding
- Gradle Kotlin DSL

## Como compilar

git clone https://github.com/Diegoutfv/App-Delivery.git

Abrir el proyecto en Android Studio y ejecutar en dispositivo o emulador.

## Estructura del proyecto

app/src/main/java/com/example/deliverymvvm/
  - CartItem.kt
  - DeliveryRepository.kt
  - CartViewModel.kt
  - CartViewModelFactory.kt
  - MainActivity.kt
  - demo/
    - MviDemo.kt
    - CleanDemo.kt