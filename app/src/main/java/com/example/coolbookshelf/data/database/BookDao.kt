package com.example.coolbookshelf.data.database

import androidx.room.*
import com.example.coolbookshelf.data.database.entities.BookEntity
import com.example.coolbookshelf.data.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertBooks(bookEntity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertFavoriteBooks(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM books_table ORDER BY id ASC")
    fun readBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM favorite_books_table ORDER BY id ASC")
    fun readFavoritesBooks(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteBook(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_books_table")
    suspend fun deleteAllFavoriteBooks()

}
