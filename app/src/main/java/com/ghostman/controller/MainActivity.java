package com.ghostman.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.myWebView);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        WebSettings webSettings = webView.getSettings();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        //webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        //webView.getSettings().setSupportZoom(true);

        progressBar = ProgressDialog.show(this, "Please Wait", "Loading...");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {

            webView.loadUrl("http://192.168.43.206:5000");

            //+++++++++++++++++++++++ WEB VIEW CLIENT +++++++++++++++++++++++++++
            webView.setWebViewClient(new WebViewClient() {

                //If you will not use this method url links are open in new brower not in webview
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // Check if Url contains ExternalLinks string in url
                    // then open url in new browser
                    // else all webview links will open in webview browser
                    if (url.contains("google")) {
                        // Could be cleverer and use a regex
                        //Open links in new browser
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                        // Here we can open new activity
                        return true;

                    } else {
                        // Stay within this webview and load url
                        view.loadUrl(url);
                        return true;
                    }
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Log.i("TAG", "ERROR LOADING URL");
                    //webView.loadUrl("file:///android_asset/error.html");
                    //webView.loadUrl("about:blank");
                    Toast.makeText(getApplicationContext(),"Error Loading Page...",Toast.LENGTH_LONG).show();
                    super.onReceivedError(view, request, error);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.i("TAG", "Finished loading URL: " + url);
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    super.onPageFinished(view, url);
                }
            });

        } else {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setCancelable(false);
            builder.setTitle(" ALERT ")
                    .setMessage("App Cannot work in Offline Mode.")
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    // fake a network operation's delayed response
    // this is just for demonstration, not real code!
    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //webView.reload();
                webView.loadUrl("http://192.168.43.206:5000");
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        // if (webView.canGoBack())
        //    webView.goBack();

        super.onBackPressed();
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getBaseContext(), android.R.style.Theme_Material_Dialog_Alert);
        builder.setCancelable(false);
        builder.setTitle(" Confirmation ")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
