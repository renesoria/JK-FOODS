# JK-FOODS Project Architecture & Guidelines (Single Source of Truth)

## 🏗 Overview
This is a **Compose Multiplatform** (Android & iOS) project built following **Clean Architecture** principles. The architecture is designed to be scalable, testable, and maintainable by strictly separating concerns into layers.

---

## 🏛 Layered Architecture (Radiography)

Each feature module (e.g., `movie`, `github`, `login`, `fakestore`) must follow this three-layer structure:

### 1. Data Layer (`data/`)
**Responsibility**: Managing raw data from external sources (APIs, Databases, Local Storage).
- **`dto/`**: Data Transfer Objects. Pure data classes annotated with `@Serializable`. They represent the raw structure of the API response.
- **`service/`**: API definitions and implementations using **Ktor Client**.
- **`datasource/`**: Interfaces and implementations for low-level data access (Remote vs Local).
- **`repository/`**: Implementation of the domain-level repository interface. This is the "brain" of the data layer, responsible for:
    - Coordinating multiple data sources (e.g., Network + Cache).
    - Error handling (mapping exceptions to domain errors).
    - Mapping DTOs to Domain Models using **Mappers**.
- **`mapper/`**: Extension functions that convert `Dto` objects into `Model` (Domain) objects.

### 2. Domain Layer (`domain/`)
**Responsibility**: Containing the core business logic. It is the most stable layer and has **NO** dependencies on external frameworks or other layers.
- **`model/`**: Pure Kotlin data classes representing business entities. No annotations (like `@Serializable`) should be here.
- **`repository/`**: Interfaces defining the contracts for data access. The implementation resides in the Data layer.
- **`usecase/`**: Single-responsibility classes that execute a specific piece of business logic. They interact only with repositories.

### 3. Presentation Layer (`presentation/`)
**Responsibility**: Rendering the UI and managing user interaction.
- **`screen/`**: High-level `@Composable` functions representing full screens. They should not contain logic, only layout and calls to the ViewModel.
- **`viewmodel/`**: Lifecycle-aware components that hold the UI state and handle user actions. They communicate with the Domain layer via UseCases.
- **`state/`**: Definitions for UI-specific data structures:
    - **`UiState`**: A single data class representing the entire screen state.
    - **`Event`**: A sealed interface/class representing user actions (e.g., button clicks).
    - **`Effect`**: A sealed interface representing side effects (e.g., navigation, showing a snackbar).
- **`composable/`**: Small, reusable UI components used within screens.

---

## 📐 Dependency Rules (The Golden Rule)
To maintain Clean Architecture, dependencies must flow **inwards**:
1.  **Domain Layer** is the center. It knows **nothing** about Data or Presentation.
2.  **Data Layer** depends on the **Domain Layer** (to implement repository interfaces).
3.  **Presentation Layer** depends on the **Domain Layer** (to use UseCases and Models).
4.  **Presentation Layer** must **NEVER** depend on the **Data Layer** directly (e.g., no DTOs in ViewModels).

---

## 🏷 Naming Conventions
Consistency is key. Use the following suffixes:

| Component | Suffix | Example |
| :--- | :--- | :--- |
| Data Transfer Object | `Dto` | `MovieDto.kt` |
| Domain Model | `Model` | `MovieModel.kt` |
| Repository Interface | `Repository` | `MovieRepository.kt` |
| Repository Impl | `RepositoryImpl` | `MovieRepositoryImpl.kt` |
| Use Case | `UseCase` | `GetMoviesUseCase.kt` |
| ViewModel | `ViewModel` | `MovieViewModel.kt` |
| UI State | `UiState` or `State` | `MovieUiState.kt` |
| UI Event | `Event` | `MovieEvent.kt` |
| UI Effect | `Effect` | `MovieEffect.kt` |
| Mapper File | `...Mapper.kt` | `MovieMapper.kt` |

---

## ⚡ State Management (MVI-ish Pattern)

Every ViewModel must implement the following pattern:

### 1. The State (`UiState`)
A `data class` representing everything the UI needs. Use `val` for all properties and provide default values.
```kotlin
data class MyUiState(
    val isLoading: Boolean = false,
    val items: List<MyModel> = emptyList(),
    val error: String? = null
)
```

### 2. The Events (`Event`)
A `sealed interface` for all user actions.
```kotlin
sealed interface MyEvent {
    data object OnLoadRequested : MyEvent
    data class OnItemClicked(val id: String) : MyEvent
}
```

### 3. The Effects (`Effect`)
A `sealed interface` for one-time side effects. Use a `Channel` in the ViewModel to emit them.
```kotlin
sealed interface MyEffect {
    data class ShowSnackbar(val message: String) : MyEffect
    data class NavigateToDetail(val id: String) : MyEffect
}
```

### 4. ViewModel Implementation Template
```kotlin
class MyViewModel(private val myUseCase: MyUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MyUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MyEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: MyEvent) {
        when(event) {
            is MyEvent.OnLoadRequested -> loadData()
            is MyEvent.OnItemClicked -> navigateToDetail(event.id)
        }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val result = myUseCase.invoke()
                _state.update { it.copy(items = result, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
                _effect.send(MyEffect.ShowSnackbar("Error loading data"))
            }
        }
    }
}
```

---

## 💉 Dependency Injection (Koin)

DI modules are organized by layer in `com.ucb.food.di`:
- **`DataModule.kt`**: Services, DataSources, and Repositories.
    - Pattern: `singleOf(::Implementation).bind<Interface>()`
- **`DomainModule.kt`**: UseCases.
    - Pattern: `singleOf(::MyUseCase)`
- **`PresentationModule.kt`**: ViewModels.
    - Pattern: `viewModelOf(::MyViewModel)`
- **`InitKoin.kt`**: Main entry point for starting Koin.

---

## 📡 API Handling Patterns

### A. Direct Object Extraction
When the API returns a single object.
```kotlin
override suspend fun getData(): MyDto {
    val response = client.get("https://api.example.com/data")
    return response.body<MyDto>()
}
```

### B. Wrapped List Extraction
When the API returns a list inside a wrapper object.
```kotlin
@Serializable
data class MyResponseDto(val results: List<MyDto>)

override suspend fun getList(): List<MyDto> {
    val response = client.get("https://api.example.com/list")
    val body = response.body<MyResponseDto>()
    return body.results
}
```

---

## 🗺 Navigation
The project uses **Type-Safe Navigation**.
- **`NavRoute.kt`**: Defines routes as `@Serializable` objects or classes.
- **`AppNavHost.kt`**: Maps routes to Screen Composables using `composable<NavRoute.X> { ... }`.
- Navigation should be handled via callbacks (`onNavigateToX`) passed to screens to keep them decoupled from `NavController`.

---

## 🎨 Resources & Styling
- **Strings**: Use `Res.string.key` with `stringResource()`.
- **Images**: Use `Res.drawable.key` with `painterResource()`.
- **Colors & Typography**: Use the **Design System** module (`DsTheme`).

---

## 📝 Best Practices & Rules
- **No Logic in Composables**: Keep them pure UI.
- **Extension Mappers**: Always map DTOs to Models in the Data layer.
- **Immutable State**: Use `copy()` to update `UiState`.
- **Error Handling**: Don't leak raw exceptions to the UI. Map them to readable error messages.
- **Constructor Injection**: Always inject dependencies via constructor parameters.
- **Module Structure**: Keep each feature contained within its own package under `com.ucb.food`.
