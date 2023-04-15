package com.example.coolbookshelf.util

class Constants {

    companion object{

        const val BASE_URL = "http://192.168.0.136:1880"

        const val BOOK_RESULT_KEY = "bookBundle"

        //API Query Keys
        const val QUERY_Q = "q"

        //ROOM Database
        const val DATABASE_NAME = "book_dataBase"
        const val BOOK_TABLE = "books_table"
        const val FAVORITE_BOOKS_TABLE = "favorite_books_table"

        //Bottom Sheet and Preferences



        const val DEFAULT_PRINT_TYPE = "book"
        const val DEFAULT_Q_KIND = "horror"

        const val PREFERENCES_Q_ID = "0"
        const val PREFERENCES_PRINT_TYPE_ID = "0"

        const val PREFERENCES_Q_KIND = "horror"
        const val PREFERENCES_PRINT_TYPE = "book"

        const val PREFERENCES_NAME = "AppPreferences"
        const val PREFERENCES_BACK_ONLINE = "backOnline"


    }
}