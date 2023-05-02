package com.example.enderecos_room_database.data

import androidx.room.*
import com.example.enderecos_room_database.domain.model.Address

@Dao
interface AddressDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Update
    suspend fun updateAddress(address: Address)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("SELECT * FROM address ORDER BY client ASC")
    suspend fun loadAddresses(): List<Address>
}