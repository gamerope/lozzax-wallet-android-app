package io.gamerope.wallet.feature.asset

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import io.gamerope.wallet.base.BaseViewModel
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.Asset
import io.gamerope.wallet.data.entity.Wallet
import io.gamerope.wallet.data.entity.WalletRelease
import io.gamerope.wallet.support.extensions.putBoolean
import io.gamerope.wallet.support.extensions.sharedPreferences
import io.gamerope.wallet.support.viewmodel.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssetViewModel : BaseViewModel() {

    val openAssetDetail = SingleLiveEvent<Intent>()

    val walletRelease = MutableLiveData<WalletRelease>()

    var wallet: Wallet? = null

    val assetVisible = SingleLiveEvent<Unit>()
    val assetInvisible = SingleLiveEvent<Unit>()

    private var asset: Asset? = null

    fun initVisible() {
        val visible = sharedPreferences().getBoolean("assetVisible", true)
        if (visible) {
            assetVisible.call()
        } else {
            assetInvisible.call()
        }
    }

    fun onItemClick(value: Asset) {
        asset = value
        wallet?.let {
            uiScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        walletRelease.postValue(AppDatabase.getInstance().walletReleaseDao().loadDataByWalletId(it.id))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    walletRelease.postValue(null)
                }
            }
        }
    }

    fun next(password: String) {
        if (asset != null) {
            openAssetDetail.value = Intent().apply {
                putExtra("password", password)
                putExtra("assetId", asset!!.id)
            }
            asset = null
        }
    }

    fun assetVisibleChanged() {
        val visible = sharedPreferences().getBoolean("assetVisible", true)
        sharedPreferences().putBoolean("assetVisible", !visible)
        if (!visible) {
            assetVisible.call()
        } else {
            assetInvisible.call()
        }
    }
}