package com.ledinh.twitch_login;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ledinh.twitch_login.model.UserModel;
import com.ledinh.twitch_login.rest.Twitch;
import com.ledinh.twitch_login.rest.UserJSON;
import com.ledinh.twitch_login.storage.SharedPreferencesStorage;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TwitchIntentService extends IntentService {
    private static final String DEBUG_TAG = TwitchIntentService.class.getSimpleName();

    private static final String ACTION_RETRIEVE_USER = "com.ledinh.twitch_login.action.RETRIEVE_USER";
    private static final String EXTRA_ACCESS_TOKEN = "com.ledinh.twitch_login.extra.EXTRA_ACCESS_TOKEN";

    public static final String ACTION_RETRIEVE_USER_RESP = "com.ledinh.twitch_login.action.RETRIEVE_USER_RESPONSE";
    public static final String EXTRA_OPERATION_SUCCESS = "com.ledinh.twitch_login.extra.EXTRA_OPERATION_SUCCESS";

    public TwitchIntentService() {
        super("TwitchIntentService");
    }

    public static void startActionRetrieveUser(Context context, String accessToken) {
        Intent intent = new Intent(context, TwitchIntentService.class);
        intent.setAction(ACTION_RETRIEVE_USER);
        intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(DEBUG_TAG, "Intent received --- " + action);
            if (ACTION_RETRIEVE_USER.equals(action)) {
                final String paramAccessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN);
                handleActionRetrieveUser(paramAccessToken);
            }
        }
    }

    private void handleActionRetrieveUser(String accessToken) {
        boolean ok = (accessToken != null);

        if (ok) {
            try {
                // On récupère les informations du compte de l'utilisateur
                Response<UserJSON> response = Twitch.api.getUser("Bearer " + accessToken).execute();
                Log.d(DEBUG_TAG, response.body().toString());
                UserModel userModel = new UserModel(response.body());
                // On stocke les informations du compte de l'utilisateur
                SharedPreferencesStorage.store(this, SharedPreferencesStorage.SHARED_PREFS_KEY_USER, userModel);
                // On stocke le token
                SharedPreferencesStorage.store(this, SharedPreferencesStorage.SHARED_PREFS_KEY_TOKEN, accessToken);
                Log.d(DEBUG_TAG, "Stored token and user information");
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "handleActionRetrieveUser failed --- " + e.getMessage());
                ok = false;
                e.printStackTrace();
            }
        }

        Intent localIntent = new Intent(ACTION_RETRIEVE_USER_RESP);
        localIntent.putExtra(EXTRA_OPERATION_SUCCESS, ok);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}
