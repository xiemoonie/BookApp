package com.example.coolbookshelf.modelstwo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VolumeInfo(
    @SerializedName("authors")
    val authors: String?,
    @SerializedName("title")
    val title: String
): Parcelable