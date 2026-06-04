package com.ucb.food.login.data.repository

import com.ucb.food.login.data.mapper.toDto
import com.ucb.food.login.data.mapper.toEntity
import com.ucb.food.login.data.service.FirebaseAuthService
import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService,
    private val userDao: UserDao
) : AuthenticationRepository {
    override suspend fun login(model: LoginModel) {
        firebaseAuthService.login(model)
        
        // After login, fetch profile and save to Room
        val userId = firebaseAuthService.getCurrentUser()?.uid
        if (userId != null) {
            val profileDto = firebaseAuthService.getProfile(userId)
            if (profileDto != null) {
                userDao.insertUser(profileDto.toEntity())
            }
        }
    }

    override suspend fun signUp(model: LoginModel, profile: UserProfileModel) {
        val userId = firebaseAuthService.signUp(model)
        val profileWithId = profile.copy(userId = userId)
        
        // Save to Firebase Database
        firebaseAuthService.saveProfile(profileWithId.toDto())
        
        // Save to Room
        userDao.insertUser(UserEntity(
            userId = userId,
            firstName = profile.firstName,
            lastName = profile.lastName,
            email = profile.email,
            mobileNumber = profile.mobileNumber,
            gender = profile.gender,
            address = profile.address
        ))
    }

    override suspend fun updateProfile(profile: UserProfileModel) {
        val userId = firebaseAuthService.getCurrentUser()?.uid ?: ""
        val profileWithId = profile.copy(userId = userId)

        // Save to Firebase Database
        firebaseAuthService.saveProfile(profileWithId.toDto())

        // Save to Room
        userDao.insertUser(UserEntity(
            userId = userId,
            firstName = profile.firstName,
            lastName = profile.lastName,
            email = profile.email,
            mobileNumber = profile.mobileNumber,
            gender = profile.gender,
            address = profile.address
        ))
    }

    override fun getUserProfile(): Flow<UserProfileModel?> {
        return userDao.getCurrentUserFlow().map { entity ->
            entity?.let {
                UserProfileModel(
                    userId = it.userId,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    email = it.email,
                    mobileNumber = it.mobileNumber,
                    gender = it.gender,
                    address = it.address
                )
            }
        }
    }

    override suspend fun syncProfile() {
        val userId = firebaseAuthService.getCurrentUser()?.uid
        if (userId != null) {
            val profileDto = firebaseAuthService.getProfile(userId)
            if (profileDto != null) {
                userDao.insertUser(profileDto.toEntity())
            }
        }
    }
}