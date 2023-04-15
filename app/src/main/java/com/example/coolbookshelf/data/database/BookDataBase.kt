package com.example.coolbookshelf.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coolbookshelf.data.database.entities.BookEntity
import com.example.coolbookshelf.data.database.entities.FavoritesEntity

@Database(
    entities = [BookEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(BookTypeConverter::class)
abstract class BookDataBase: RoomDatabase(){
    abstract fun bookDao(): BookDao
}