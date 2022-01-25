package io.gamerope.wallet.feature.address

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseTitleSecondActivity
import io.gamerope.wallet.support.BackgroundHelper
import io.gamerope.wallet.support.REQUEST_SCAN_ADDRESS
import io.gamerope.wallet.support.extensions.afterTextChanged
import io.gamerope.wallet.support.extensions.setImage
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : BaseTitleSecondActivity() {

    private lateinit var viewModel: AddAddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        setCenterTitle(R.string.add_address)
        setRightIcon(R.drawable.icon_scan)
        setRightIconClick(View.OnClickListener { scanAddress() })

        viewModel = ViewModelProviders.of(this).get(AddAddressViewModel::class.java)

        notes.editText?.afterTextChanged {
            viewModel.notes.value = it
        }

        address.editText?.afterTextChanged {
            viewModel.addressChanged(it)
        }

        viewModel.scanAddress.observe(this, Observer {
            address.editText?.apply {
                setText(it)
                setSelection(it?.length ?: 0)
            }
        })

        viewModel.addressError.observe(this, Observer {
            if (it != null && it) {
                address.error = getString(R.string.address_invalid)
            } else {
                address.error = null
            }
        })

        viewModel.coin.observe(this, Observer { value ->
            value?.let {
                coin.text = it
                icon.setImage(it)
            }
        })

        confirm.background = BackgroundHelper.getButtonBackground(this)
        confirm.setOnClickListener {
            viewModel.next()
        }

        viewModel.enabled.observe(this, Observer { value ->
            value?.let {
                confirm.isEnabled = it
            }
        })

        viewModel.navigation.observe(this, Observer {
            finish()
        })
    }

    private fun scanAddress() {
        startActivityForResult(Intent(this, ScanActivity::class.java), REQUEST_SCAN_ADDRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleResult(requestCode, resultCode, data)
    }
}