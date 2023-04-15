package com.example.coolbookshelf.data.database

import androidx.room.TypeConverter
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.modelstwo.NewBookResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun BookResultToString(bookResult: NewBookResult): String{
        return gson.toJson(bookResult)
    }
    @TypeConverter
    fun stringToBookResult(data: String): NewBookResult {
        val listType = object : TypeToken<NewBookResult>() {}.type
        return gson.fromJson(data, listType)
    }
    @TypeConverter
    fun resultToString(item: Item): String{
        return gson.toJson(item)
    }
    @TypeConverter
    fun stringToResult(data: String): Item{
        val listType = object: TypeToken<Item>() {}.type
        return gson.fromJson(data,listType)
    }
}