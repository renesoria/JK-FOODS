package com.ucb.food.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            domainModule,
            presentationModule,
            dataModule
            // Quitamos databaseModule temporalmente para que la app no explote
        )
    }

fun getModules() = listOf(
    domainModule,
    presentationModule,
    dataModule
)
