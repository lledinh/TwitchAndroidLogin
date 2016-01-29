package com.ledinh.twitch_login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ledinh.twitch_login.rest.Twitch;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lam on 16/01/2016.
 * Affiche une boîte de dialogue qui permet à l'utilisateur de se connecter à son compte Twitch.
 * Une fois que l'utilisateur s'est connecté, on récupère un token. Ce token est nécessaire pour faire certain appels à l'api REST Twitch.
 */
public class TwitchLoginDialogFragment extends WebviewDialogFragment {
    private static final String DEBUG_TAG = TwitchLoginDialogFragment.class.getSimpleName();

    private static final String ARG_KEEP_COOKIES = "keep_cookies";

//    private TwitchLoginDialogFragment.Callback mCallback;
    private boolean mKeepCookies = false;
    private String mAccessToken;

//    public interface Callback {
//        void onSuccessTwitchLogin(String result);
//        void onDismissTwitchLogin();
//        void onErrorTwitchLogin(String message);
//    }

    public static TwitchLoginDialogFragment newInstance(boolean keepCookies) {
        TwitchLoginDialogFragment fragment = new TwitchLoginDialogFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_KEEP_COOKIES, keepCookies);
        fragment.setArguments(args);

        return fragment;
    }

    public TwitchLoginDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mKeepCookies = args.getBoolean(ARG_KEEP_COOKIES);
        }

//        try {
//            mCallback = (TwitchLoginDialogFragment.Callback) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Calling activity must implement TwitchLoginDialogFragment");
//        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        TwitchIntentService.startActionRetrieveUser(getContext(), mAccessToken);
        super.onDismiss(dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        // TODO: CookieSyncManager à utiliser pour les api < 21 ?
        // CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        // S'il y a des cookies Twitch, on les supprime, cela permet de d'éviter que la page de login se ferme de suite si on essaie de se ré-authentifier (sur un autre compte par exemple).
        // Il ne faut cependant pas désactiver les cookies, sinon on aura une erreur "Bad request" après avoir rentré son identifiant et mot de passe.
        // TODO: removeAllCookie deprecated
        if(!mKeepCookies) cookieManager.removeAllCookie();

        mWebView.loadUrl(Twitch.LOGIN_URL);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(DEBUG_TAG, "onPageFinished url = " + url);
                mProgressBar.setVisibility(View.GONE);

                boolean loginSuccess = url.contains("access_token");
                boolean loginFail = url.contains("error");
                if (loginSuccess) {
                    handleLoginSuccess(url);
                } else if (loginFail) {
                    handleLoginFail();
                }

                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                Log.d(DEBUG_TAG, "onReceivedHttpAuthRequest called");
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d(DEBUG_TAG, "onReceivedError called");
                Log.d(DEBUG_TAG, "errorCode = " + errorCode);
                Log.d(DEBUG_TAG, "description = " + description);
                Log.d(DEBUG_TAG, "failingUrl = " + failingUrl);

                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                Log.d(DEBUG_TAG, "onReceivedLoginRequest called");
                super.onReceivedLoginRequest(view, realm, account, args);
            }
        });

        return rootView;
    }

    private void handleLoginSuccess(String returnedUrl){
        try{
            mAccessToken = retrieveAccessToken(returnedUrl);
//            mCallback.onSuccessTwitchLogin(accessToken);
        }
        catch(MalformedURLException e){
            Log.d(DEBUG_TAG, "MalformedURLException thrown, should not happen");
        }

//        mCallback.onDismissTwitchLogin();
        // On ferme la boîte de dialogue
        dismiss();
    }

    private void handleLoginFail(){
//        mCallback.onErrorTwitchLogin("Login failed.");
        // On ferme la boîte de dialogue
        dismiss();
    }

    // L'url donnée en paramètre est de la forme: http://localhost/#access_token={token}&scope={scope}
    private String retrieveAccessToken(String completeUrl) throws MalformedURLException{
        String accessToken = null;

        URL url = new URL(completeUrl);
        // getRef au lieu de getQuery car il y a un # juste apres localhost
        String query = url.getRef();

        // On récupère les paramètres et leurs valeurs
        String[] queryParameters = query.split("&");

        for(String queryParameter : queryParameters){
            // On s'intéresse seulement au paramètre access_token
            if(queryParameter.contains("access_token")){
                // Le paramètre et la valeur sont séparés par un '=', on découpe cette chaîne pour récupérer la valeur
                String accessTokenParameter[] = queryParameter.split("=");
                // 0 = paramètre, 1 = valeur
                accessToken = accessTokenParameter[1];
            }
        }

        return accessToken;
    }

}
