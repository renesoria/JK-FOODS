package com.ucb.food.login.data.service

import com.ucb.food.login.data.dto.UserProfileDto
import com.ucb.food.login.domain.model.LoginModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.first

class FirebaseAuthService {
    private val auth = Firebase.auth

    suspend fun login(loginModel: LoginModel) {
        auth.signInWithEmailAndPassword(loginModel.email, loginModel.password)
    }

    suspend fun signUp(loginModel: LoginModel): String {
        val result = auth.createUserWithEmailAndPassword(loginModel.email, loginModel.password)
        return result.user?.uid ?: throw Exception("User creation failed: No UID")
    }

    suspend fun saveProfile(profile: UserProfileDto) {
        val database = Firebase.database.reference()
        database.child("users").child(profile.userId).setValue(profile)
    }

    suspend fun getProfile(userId: String): UserProfileDto? {
        val database = Firebase.database.reference()
        return try {
            val snapshot = database.child("users").child(userId).valueEvents.first()
            if (snapshot.exists) {
                snapshot.value<UserProfileDto>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentUser() = auth.currentUser
}
