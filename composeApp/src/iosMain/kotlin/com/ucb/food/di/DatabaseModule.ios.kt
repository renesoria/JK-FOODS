package com.ucb.food.di

import AppDatabase
import getDatabaseBuilder
import getRoomDatabase
import org.koin.dsl.module

actual val databaseModule = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().getDao() }
}
