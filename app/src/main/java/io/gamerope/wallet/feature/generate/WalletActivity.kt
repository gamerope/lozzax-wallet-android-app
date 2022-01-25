package io.gamerope.wallet.feature.generate

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseActivity
import io.gamerope.wallet.support.BackgroundHelper
import io.gamerope.wallet.support.WALLET_CREATE
import io.gamerope.wallet.support.WALLET_RECOVERY
import io.gamerope.wallet.support.extensions.putInt
import io.gamerope.wallet.support.extensions.putString
import io.gamerope.wallet.support.extensions.sharedPreferences
import io.gamerope.wallet.support.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_wallet.*

class WalletActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        StatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.color_FFFFFF))
        StatusBarHelper.setStatusBarLightMode(this)

        createWallet.background = BackgroundHelper.getButtonBackground(this, R.color.color_00A761)
        recoveryWallet.background = BackgroundHelper.getButtonBackground(this, R.color.color_002C6D)

        sharedPreferences().putString("symbol", "LOZZ")
        createWallet.setOnClickListener {
            startActivity(Intent(this, GenerateWalletActivity::class.java).apply {
                sharedPreferences().putInt("type", WALLET_CREATE)
            })
        }
        recoveryWallet.setOnClickListener {
            startActivity(Intent(this, GenerateWalletActivity::class.java).apply {
                sharedPreferences().putInt("type", WALLET_RECOVERY)
            })
        }
    }
}
