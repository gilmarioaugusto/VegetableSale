package e.tux.ifruit.pagamento;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import e.tux.ifruit.R;

public class PagamentoPayPal extends AppCompatActivity {

    private WebView wView;
    private ProgressBar processo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento_pay_pal);

        wView = (WebView) findViewById(R.id.webView);
        processo = (ProgressBar) findViewById(R.id.progressBar);

        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wView.setWebViewClient(new WebViewClient() {

                                   @Override
                                   public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                       super.onPageStarted(view, url, favicon);
                                       wView.setVisibility(View.GONE);
                                       processo.setVisibility(View.VISIBLE);


                                   }

                                   @Override
                                   public void onPageFinished(WebView view, String url) {
                                       super.onPageFinished(view, url);
                                       wView.setVisibility(View.VISIBLE);
                                       processo.setVisibility(View.GONE);

                                   }
                               }

        );

        wView.loadUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=LLGX5ZE5UQS3C");

    }

}
