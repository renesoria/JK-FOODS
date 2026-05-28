package com.ucb.food.onboarding.data.mapper

import com.ucb.food.onboarding.data.dto.OnboardingItemDto
import com.ucb.food.onboarding.domain.model.OnboardingItem

fun OnboardingItemDto.toDomain(languageCode: String): OnboardingItem {
    return OnboardingItem(
        id = id,
        title = title[languageCode] ?: title["en"] ?: "",
        description = description[languageCode] ?: description["en"] ?: "",
        imageUrl = imageUrl[languageCode] ?: imageUrl["en"] ?: ""
    )
}
