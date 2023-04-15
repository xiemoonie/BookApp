package com.example.coolbookshelf.data

import com.example.coolbookshelf.data.network.BookAPI
import com.example.coolbookshelf.modelstwo.NewBookResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val bookAPI: BookAPI
) {

    suspend fun getBook(queries: Map<String, String>): Response<NewBookResult> {
        return bookAPI.getBookResult(queries)
    }
    suspend fun searchBooks(searchQuery: Map<String, String>): Response<NewBookResult>{
        return bookAPI.searchBooks(searchQuery)
    }
}