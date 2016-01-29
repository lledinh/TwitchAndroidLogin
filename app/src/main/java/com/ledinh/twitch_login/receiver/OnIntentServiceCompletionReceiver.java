package com.ledinh.twitch_login.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ledinh.twitch_login.TwitchIntentService;
import com.ledinh.twitch_login.listener.OnIntentServiceCompletionListener;

/**
 * Created by Lam on 29/01/2016.
 */
public class OnIntentServiceCompletionReceiver extends BroadcastReceiver {
    private OnIntentServiceCompletionListener mOnIntentServiceCompletionListener;

    public OnIntentServiceCompletionReceiver(OnIntentServiceCompletionListener onIntentServiceCompletionListener) {
        this.mOnIntentServiceCompletionListener = onIntentServiceCompletionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mOnIntentServiceCompletionListener.onCompletion(intent.getBooleanExtra(TwitchIntentService.EXTRA_OPERATION_SUCCESS, false));
    }
}
