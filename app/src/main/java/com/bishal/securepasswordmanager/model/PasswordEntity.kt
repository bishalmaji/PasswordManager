package com.bishal.securepasswordmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val username: String,
    val password: String
)
