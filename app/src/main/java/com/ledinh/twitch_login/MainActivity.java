package com.ledinh.twitch_login;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ledinh.twitch_login.listener.OnIntentServiceCompletionListener;
import com.ledinh.twitch_login.model.UserModel;
import com.ledinh.twitch_login.receiver.OnIntentServiceCompletionReceiver;
import com.ledinh.twitch_login.storage.SharedPreferencesStorage;

public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = MainActivity.class.getSimpleName();

    private static final String FRAGMENT_TAG_TWITCH_DIALOG = "twitch_dialog_fragment";

    private Button mLoginButtonKeepCookies;
    private Button mLoginButtonDeleteCookies;
    private Button mLogoutButton;
    private TextView mTextViewTokenValue;
    private TextView mTextViewUserInformationValue;

    private OnIntentServiceCompletionListener mGetUserRequestListener = new OnIntentServiceCompletionListener() {
        @Override
        public void onCompletion(boolean success) {
            if (success) {
                String accessToken = SharedPreferencesStorage.retrieve(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN, String.class);
                mTextViewTokenValue.setText(accessToken);
                UserModel userModel = SharedPreferencesStorage.retrieve(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER, UserModel.class);
                mTextViewUserInformationValue.setText(userModel.toString());
            }
            else {
                Log.d(DEBUG_TAG, "Failed to retrieve user information.");
            }
        }
    };
    private OnIntentServiceCompletionReceiver mGetUserRequestReceiver = new OnIntentServiceCompletionReceiver(mGetUserRequestListener);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButtonKeepCookies = (Button) findViewById(R.id.buttonLoginKeepCookies);
        mLoginButtonDeleteCookies = (Button) findViewById(R.id.buttonLoginDeleteCookies);
        mLogoutButton = (Button) findViewById(R.id.buttonLogout);
        mTextViewTokenValue = (TextView) findViewById(R.id.textViewTokenValue);
        mTextViewUserInformationValue = (TextView) findViewById(R.id.textViewUserInformationValue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // On récupère le token si il existe
        if(SharedPreferencesStorage.contains(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN)){
            String accessToken = SharedPreferencesStorage.retrieve(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN, String.class);
            mTextViewTokenValue.setText(accessToken);
        }

        // On récupère les informations du compte de l'utilisateur si elles existent
        if(SharedPreferencesStorage.contains(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER)){
            UserModel userModel = SharedPreferencesStorage.retrieve(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER, UserModel.class);
            mTextViewUserInformationValue.setText(userModel.toString());
        }

        mLoginButtonKeepCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTwitchLoginDialogFragment(true);
            }
        });

        mLoginButtonDeleteCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTwitchLoginDialogFragment(false);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesStorage.reset(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN);
                SharedPreferencesStorage.reset(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER);
                mTextViewTokenValue.setText("--- Token value ---");
                mTextViewUserInformationValue.setText("--- User information values ---");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(TwitchIntentService.ACTION_RETRIEVE_USER_RESP);
        LocalBroadcastManager.getInstance(this).registerReceiver(mGetUserRequestReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause ();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGetUserRequestReceiver);
    }

    // Affiche la boîte de dialogue pour s'authentifier sur son compte Twitch
    private void showTwitchLoginDialogFragment(boolean keepCookies){
        TwitchLoginDialogFragment fragment = TwitchLoginDialogFragment.newInstance(keepCookies);
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, FRAGMENT_TAG_TWITCH_DIALOG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//
//    @Override
//    public void onSuccessTwitchLogin(String accessToken) {
//        Log.d(DEBUG_TAG, "onSuccessTwitchLogin called");
//        Log.d(DEBUG_TAG, "accessToken = " + accessToken);
//        // On stocke le token
//        SharedPreferencesStorage.store(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN, accessToken);
//
//        // On récupère les informations du compte de l'utilisateur
//        Twitch.api.getUser("OAuth " + accessToken).enqueue(new Callback<UserObject>() {
//            @Override
//            public void onResponse(Response<UserObject> response) {
//                Log.d(DEBUG_TAG, "onResponse = " + response.raw());
//                UserModel userModel = new UserModel(response.body());
//                // On stocke les informations du compte de l'utilisateur
//                SharedPreferencesStorage.store(MainActivity.this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER, userModel);
//                mTextViewUserInformationValue.setText(userModel.toString());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(DEBUG_TAG, "getUser failed --- " + t.getMessage());
//            }
//        });
//
//        mTextViewTokenValue.setText(accessToken);
//    }
//
//    @Override
//    public void onDismissTwitchLogin() {
//        Log.d(DEBUG_TAG, "onDismissTwitchLogin called");
//    }
//
//    @Override
//    public void onErrorTwitchLogin(String message) {
//        Log.d(DEBUG_TAG, "onErrorTwitchLogin called");
//    }

}
