package com.ucb.food.chat.domain.usecase

import com.ucb.food.chat.domain.model.MessageModel
import com.ucb.food.chat.domain.repository.ChatRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend fun invoke(message: MessageModel, chatId: String) {

    }
}

// Individuales, Grupales