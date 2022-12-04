package com.aritra.goldmannasa.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apod_table")
data class APODEntity(
    val copyright: String = "",
    @PrimaryKey
    val date: String,
    val explanation: String,
    val hdurl: String?,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String,
    val isFavourite:Boolean =false
)