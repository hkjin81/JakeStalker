package kr.hkjin.jakestalker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;

public class HomepageActivity extends AppCompatActivity {
    public final String INTENT_EXTRA_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.webview_title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);


        String url = getIntent().getStringExtra(INTENT_EXTRA_URL);
        WebView webView = (WebView)findViewById(R.id.webView);
        if (url.isEmpty() == false) {
            try {
                setDomain(getDomainName(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    setTitle(view.getTitle());
                }
            });
        }
    }

    private void setTitle(String title) {
        View v = getSupportActionBar().getCustomView();
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        titleTextView.setText(title);
    }

    private void setDomain(String domain) {
        View v = getSupportActionBar().getCustomView();
        TextView domainTextView = (TextView) v.findViewById(R.id.domain);
        domainTextView.setText(domain);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
