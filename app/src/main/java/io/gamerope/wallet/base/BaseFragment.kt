package io.gamerope.wallet.base

import androidx.fragment.app.Fragment
import io.gamerope.wallet.dialog.LoadingDialog

open class BaseFragment : Fragment() {
    var loadingDialog: LoadingDialog? = null

    open fun showLoading() {
        val ctx = context
        if (ctx != null && loadingDialog == null) {
            loadingDialog = LoadingDialog(ctx)
        }
        loadingDialog?.show()
    }

    open fun hideLoading() {
        val act = activity
        if (act != null && !act.isFinishing && !act.isDestroyed) loadingDialog?.dismiss()
    }
}