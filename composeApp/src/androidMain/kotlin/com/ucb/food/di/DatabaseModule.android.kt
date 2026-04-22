package com.ucb.food.di

import AppDatabase
import getDatabaseBuilder
import getRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single { getRoomDatabase(getDatabaseBuilder(androidContext())) }
    single { get<AppDatabase>().getDao() }
    single { get<AppDatabase>().getEventDao() }
    single { get<AppDatabase>().getConfigDao() }
}
