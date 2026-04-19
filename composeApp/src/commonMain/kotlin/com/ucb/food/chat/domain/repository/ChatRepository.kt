package com.ucb.food.chat.domain.repository

import com.ucb.food.chat.domain.model.ChatModel
import com.ucb.food.chat.domain.model.MessageModel

interface ChatRepository {
    suspend fun getChats(userId: String): List<ChatModel>
    suspend fun getChat(chatId: String): ChatModel
    suspend fun createChatIndividual(): ChatModel
    suspend fun createChatGroup(): ChatModel
    suspend fun getMessages(chatId: String): List<MessageModel>
}