package com.example.navitime_challenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "auth")
data class DatabaseAuth constructor(
    @PrimaryKey
    val id: String,
    val token: String)
