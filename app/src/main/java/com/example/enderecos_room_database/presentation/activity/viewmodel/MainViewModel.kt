package com.example.enderecos_room_database.presentation.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enderecos_room_database.data.AddressDAO
import com.example.enderecos_room_database.domain.model.Address
import kotlinx.coroutines.launch

class MainViewModel(private val addressDAO: AddressDAO) : ViewModel() {

    suspend fun fetchAddress() : List<Address> {
        return addressDAO.loadAddresses()
    }

    fun addAddress(address: Address) = viewModelScope.launch {
        addressDAO.insertAddress(address)
    }

    fun removeAddress(address: Address) = viewModelScope.launch {
        addressDAO.deleteAddress(address)
    }

    fun updateAddress(address: Address) = viewModelScope.launch {
        addressDAO.updateAddress(address)
    }
}