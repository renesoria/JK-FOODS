package com.ucb.food.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.data.repository.UserDao
import com.ucb.food.profile.domain.usecase.GetUserProfileUseCase
import com.ucb.food.restaurant.domain.usecase.GetUserReviewsUseCase
import com.ucb.food.profile.presentation.state.ProfileEffect
import com.ucb.food.profile.presentation.state.ProfileEvent
import com.ucb.food.profile.presentation.state.ProfileUiState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUserProfileUseCase().flatMapLatest { user ->
                if (user != null) {
                    getUserReviewsUseCase(user.userId).map { reviews ->
                        Triple(user, reviews.size, reviews)
                    }
                } else {
                    flowOf(Triple(null, 0, emptyList()))
                }
            }.collect { (user, count, reviews) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        reviewCount = count,
                        reviews = reviews
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.OnEditProfileClick -> _effect.emit(ProfileEffect.NavigateToEditProfile)
                ProfileEvent.OnLogoutClick -> {
                    Firebase.auth.signOut()
                    userDao.clearAllUsers()
                    _effect.emit(ProfileEffect.NavigateToLogin)
                }
                ProfileEvent.OnBackClick -> _effect.emit(ProfileEffect.NavigateBack)
                ProfileEvent.OnMyReviewsClick -> _effect.emit(ProfileEffect.NavigateToMyReviews)
                ProfileEvent.OnNotificationsClick -> { /* TODO */ }
                ProfileEvent.OnAboutClick -> { /* TODO */ }
            }
        }
    }
}
