package com.ucb.food.di

import com.ucb.food.core.presentation.AndroidNotificationHandler
import com.ucb.food.core.presentation.NotificationHandler
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val platformModule = module {
    single<NotificationHandler> { AndroidNotificationHandler(androidContext()) }
}
