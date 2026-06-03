package com.ucb.food.login.data.service

import com.ucb.food.login.domain.model.LoginModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

// Using GitLive Firebase for KMP support (assuming user wants KMP compatibility)
// Note: This requires the GitLive dependency, which we will add next if needed.
// For now, let's implement the logic assuming the library is available.

class FirebaseAuthService {
    private val auth = Firebase.auth

    suspend fun login(loginModel: LoginModel) {
        auth.signInWithEmailAndPassword(loginModel.email, loginModel.password)
    }

    suspend fun signUp(loginModel: LoginModel) {
        auth.createUserWithEmailAndPassword(loginModel.email, loginModel.password)
    }

    fun getCurrentUser() = auth.currentUser
}