package io.gamerope.wallet.feature.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.gamerope.wallet.App
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseTitleSecondActivity
import io.gamerope.wallet.support.BackgroundHelper
import io.gamerope.wallet.support.extensions.dp2px
import io.gamerope.wallet.support.extensions.openBrowser
import io.gamerope.wallet.support.extensions.versionName
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseTitleSecondActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setCenterTitle(R.string.about_us)

        version.setLeftString(getString(R.string.version_placeholder, versionName()))
        version.setOnClickListener {
            openBrowser("https://docs.lozzax.xyz")
        }

        agreement.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (App.newVersion) {
            version.rightTextView.visibility = View.VISIBLE
            version.setRightString(getString(R.string.find_new_version))
            version.rightTextView.compoundDrawablePadding = dp2px(5)
            version.rightTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    BackgroundHelper.getRedDotDrawable(this), null)
        } else {
            version.rightTextView.visibility = View.INVISIBLE
        }
    }
}