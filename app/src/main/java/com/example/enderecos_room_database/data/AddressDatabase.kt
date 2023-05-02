package com.example.enderecos_room_database.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.enderecos_room_database.domain.model.Address

@Database(entities = [Address::class], version = 1, exportSchema = false)
abstract class AddressDatabase : RoomDatabase() {
    abstract val addressDAO: AddressDAO

    companion object {
        @Volatile
        private var INSTANCE: AddressDatabase? = null
        fun getDatabase(context: Context): AddressDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AddressDatabase::class.java,
                    "address_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}