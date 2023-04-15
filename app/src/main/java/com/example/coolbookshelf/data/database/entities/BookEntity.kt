package com.example.coolbookshelf.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coolbookshelf.modelstwo.NewBookResult
import com.example.coolbookshelf.util.Constants.Companion.BOOK_TABLE



@Entity(tableName = BOOK_TABLE)
class BookEntity( var  book : NewBookResult) {
    @PrimaryKey(
        autoGenerate = false)
        var id: Int = 0
}