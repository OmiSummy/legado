package io.legado.app.ui.rss.read

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import io.legado.app.R
import io.legado.app.base.VMBaseActivity
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.getViewModel
import kotlinx.android.synthetic.main.activity_rss_read.*

class ReadRssActivity : VMBaseActivity<ReadRssViewModel>(R.layout.activity_rss_read) {

    override val viewModel: ReadRssViewModel
        get() = getViewModel(ReadRssViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("title")
        webView.webViewClient = WebViewClient()
        initLiveData()
        viewModel.initData(intent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initLiveData() {
        viewModel.rssSourceLiveData.observe(this, Observer {
            if (it.enableJs) {
                webView.settings.javaScriptEnabled = true
            }
        })
        viewModel.contentLiveData.observe(this, Observer { content ->
            viewModel.rssArticle?.let {
                val url = NetworkUtils.getAbsoluteURL(it.origin, it.link ?: "")
                webView.loadDataWithBaseURL(
                    url,
                    "<style>img{max-width:100%}</style>$content",
                    "text/html",
                    "utf-8",
                    url
                )
            }
        })
        viewModel.urlLiveData.observe(this, Observer {
            webView.loadUrl(it)
        })
    }

}