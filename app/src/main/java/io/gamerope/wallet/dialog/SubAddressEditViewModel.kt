package io.gamerope.wallet.dialog

import androidx.lifecycle.MutableLiveData
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.core.XMRWalletController
import io.gamerope.wallet.data.entity.SubAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubAddressEditViewModel : BaseViewModel() {
    val success = MutableLiveData<Boolean>()

    val showLoading = MutableLiveData<Boolean>()
    val hideLoading = MutableLiveData<Boolean>()
    val toastRes = MutableLiveData<Int>()

    fun addSubAddress(label: String) {
        showLoading.postValue(true)
        uiScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    XMRWalletController.addSubAddress(label)
                }
                hideLoading.postValue(true)
                success.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                hideLoading.postValue(true)
                toastRes.postValue(R.string.node_connect_failed)
            }
        }
    }

    fun setSubAddressLabel(label: String, address: SubAddress) {
        showLoading.postValue(true)
        uiScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val index = XMRWalletController.getIndexByAddress(address.address)
                    if (index < 0) {
                        toastRes.postValue(R.string.data_exception)
                    } else {
                        XMRWalletController.setSubAddressLabel(label, index)
                    }
                }
                hideLoading.postValue(true)
                success.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                hideLoading.postValue(true)
                toastRes.postValue(R.string.node_connect_failed)
            }
        }
    }
}