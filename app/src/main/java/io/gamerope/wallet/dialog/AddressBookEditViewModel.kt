package io.gamerope.wallet.dialog

import androidx.lifecycle.MutableLiveData
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.AddressBook
import io.gamerope.wallet.data.entity.SwapAddressBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddressBookEditViewModel : BaseViewModel() {

    val success = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()
    val hideLoading = MutableLiveData<Boolean>()
    val toastRes = MutableLiveData<Int>()

    fun updateAddressBook(addressBook: AddressBook) {
        showLoading.value = true
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    AppDatabase.getInstance().addressBookDao().updateAddressBook(addressBook)
                    success.postValue(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    toastRes.postValue(R.string.data_exception)
                }
            }
            hideLoading.postValue(true)
        }
    }

    fun updateAddressBook(swapAddressBook: SwapAddressBook) {
        showLoading.value = true
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    AppDatabase.getInstance().swapAddressBookDao().update(swapAddressBook)
                    success.postValue(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    toastRes.postValue(R.string.data_exception)
                }
            }
            hideLoading.postValue(true)
        }
    }

}