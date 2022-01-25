package io.gamerope.wallet.feature.wallet

import androidx.lifecycle.MutableLiveData
import android.content.Intent
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.Wallet
import io.gamerope.wallet.support.viewmodel.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalletManagerViewModel : BaseViewModel() {

    val activeWallet = SingleLiveEvent<Unit>()
    val walletDetail = MutableLiveData<Intent>()

    fun activeWallet(wallet: Wallet) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val walletDao = AppDatabase.getInstance().walletDao()
                val activeWallet = walletDao.getActiveWallet()
                if (activeWallet != null) {
                    walletDao.updateWallets(activeWallet.apply { isActive = false }, wallet.apply { isActive = true })
                } else {
                    walletDao.updateWallets(wallet.apply { isActive = true })
                }
                delay(300)
            }
            activeWallet.call()
        }
    }

    fun onItemClick(wallet: Wallet) {
        walletDetail.value = Intent().apply {
            putExtra("walletId", wallet.id)
        }
    }
}