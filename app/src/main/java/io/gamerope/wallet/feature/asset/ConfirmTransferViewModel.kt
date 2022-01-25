package io.gamerope.wallet.feature.asset

import androidx.lifecycle.MutableLiveData
import android.os.SystemClock
import io.gamerope.wallet.ActivityStackManager
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.core.XMRRepository
import io.gamerope.wallet.core.XMRWalletController
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.Wallet
import io.gamerope.wallet.data.entity.WalletRelease
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmTransferViewModel : BaseViewModel() {

    val amount = MutableLiveData<String>()
    val fee = MutableLiveData<String>()
    val enabled = MutableLiveData<Boolean>()

    var activeWallet: Wallet? = null
    var walletRelease: WalletRelease? = null

    val toast = MutableLiveData<String>()
    val toastInt = MutableLiveData<Int>()

    init {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                activeWallet = AppDatabase.getInstance().walletDao().getActiveWallet()
                activeWallet?.let {
                    walletRelease = AppDatabase.getInstance().walletReleaseDao().loadDataByWalletId(it.id)
                }
                amount.postValue(XMRWalletController.getTxAmount())
                fee.postValue(XMRWalletController.getTxFee())
                if (activeWallet == null) {

                    toastInt.postValue(R.string.data_exception)
                    enabled.postValue(false)
                } else {
                    enabled.postValue(true)
                }
            }
        }
    }

    fun next(password: String) {
        enabled.postValue(false)
        uiScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val verify = XMRWalletController.verifyWalletPasswordOnly(XMRRepository().getKeysFilePath(activeWallet!!.name), password)
                    if (verify) {
                        XMRWalletController.sendTransaction()
                        SystemClock.sleep(300)
                        if (ActivityStackManager.getInstance().contain(AssetDetailActivity::class.java)) {
                            ActivityStackManager.getInstance().finishToActivity(AssetDetailActivity::class.java)
                        }
                    } else {
                        throw IllegalArgumentException("invalid password")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                toast.postValue(e.message)
            } finally {
                enabled.postValue(true)
            }
        }
    }
}