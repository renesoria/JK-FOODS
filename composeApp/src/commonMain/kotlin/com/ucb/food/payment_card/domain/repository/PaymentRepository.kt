package com.ucb.food.payment_card.domain.repository

import com.ucb.food.payment_card.domain.model.CreditCard

interface PaymentRepository {
    suspend fun executePayment(
        paymentModel: CreditCard,
        price: Double
    ): Boolean
}