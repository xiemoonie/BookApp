package com.example.coolbookshelf.data.network

import com.example.coolbookshelf.modelstwo.NewBookResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface BookAPI {
    @GET("coolshelf")
    suspend fun getBookResult(
        @QueryMap queries : Map<String, String>
    ): Response<NewBookResult>

    @GET("coolshelf")
    suspend fun searchBooks(
        @QueryMap queries: Map<String, String>
    ): Response<NewBookResult>
}