package com.ledinh.twitch_login;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Lam on 16/01/2016.
 */
public abstract class WebviewDialogFragment extends DialogFragment {
    private static final String DEBUG_TAG = WebviewDialogFragment.class.getSimpleName();

    protected ProgressBar progressBar;
    protected WebView webView;

    public WebviewDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View rootView = inflater.inflate(R.layout.dialog_webview_fragment_layout, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.dialogWebViewProgressBar);
        webView = (WebView) rootView.findViewById(R.id.dialogWebView);

        return rootView;
    }
}
