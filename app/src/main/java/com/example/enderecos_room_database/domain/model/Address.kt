package com.example.enderecos_room_database.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class Address(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    val client: String,
    val street: String,
    val number: String,
    val district: String,
    val complement: String,
    val contact: String
) : java.io.Serializable