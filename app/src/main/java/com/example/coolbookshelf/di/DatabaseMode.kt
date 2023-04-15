package com.example.coolbookshelf.di

import android.content.Context
import androidx.room.Room
import com.example.coolbookshelf.data.database.BookDataBase
import com.example.coolbookshelf.util.Constants
import com.example.coolbookshelf.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseMode {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            BookDataBase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    fun provideDao(database : BookDataBase) = database.bookDao()
}