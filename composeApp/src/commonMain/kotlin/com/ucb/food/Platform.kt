package com.ucb.food

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform