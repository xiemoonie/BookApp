package com.example.coolbookshelf.modelstwo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    @SerializedName("art")
    val art: Boolean,
    @SerializedName("biography")
    val biography: Boolean,
    @SerializedName("description")
    val description: String?,
    @SerializedName("dystopian")
    val dystopian: Boolean,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("fantasy")
    val fantasy: Boolean,
    @SerializedName("historical")
    val historical: Boolean,
    @SerializedName("horror")
    val horror: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("likes")
    val likes: String?,
    @SerializedName("publishedDate")
    val publishedDate: String,
    @SerializedName("romance")
    val romance: Boolean,
    @SerializedName("science")
    val science: Boolean,
    @SerializedName("scifi")
    val scifi: Boolean,
    @SerializedName("selfLink")
    val selfLink: String,
    @SerializedName("selfhelp")
    val selfhelp: Boolean,
    @SerializedName("spirituality")
    val spirituality: Boolean,
    @SerializedName("travel")
    val travel: Boolean,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo?
): Parcelable