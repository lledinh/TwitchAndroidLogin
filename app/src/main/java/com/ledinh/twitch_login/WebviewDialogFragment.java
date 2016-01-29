package com.ledinh.twitch_login;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Lam on 16/01/2016.
 */
public abstract class WebviewDialogFragment extends DialogFragment {
    private static final String DEBUG_TAG = WebviewDialogFragment.class.getSimpleName();

    protected ProgressBar mProgressBar;
    protected WebView mWebView;

    public WebviewDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_webview_fragment_layout, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.dialogWebViewProgressBar);
        mWebView = (WebView) rootView.findViewById(R.id.dialogWebView);

        return rootView;
    }
}
