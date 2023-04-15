package com.example.coolbookshelf.data

import com.example.coolbookshelf.data.database.BookDao
import com.example.coolbookshelf.data.database.entities.BookEntity
import com.example.coolbookshelf.data.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val bookDao: BookDao) {

     fun readDatabase(): Flow<List<BookEntity>> {
        return bookDao.readBooks()
    }
    suspend fun insertBook(bookEntity: BookEntity){
        return bookDao.insertBooks((bookEntity))
    }

    fun readFavoriteBooks(): Flow<List<FavoritesEntity>>{
        return bookDao.readFavoritesBooks()
    }

    suspend fun insertFavoriteBooks(favoritesEntity: FavoritesEntity){
        bookDao.insertFavoriteBooks(favoritesEntity)
    }

    suspend fun deleteFavoriteBook(favoritesEntity: FavoritesEntity){
        bookDao.deleteFavoriteBook(favoritesEntity)
    }

    suspend fun deleteAllFavoriteBooks(){
        bookDao.deleteAllFavoriteBooks()
    }

}