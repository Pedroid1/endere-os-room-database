package com.example.enderecos_room_database.presentation.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enderecos_room_database.R
import com.example.enderecos_room_database.data.AddressDatabase
import com.example.enderecos_room_database.databinding.ActivityAddAddressBinding
import com.example.enderecos_room_database.domain.model.Address
import com.example.enderecos_room_database.presentation.activity.viewmodel.MainViewModel

class AddAddressActivity : AppCompatActivity() {

    companion object {
        const val EDIT_FLAG = "EDIT"
    }

    private var isEditing = false
    private var address: Address? = null

    private lateinit var _binding: ActivityAddAddressBinding
    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(AddressDatabase.getDatabase(this@AddAddressActivity).addressDAO) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initialWork()
    }

    private fun initialWork() {
        getExtras()
        setInputTypes()
        prepareViewListener()
    }

    private fun setInputTypes() {
        _binding.apply {
            edtClient.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            edtStreet.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            edtDistrict.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            edtComplement.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }
    }

    private fun getExtras() {
        val intent = intent
        val extras = intent.extras
        val addressExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras?.getSerializable(EDIT_FLAG, Address::class.java)
        } else {
            extras?.getSerializable(EDIT_FLAG) as? Address
        }
        addressExtra?.let {
            isEditing = true
            _binding.apply {
                edtClient.setText(it.client)
                edtContact.setText(it.contact)
                edtStreet.setText(it.street)
                edtDistrict.setText(it.district)
                edtComplement.setText(it.complement)
                edtNumber.setText(it.number)
                btnAdd.setText(R.string.att)
                icDelete.visibility = View.VISIBLE
                icDelete.setOnClickListener {
                    address?.let {
                        viewModel.removeAddress(it)
                        finish()
                    }
                }
            }
            address = it
        }
    }


    private fun prepareViewListener() {
        _binding.apply {
            btnAdd.setOnClickListener {
                val client = edtClient.text.toString().trim()
                val contact = edtContact.text.toString().trim()
                val street = edtStreet.text.toString().trim()
                val district = edtDistrict.text.toString().trim()
                var complement = edtComplement.text.toString().trim()
                var number = edtNumber.text.toString().trim()

                if (complement.isEmpty()) {
                    complement = "N/A"
                }
                if (number.isEmpty()) {
                    number = "SN"
                }

                if (client.isNotEmpty() && contact.isNotEmpty() && contact.length == 15 && street.isNotEmpty() && district.isNotEmpty()) {
                    if (isEditing) {
                        address?.let {
                            val address = Address(
                                it.uid,
                                client,
                                street,
                                number,
                                district,
                                complement,
                                contact
                            )
                            viewModel.updateAddress(address)
                        }
                    } else {
                        val address =
                            Address(null, client, street, number, district, complement, contact)
                        viewModel.addAddress(address)
                    }

                    finish()
                } else {
                    if (client.isEmpty()) {
                        this.client.error = getString(R.string.required_field)
                    }
                    if(contact.length != 15) {
                        this.contact.error = getString(R.string.invalid_number)
                    }
                    if (contact.isEmpty()) {
                        this.contact.error = getString(R.string.required_field)
                    }
                    if (street.isEmpty()) {
                        this.street.error = getString(R.string.required_field)
                    }
                    if (district.isEmpty()) {
                        this.district.error = getString(R.string.required_field)
                    }
                }
            }

            edtClient.addTextChangedListener {
                this.client.error = null
            }
            edtStreet.addTextChangedListener {
                this.street.error = null
            }
            edtDistrict.addTextChangedListener {
                this.district.error = null
            }

            val textChangedListener = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    contact.error = null
                    edtContact.removeTextChangedListener(this)

                    var text = edtContact.text.toString().replace(Regex("[()-]"), "").replace(" ", "")

                    if (text.length == 11) {
                        text =
                            "(${text.substring(0, 2)}) ${text.substring(2, 7)}-${text.substring(7)}"
                    }
                    edtContact.setText(text)

                    edtContact.text?.let { edtContact.setSelection(it.length) }

                    // Adicione novamente o listener
                    edtContact.addTextChangedListener(this)
                }
            }
            edtContact.addTextChangedListener(textChangedListener)
        }
    }
}