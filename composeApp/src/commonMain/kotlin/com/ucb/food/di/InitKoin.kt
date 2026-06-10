package com.ucb.food.di

import org.koin.core.module.Module

fun getModules(platformModules: List<Module> = emptyList()) = listOf(
    domainModule,
    presentationModule,
    dataModule,
    databaseModule
) + platformModules
