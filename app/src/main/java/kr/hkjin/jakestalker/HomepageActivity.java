package kr.hkjin.jakestalker;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;

public class HomepageActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_URL = "url";

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

                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        showLoading(true);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        showLoading(false);
                        setTitle(view.getTitle());
                    }
                });
            } catch (URISyntaxException e) {
                showErrorDialog(getString(R.string.uri_syntax_error));
                e.printStackTrace();
            }
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

    private void showLoading(boolean show) {
        int visibility;
        if (show) {
            visibility = View.VISIBLE;
        }
        else {
            visibility = View.INVISIBLE;
        }

        findViewById(R.id.loading).setVisibility(visibility);
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(R.drawable.ic_sorry)
                .show();
    }
}
