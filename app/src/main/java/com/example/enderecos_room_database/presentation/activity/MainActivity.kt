package com.example.enderecos_room_database.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.enderecos_room_database.data.AddressDatabase
import com.example.enderecos_room_database.databinding.ActivityMainBinding
import com.example.enderecos_room_database.domain.model.Address
import com.example.enderecos_room_database.presentation.activity.adapter.RvAddressAdapter
import com.example.enderecos_room_database.presentation.activity.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnEditAddress {

    private lateinit var _binding: ActivityMainBinding
    private val rvAddressAdapter: RvAddressAdapter = RvAddressAdapter(this)
    private var addressList: List<Address> = ArrayList()
    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(AddressDatabase.getDatabase(this@MainActivity).addressDAO) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initialWork()
    }

    private fun initialWork() {
        _binding.rvMain.adapter = rvAddressAdapter
        prepareViewListener()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            addressList = viewModel.fetchAddress()
            rvAddressAdapter.submitList(addressList)
        }
    }

    private fun prepareViewListener() {
        _binding.apply {
            fabAddAddress.setOnClickListener {
                val intent = Intent(applicationContext, AddAddressActivity::class.java)
                startActivity(intent)
            }

            searchView.setOnQueryTextListener(object : OnQueryTextListener,
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }

            })
        }
    }

    private fun filterList(newText: String?) {
        newText?.let { text ->
            val filteredList =
                addressList.filter { it.client.contains(text.trim(), true) }.toMutableList()
            rvAddressAdapter.submitList(filteredList)
        }
    }

    override fun onEdit(address: Address) {
        editAddress(address)
    }

    private fun editAddress(address: Address) {
        val intent = Intent(applicationContext, AddAddressActivity::class.java)
        intent.putExtra(AddAddressActivity.EDIT_FLAG, address)
        startActivity(intent)
    }
}

interface OnEditAddress {
    fun onEdit(address: Address)
}