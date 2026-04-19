# Project Documentation for AI Agents

## Overview
This is a **Compose Multiplatform** project targeting Android and iOS. It follows a **Clean Architecture** pattern with a strict separation of concerns between Data, Domain, and Presentation layers.

### Key Technologies
- **Networking:** Ktor Client with Kotlinx Serialization.
- **Dependency Injection:** Koin.
- **Image Loading:** Coil3.
- **State Management:** `StateFlow` and `SharedFlow` within ViewModels.

---

## Architecture & Module Structure

Every feature module (e.g., `github`, `movie`, `counter`) is organized into the following layers:

### 1. Data Layer (`data/`)
- **`datasource/`**: Interfaces for remote/local data fetching.
- **`dto/`**: Data Transfer Objects representing the raw API response.
- **`service/`**: Implementations of the data sources using Ktor.
- **`repository/`**: Implementation of the domain repository, handles data mapping.
- **`mapper/`**: Extension functions to convert DTOs to Domain Models.

### 2. Domain Layer (`domain/`)
- **`model/`**: Pure Kotlin data classes used across the app.
- **`repository/`**: Interfaces defining the data contracts.
- **`usecase/`**: Business logic units that interact with repositories.

### 3. Presentation Layer (`presentation/`)
- **`screen/`**: Main Composable screens.
- **`composable/`**: Reusable UI components.
- **`viewmodel/`**: Lifecycle-aware components managing UI state.
- **`state/`**: `UiState` (data), `Event` (user actions), and `Effect` (one-time side effects).

---

## API Extraction Patterns

The project demonstrates two common API handling patterns:

### A. Direct Object Extraction (`github` module)
In the GitHub module, the API returns a single JSON object representing the user.
- **Endpoint:** `https://api.github.com/users/$nickname`
- **Pattern:** `response.body<UserDto>()`
- **Data Flow:** The `GitHubApiService` directly returns the `UserDto` which is then mapped to `GithubModel`.

### B. Wrapped List Extraction (`movie` module)
In the Movie module, the API returns a wrapper object containing a list of items.
- **Endpoint:** `https://api.themoviedb.org/3/discover/movie?...`
- **Pattern:** 
  1. Receive `MovieResponseDto` (which contains `val results: List<MovieDto>`).
  2. Extract `body.results`.
- **Data Flow:** `MovieService` fetches the wrapper, extracts the list, and the `MovieRepositoryImpl` maps each `MovieDto` in the list to a `MovieModel`.

---

## Real-World Examples (GitHub vs. Movie)

### 1. Data Layer: API Handling & DTOs

#### GitHub (Direct Object)
```kotlin
// github/data/dto/UserDto.kt
@Serializable
data class UserDto(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val name: String? = null
)

// github/data/service/GitHubApiService.kt
override suspend fun getUser(nickname: String): UserDto {
    val response = client.get("https://api.github.com/users/$nickname")
    return response.body<UserDto>() // Direct extraction
}
```

#### Movie (Wrapped List)
```kotlin
// movie/data/dto/MovieResponseDto.kt
@Serializable
data class MovieResponseDto(
    val results: List<MovieDto> // Wrapper for the list
)

// movie/data/service/MovieService.kt
override suspend fun getList(): List<MovieDto> {
    val response = client.get("https://api.themoviedb.org/3/discover/movie?...")
    val body = response.body<MovieResponseDto>()
    return body.results // Extracting the list from the wrapper
}
```

### 2. Domain Layer: Repository & Use Case

```kotlin
// movie/domain/repository/MovieRepository.kt
interface MovieRepository {
    suspend fun getMovies(): List<MovieModel>
}

// movie/domain/usecase/GetMoviesUseCase.kt
class GetMoviesUseCase(val repository: MovieRepository) {
    suspend fun invoke(): List<MovieModel> = repository.getMovies()
}
```

### 3. Presentation Layer: ViewModel & State

```kotlin
// movie/presentation/state/MovieUiState.kt
data class MovieUiState(
    val isLoading: Boolean = false,
    val list: List<MovieModel> = emptyList()
)

// movie/presentation/viewmodel/MovieViewModel.kt
class MovieViewModel(val moviesUseCase: GetMoviesUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MovieUiState())
    val state = _state.asStateFlow()

    fun load() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val list = moviesUseCase.invoke()
            _state.update { it.copy(list = list, isLoading = false) }
        }
    }
}
```

---

## Dependency Injection (Koin)

The DI configuration is split into modules located in `com.ucb.app.di`:

- **`DataModule.kt`**: Defines singletons for ApiServices, DataSources, and Repositories.
  - *Pattern:* `singleOf(::Implementation).bind<Interface>()`
- **`DomainModule.kt`**: Defines singletons for Use Cases.
- **`PresentationModule.kt`**: Defines ViewModels.
  - *Pattern:* `viewModelOf(::MyViewModel)`
- **`InitKoin.kt`**: Orchestrates the loading of all modules via `getModules()`.

---

## Implementation Details for Agents

### State Management Template
When adding new features, follow this pattern:
1. Define a `MyUiState` data class with defaults.
2. Define a `MyEvent` sealed interface for user interactions.
3. In `MyViewModel`, use `MutableStateFlow` to update state via `_state.update { it.copy(...) }`.

### Mapper Convention
Always use extension functions in the `data/mapper/` package:
```kotlin
fun MyDto.toModel() = MyModel(...)
```

### Resource Management
Strings and Drawables are managed via `composeResources` (e.g., `Res.string.key`). Use `stringResource(Res.string.key)` in Composables.

---

## Navigation

The project uses **Type-Safe Navigation** from the Jetpack Navigation library.

- **`NavRoute.kt`**: Defines a sealed class with `@Serializable` objects representing each screen.
- **`AppNavHost.kt`**: Configures the `NavHost` and maps `NavRoute` objects to their respective `@Composable` screens.

To add a new screen:
1. Add an `@Serializable` object/class to `NavRoute`.
2. Register it in `AppNavHost` using `composable<NavRoute.YourScreen> { YourScreen() }`.
