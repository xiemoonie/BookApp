package com.example.coolbookshelf.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.coolbookshelf.data.DataStoreRepository
import com.example.coolbookshelf.util.Constants
import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_PRINT_TYPE

import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_Q_KIND

import com.example.coolbookshelf.util.Constants.Companion.QUERY_Q
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var bookKind = DEFAULT_Q_KIND
    private var printType = DEFAULT_PRINT_TYPE

    val readBookKind = dataStoreRepository.readBookType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    var networkStatus = false
    var backOnline = false

    fun saveBookKind(bookKind: String, bookKindId :Int) =
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveBookKind(bookKind, bookKindId)
        }

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline) }


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readBookKind.collect{value ->
                Log.d("values", " " +value.selectedBookKind )
                bookKind = value.selectedBookKind

            }
        }

        queries[QUERY_Q] = bookKind


        return queries

    }
//check constants
    fun applySearchQuery(searchQuery: String): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()
        Log.d("searchQuery", ""+searchQuery);
        queries[QUERY_Q] = searchQuery

        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT)
            saveBackOnline(true)
        }else if(networkStatus){
            if(backOnline){
                Toast.makeText(getApplication(), "We are back online", Toast.LENGTH_SHORT)
               saveBackOnline(false)
            }
        }
    }
}