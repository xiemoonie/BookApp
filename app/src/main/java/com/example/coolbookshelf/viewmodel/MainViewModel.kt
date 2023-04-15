package com.example.coolbookshelf.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.coolbookshelf.data.database.entities.BookEntity
import com.example.coolbookshelf.data.Repository
import com.example.coolbookshelf.data.database.entities.FavoritesEntity
import com.example.coolbookshelf.modelstwo.NewBookResult
import com.example.coolbookshelf.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application){

    /**ROOM DATABASE**/
    val readBook: LiveData<List<BookEntity>> = repository.local.readDatabase().asLiveData()
    val readFavoriteBooks: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteBooks().asLiveData()

    fun insertBook(bookEntity: BookEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertBook(bookEntity)
        }
    fun insertFavoriteBook(favoriteEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO){
            repository.local.insertFavoriteBooks(favoriteEntity) }

    fun deleteFavoriteBook(favoriteEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteBook(favoriteEntity)
        }
    fun deleteAllFavoriteBook() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteBooks()
        }

    /**RETROFIT**/
    var bookResponse: MutableLiveData<NetworkResult<NewBookResult>> = MutableLiveData()
    var searchBooksResponse: MutableLiveData<NetworkResult<NewBookResult>> = MutableLiveData()

    fun searchBooks(searchQuery: Map<String, String>) = viewModelScope.launch{
        searchBooksSafeCall(searchQuery)
    }

    private suspend fun searchBooksSafeCall(searchQuery: Map<String, String>){
        bookResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.searchBooks(searchQuery)
                bookResponse.value = handleBooksResponse(response)
            } catch (e: java.lang.Exception) {
                bookResponse.value = NetworkResult.Error("Books not found")
            }
        } else {
            bookResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleBooksResponse(response: Response<NewBookResult>): NetworkResult<NewBookResult>? {
        when {
            response.message().toString().contains("tienes") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.items.isNullOrEmpty() -> {
                return NetworkResult.Error("Books not found")
            }
            response.isSuccessful -> {
                val foodBook = response.body()
                return NetworkResult.Success(foodBook!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    fun getBooks(queries: Map<String, String>) = viewModelScope.launch { getBooksSafeCall(queries) }


private suspend fun getBooksSafeCall(queries: Map<String, String>){
    bookResponse.value = NetworkResult.Loading()
    if(hasInternetConnection()){
        try {
            val response = repository.remote.getBook(queries)
            bookResponse.value = handleBookResponse(response)
            val bookResult = bookResponse.value!!.data
            if (bookResult != null) {
                offlineCacheBooks(bookResult)
            }
        }catch (e : java.lang.Exception){
            bookResponse.value = NetworkResult.Error("Books not found")
            Log.e("books error", "", e)
        }
    }else{
        bookResponse.value = NetworkResult.Error("No internet Connection")
    }
}

    private fun offlineCacheBooks(bookResult: NewBookResult) {
        val bookEntity = BookEntity(bookResult)
        insertBook(bookEntity)

    }

private fun handleBookResponse(response: Response<NewBookResult>): NetworkResult<NewBookResult>?{
    when{
        response.message().toString().contains("tienes") ->{
            return NetworkResult.Error("Timeout")
        }
        response.code() == 402 ->{
            return NetworkResult.Error("API Key Limited")
        }
        response.body()!!.items.isNullOrEmpty()->{
            return NetworkResult.Error("Books not found")
        }
        response.isSuccessful->{
            val bookResult = response.body()
            return NetworkResult.Success(bookResult!!)
        }
        else->{
            return NetworkResult.Error(response.message())
        }
    }
}
private fun hasInternetConnection(): Boolean {
    val connectivityManager = getApplication<Application>().getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
}

