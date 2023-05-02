package com.example.enderecos_room_database.presentation.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.enderecos_room_database.databinding.AddressItemBinding
import com.example.enderecos_room_database.domain.model.Address
import com.example.enderecos_room_database.presentation.activity.OnEditAddress

class RvAddressAdapter(private val onEditAddress: OnEditAddress) :
    ListAdapter<Address, RvAddressAdapter.RvAddressViewHolder>(DIFFUTILS) {

    class RvAddressViewHolder(val _binding: AddressItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(address: Address, onEditAddress: OnEditAddress) {
            _binding.address = address
            _binding.onEditListener = onEditAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAddressViewHolder {
        return RvAddressViewHolder(
            AddressItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RvAddressViewHolder, position: Int) {
        holder.bind(getItem(position), onEditAddress)
    }

    companion object {
        val DIFFUTILS = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(
                oldItem: Address, newItem: Address
            ): Boolean {
                return oldItem.uid == newItem.uid && oldItem.client == newItem.client && oldItem.contact == newItem.contact && oldItem.street == newItem.street && oldItem.number == newItem.number && oldItem.district == newItem.district && oldItem.complement == newItem.complement
            }

            override fun areContentsTheSame(
                oldItem: Address, newItem: Address
            ): Boolean {
                return oldItem.uid == newItem.uid && oldItem.client == newItem.client && oldItem.contact == newItem.contact && oldItem.street == newItem.street && oldItem.number == newItem.number && oldItem.district == newItem.district && oldItem.complement == newItem.complement
            }

        }
    }
}