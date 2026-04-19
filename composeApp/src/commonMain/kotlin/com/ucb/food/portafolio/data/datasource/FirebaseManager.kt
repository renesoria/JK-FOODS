package com.ucb.food.portafolio.data.datasource
expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)
}

