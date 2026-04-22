package com.ucb.food.di

fun getModules() = listOf(
    domainModule,
    presentationModule,
    dataModule,
    databaseModule
)