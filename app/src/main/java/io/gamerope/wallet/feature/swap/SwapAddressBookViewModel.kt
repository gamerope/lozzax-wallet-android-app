package io.gamerope.wallet.feature.swap

import androidx.lifecycle.MutableLiveData
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.AddressBook
import io.gamerope.wallet.data.entity.SwapAddressBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SwapAddressBookViewModel : BaseViewModel() {

    val isDelete = MutableLiveData<Boolean>()
    val itemClick = MutableLiveData<SwapAddressBook>()
    val editAddressBook = MutableLiveData<SwapAddressBook>()
    val deleteSuccess = MutableLiveData<Int>()

    fun deleteAddressBook(it: SwapAddressBook, position: Int) {
        isDelete.value = true
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    AppDatabase.getInstance().swapAddressBookDao().delete(it)
                    deleteSuccess.postValue(position)
                } catch (e: Exception) {
                    e.printStackTrace()
                    deleteSuccess.postValue(-1)
                }
            }
        }
    }

    fun itemClick(addressBook: SwapAddressBook) {
        itemClick.value = addressBook
    }

    fun edit(addressBook: SwapAddressBook) {
        editAddressBook.value = addressBook
    }
}