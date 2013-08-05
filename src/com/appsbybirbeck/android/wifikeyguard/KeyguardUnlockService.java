package com.appsbybirbeck.android.wifikeyguard;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class KeyguardUnlockService extends Service {

    private static final String TAG = KeyguardUnlockService.class.getName();

    private KeyguardManager.KeyguardLock keyguardLock;

    @Override
    public void onCreate() {
        final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting up [ " + startId + " ].");
        keyguardLock.disableKeyguard();
        Log.d(TAG, "Keyguard unlocked.");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Shutting down.");
        keyguardLock.reenableKeyguard();
        Log.d(TAG, "Keyguard locked.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
