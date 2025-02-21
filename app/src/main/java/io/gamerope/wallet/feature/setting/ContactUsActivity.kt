package io.gamerope.wallet.feature.setting

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.gamerope.wallet.R
import io.gamerope.wallet.base.BaseTitleSecondActivity
import io.gamerope.wallet.data.entity.ContactUs
import io.gamerope.wallet.support.extensions.copy
import io.gamerope.wallet.support.extensions.dp2px
import io.gamerope.wallet.widget.DividerItemDecoration
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.item_contact_us.*

class ContactUsActivity : BaseTitleSecondActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        setCenterTitle(R.string.contact_us)

        val viewModel = ViewModelProviders.of(this).get(ContactUsViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val list = listOf(
                ContactUs(R.drawable.icon_telegram, "Telegram", "https://t.me/LozzaxCommunity"),
                ContactUs(R.drawable.icon_twitter, "Twitter", "https://twitter.com/LozzaxCommunity"),
                ContactUs(R.drawable.icon_github, "GitHub", "https://github.com/lozzax-project"),
                ContactUs(R.drawable.icon_facebook, "Facebook", "https://www.facebook.com/LozzaxCommunity"))
        recyclerView.adapter = ContactUsAdapter(list, viewModel)
        recyclerView.addItemDecoration(DividerItemDecoration().apply {
            setOrientation(DividerItemDecoration.VERTICAL)
            setMarginStart(dp2px(25))
        })

        viewModel.openBrowser.observe(this, Observer { value -> value?.let { openBrowser(it) } })
        viewModel.copyUrl.observe(this, Observer { copy(it) })
    }

    private fun openBrowser(url: String) {
        //从其他浏览器打开
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    class ContactUsAdapter(val data: List<ContactUs>, val viewModel: ContactUsViewModel) : androidx.recyclerview.widget.RecyclerView.Adapter<ContactUsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_us, parent, false)
            return ViewHolder(view, viewModel)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindViewHolder(data[position])
        }

        class ViewHolder(override val containerView: View, val viewModel: ContactUsViewModel) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bindViewHolder(contactUs: ContactUs) {
                icon.setImageResource(contactUs.icon)
                name.text = contactUs.name
                url.text = contactUs.url
                url.setOnClickListener { viewModel.openBrowser(contactUs.url) }
                copy.setOnClickListener { viewModel.copyUrl(contactUs.url) }
            }
        }
    }
}