package com.example.coolbookshelf.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.util.Constants.Companion.FAVORITE_BOOKS_TABLE

@Entity(tableName= FAVORITE_BOOKS_TABLE)
class FavoritesEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var item: Item
)