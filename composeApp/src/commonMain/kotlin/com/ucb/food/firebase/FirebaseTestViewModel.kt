package com.ucb.food.firebase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.portafolio.data.datasource.FirebaseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import TodoEntity
import TodoDao
import ConfigDao
import ConfigEntity

class FirebaseTestViewModel(
    private val firebaseManager: FirebaseManager,
    private val todoDao: TodoDao,
    private val configDao: ConfigDao
) : ViewModel() {

    var fcmToken by mutableStateOf("Obteniendo...")
    var remoteConfigText by mutableStateOf("Cargando...")
    var realtimeDbStatus by mutableStateOf("Esperando...")
    
    private val _localTodos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val localTodos: StateFlow<List<TodoEntity>> = _localTodos.asStateFlow()

    // Flujo para la configuración cacheada
    private val _cachedConfig = MutableStateFlow<String>("Sin caché")
    val cachedConfig: StateFlow<String> = _cachedConfig.asStateFlow()

    init {
        loadFcmToken()
        loadRemoteConfig()
        observeLocalDatabase()
        observeCachedConfig()
    }

    private fun loadFcmToken() {
        viewModelScope.launch {
            try {
                fcmToken = getToken()
            } catch (e: Exception) {
                fcmToken = "Error: ${e.message}"
            }
        }
    }

    private fun loadRemoteConfig() {
        remoteConfigText = getRemoteConfigString("texto_bienvenida")
    }

    private fun observeLocalDatabase() {
        viewModelScope.launch {
            todoDao.getAllAsFlow().collect {
                _localTodos.value = it
            }
        }
    }

    private fun observeCachedConfig() {
        viewModelScope.launch {
            configDao.getAllConfigsFlow().collect { configs ->
                val welcome = configs.find { it.key == "texto_bienvenida" }
                _cachedConfig.value = welcome?.value ?: "No hay caché guardada"
            }
        }
    }

    fun testRealtimeDatabase(message: String) {
        viewModelScope.launch {
            try {
                realtimeDbStatus = "Guardando..."
                firebaseManager.saveData("test_path", message)
                realtimeDbStatus = "¡Guardado exitoso!"
            } catch (e: Exception) {
                realtimeDbStatus = "Error: ${e.message}"
            }
        }
    }

    fun addLocalTodo(title: String) {
        viewModelScope.launch {
            todoDao.insert(TodoEntity(title = title, content = "Prueba local"))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            todoDao.delete(todo)
        }
    }

    fun clearAllTodos() {
        viewModelScope.launch {
            todoDao.deleteAll()
        }
    }
}
