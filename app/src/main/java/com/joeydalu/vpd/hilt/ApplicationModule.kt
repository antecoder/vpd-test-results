/*
 * Copyright (c) 2024 . All rights reserved.
 */

package com.joeydalu.vpd.hilt

import android.content.Context
import androidx.room.Room
import com.joeydalu.vpd.data.database.AppDatabase
import com.joeydalu.vpd.auth.Authenticator
import com.joeydalu.vpd.auth.FirebaseAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideAuthenticator(): Authenticator =
        FirebaseAuthenticator()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "vpd-base").build()
    }

}
