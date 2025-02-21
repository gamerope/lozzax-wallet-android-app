package io.gamerope.wallet.core

import android.app.Application
import io.gamerope.wallet.App
import io.gamerope.wallet.data.AppDatabase
import io.gamerope.wallet.data.entity.Asset
import io.gamerope.wallet.data.entity.Node
import io.gamerope.wallet.data.entity.Wallet
import io.gamerope.wallet.support.ZH_CN
import io.gamerope.wallet.support.extensions.getCurrentLocale
import io.gamerope.wallet.support.nodeArray
import java.io.File

class XMRRepository(val context: Application = App.instance) {

    fun getKeysFilePath(name: String): String {
        return "${context.filesDir.absolutePath}${File.separator}wallet${File.separator}xmr${File.separator}$name${File.separator}$name.keys"
    }

    fun getWalletFilePath(name: String): String {
        return "${context.filesDir.absolutePath}${File.separator}wallet${File.separator}xmr${File.separator}$name${File.separator}$name"
    }

    private fun generateXMRFile(name: String): File {
        val dir = "${context.filesDir.absolutePath}${File.separator}wallet${File.separator}xmr${File.separator}$name"
        val walletFolder = File(dir)
        if (!walletFolder.exists()) {
            walletFolder.mkdirs()
        }
        val cacheFile = File(dir, name)
        val keysFile = File(walletFolder, "$name.keys")
        val addressFile = File(walletFolder, "$name..address.txt")
        if (cacheFile.exists() || keysFile.exists() || addressFile.exists()) {
            throw RuntimeException("Some wallet files already exist for $dir")
        }
        return cacheFile
    }

    fun createWallet(walletName: String, password: String): Wallet? {
        return XMRWalletController.createWallet(generateXMRFile(walletName), password)
    }

    fun recoveryWallet(walletName: String, password: String, mnemonic: String, restoreHeight: Long?): Wallet? {
        return XMRWalletController.recoveryWallet(
            generateXMRFile(walletName), password, mnemonic, restoreHeight
                ?: 0
        )
    }

    fun createWalletWithKeys(
        walletName: String, password: String, address: String, viewKey: String,
        spendKey: String, restoreHeight: Long?
    ): Wallet? {
        return XMRWalletController.createWalletWithKeys(
            generateXMRFile(walletName), password, restoreHeight
                ?: 0, address, viewKey, spendKey
        )
    }

    fun saveWallet(wallet: Wallet): Wallet? {
        val database = AppDatabase.getInstance()

        val actWallets = database.walletDao().getActiveWallets()
        if (!actWallets.isNullOrEmpty()) {
            actWallets.forEach {
                it.isActive = false
            }
            database.walletDao().updateWallets(*actWallets.toTypedArray())
        }
        wallet.isActive = true
        database.walletDao().insertWallet(wallet)
        val insert = database.walletDao().getWalletsByName(wallet.symbol, wallet.name)
        if (insert != null) {
            database.assetDao().insertAsset(Asset(walletId = insert.id, token = insert.symbol))
        }
        return insert
    }

    fun deleteWallet(name: String): Boolean {
        var success = false
        val dir = "${context.filesDir.absolutePath}${File.separator}wallet${File.separator}xmr${File.separator}$name"
        val walletFolder = File(dir)
        if (walletFolder.exists() && walletFolder.isDirectory) {
            success = walletFolder.deleteRecursively()
        }
        return success
    }

    fun insertNodes() {
        // 兼容旧版
        val nodes = AppDatabase.getInstance().nodeDao().getSymbolNodes("LOZZ")
        var zhNode: Node? = null
        nodes?.forEach {
            if (it.url == "87.249.61.69:11112") {
                zhNode = it
                return@forEach
            }
        }
        AppDatabase.getInstance().nodeDao().insertNodes(nodes = *nodeArray)
        val node = AppDatabase.getInstance().nodeDao().getSymbolNode("LOZZ")
        val locale = context.getCurrentLocale()
        if (locale == ZH_CN && zhNode == null && node != null && node.url == "87.249.61.69:11112") {
            // 兼容旧版，修改中文区默认节点
            val filter = AppDatabase.getInstance().nodeDao().getSymbolNodes("LOZZ")?.filter {
                it.url == "nodes.lozzax.xyz"
            }
            if (!filter.isNullOrEmpty()) {
                AppDatabase.getInstance().nodeDao().updateNodes(
                    node.apply {
                        isSelected = false
                    },
                    filter[0].apply {
                        isSelected = true
                    })
            }
        }
    }

}