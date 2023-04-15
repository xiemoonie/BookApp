package com.example.coolbookshelf.modelstwo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewBookResult(
    @SerializedName("items")
    val items: List<Item>
): Parcelable