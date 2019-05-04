package com.liyawei.nbateamviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Team(
    @PrimaryKey val id: Int,
    @SerializedName("full_name") val fullName: String,
    val wins: Int,
    val losses: Int
)