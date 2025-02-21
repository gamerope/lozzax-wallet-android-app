package io.gamerope.wallet.feature.wallet

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseTitleSecondActivity
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.Wallet
import io.gamerope.wallet.feature.generate.GenerateWalletActivity
import io.gamerope.wallet.support.BackgroundHelper
import io.gamerope.wallet.support.WALLET_CREATE
import io.gamerope.wallet.support.WALLET_RECOVERY
import io.gamerope.wallet.support.extensions.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_wallet_manager.*
import kotlinx.android.synthetic.main.item_wallet.*

class WalletManagerActivity : BaseTitleSecondActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_manager)
        setCenterTitle(R.string.wallet_manager)

        val viewModel = ViewModelProviders.of(this).get(WalletManagerViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val list = mutableListOf<Wallet>()
        val adapter = WalletAdapter(list, viewModel)
        recyclerView.adapter = adapter

        AppDatabase.getInstance().walletDao().loadSymbolWallets("XMR").observe(this, Observer { value ->
            value?.let {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.activeWallet.observe(this, Observer {
            finish()
        })

        viewModel.walletDetail.observe(this, Observer { value ->
            value?.let {
                startActivity(it.apply {
                    setClass(this@WalletManagerActivity, WalletDetailActivity::class.java)
                })
            }
        })

        recoveryWallet.background = BackgroundHelper.getButtonBackground(this, R.color.color_002C6D)
        createWallet.background = BackgroundHelper.getButtonBackground(this, R.color.color_00A761)

        recoveryWallet.setOnClickListener {
            startActivity(Intent(this, GenerateWalletActivity::class.java).apply {
                sharedPreferences().putInt("type", WALLET_RECOVERY)
            })
        }

        createWallet.setOnClickListener {
            startActivity(Intent(this, GenerateWalletActivity::class.java).apply {
                sharedPreferences().putInt("type", WALLET_CREATE)
            })
        }
    }

    class WalletAdapter(val data: List<Wallet>, private val viewModel: WalletManagerViewModel) : androidx.recyclerview.widget.RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)
            return ViewHolder(view, viewModel)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindViewHolder(data[position])
        }

        class ViewHolder(override val containerView: View, val viewModel: WalletManagerViewModel) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bindViewHolder(wallet: Wallet) {
                name.text = wallet.name
                if (wallet.isActive) {
                    current.visibility = View.VISIBLE
                    current.background = BackgroundHelper.getBackground(current.context, R.color.color_59C698, dp2px(3))
                    active.visibility = View.GONE
                } else {
                    current.visibility = View.GONE
                    active.visibility = View.VISIBLE
                }
                active.setOnClickListener {
                    viewModel.activeWallet(wallet)
                }
                icon.setImage(wallet.symbol)
                symbol.text = wallet.symbol
                balance.text = wallet.balance.formatterAmountStrip()
                BackgroundHelper.setItemShadowBackground(itemView)
                itemView.setOnClickListener { viewModel.onItemClick(wallet) }
            }
        }
    }
}