package io.gamerope.wallet.support

import io.gamerope.wallet.data.entity.Coin
import io.gamerope.wallet.data.entity.Node

const val WALLET_RECOVERY = 1
const val WALLET_CREATE = 0

const val TRANSFER_ALL = 0
const val TRANSFER_IN = 1
const val TRANSFER_OUT = 2

const val SELECT_ADDRESS = 1

const val REQUEST_SCAN_ADDRESS = 100
const val REQUEST_SELECT_COIN = 101
const val REQUEST_SELECT_ADDRESS = 102
const val REQUEST_SELECT_NODE = 103
const val REQUEST_SELECT_SUB_ADDRESS = 104

const val REQUEST_SWAP_SCAN_ADDRESS = 200
const val REQUEST_SWAP_SELECT_ADDRESS = 202

const val REQUEST_PATTERN_SETTING = 105
const val REQUEST_PATTERN_CHECKING = 106
const val REQUEST_PATTERN_CHECKING_ADDRESS_SETTING = 107
const val REQUEST_PATTERN_CHECKING_BACKUP_MNEMONIC = 108
const val REQUEST_PATTERN_CHECKING_BACKUP_KEY = 109

const val REQUEST_CODE_PERMISSION_CAMERA = 501

const val ZH_CN = "zh-CN"
const val EN = "en"

const val KEY_ALIAS = "gameropeLozzax"
const val RSA_KEY_ALIAS = "gameropeLozzaxRSA"

val coinList = listOf(
    Coin("LOZZ", "LOZZAX")
)

val nodeArray = arrayOf(
    Node().apply {
        symbol = "LOZZ"
        url = "nodes.lozzax.xyz:11112"
        isSelected = true
    },
    Node().apply {
        symbol = "LOZZ"
        url = "nodes.crypto-pool.xyz:11112"
        isSelected = false
    },
    Node().apply {
        symbol = "LOZZ"
        url = "node.lozzax.xyz:11112"
        isSelected = false
    },
    Node().apply {
        symbol = "LOZZ"
        url = "node-1.lozzax.xyz:11112"
        isSelected = false
    }
)
