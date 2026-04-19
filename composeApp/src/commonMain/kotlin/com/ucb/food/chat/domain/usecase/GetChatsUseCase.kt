package com.ucb.food.chat.domain.usecase

import com.ucb.food.chat.domain.model.ChatModel
import com.ucb.food.chat.domain.repository.ChatRepository

class GetChatsUseCase(
    private val chatRepository: ChatRepository
) {
    suspend fun invoke(userId: String): List<ChatModel> {
        return chatRepository.getChats(userId)
    }
}