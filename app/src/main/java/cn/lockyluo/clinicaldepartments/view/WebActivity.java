package cn.lockyluo.clinicaldepartments.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lockyluo.clinicaldepartments.R;

/**
 * 内置浏览器
 */

public class WebActivity extends AppCompatActivity {
    WebView mWeb;
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static final String urlHead="https://m.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=";

    public static String url="https://m.baidu.com/";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mWeb.clearCache(true);
            mWeb.clearHistory();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();//添加返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        mWeb =findViewById(R.id.webview);
        progressBar =findViewById(R.id.webview_progressbar);

        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        mWeb.loadUrl(url);
        mWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 80) {
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(100);
                } else {
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWeb.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mWeb.goBack();//监听返回键
        }
        return true;
    }
}
