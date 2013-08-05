package com.appsbybirbeck.android.wifikeyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiStateReceiver extends BroadcastReceiver {

    public static final String ACTION_NETWORK_TOGGLED = "com.appsbybirbeck.android.wifikeyguard.NETWORK_TOGGLED";
    public static final String EXTRA_SSID = "_ssid";

    private static final String TAG = WifiStateReceiver.class.getName();
    private static final Class KEYGUARD_UNLOCK_SERVICE = KeyguardUnlockService.class;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo info = wifiManager.getConnectionInfo();

        if (intent.getAction().equals(ACTION_NETWORK_TOGGLED)) {
            final String extra_ssid = intent.getStringExtra(EXTRA_SSID);
            if (!extra_ssid.equals(WifiPreferenceUtil.normalizeSSID(info.getSSID()))) {
                Log.d(TAG, "not connected to network [ " + extra_ssid + " ]");
                return;
            }
        }

        if (info.getSupplicantState() != SupplicantState.COMPLETED) {
            Log.d(TAG, "Wifi is not connected.");
            lockKeyguard(context);
            return;
        }

        Log.d(TAG, "Wifi is connected.");
        final WifiPreferenceUtil util = new WifiPreferenceUtil(context);
        final String ssid = info.getSSID();
        if (util.isKeyguardDisabledForNetwork(ssid)) {
            Log.d(TAG, "Unlocking keyguard for SSID: [ " + ssid + " ].");
            unlockKeyguard(context);
        } else {
            Log.d(TAG, "Locking keyguard for SSID: [ " + ssid + " ].");
            lockKeyguard(context);
        }
    }

    private void unlockKeyguard(final Context context) {
        // final PackageManager packageManager = context.getPackageManager();
        // packageManager.setComponentEnabledSetting(new ComponentName(context, KEYGUARD_UNLOCK_SERVICE),
        //         PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        context.startService(new Intent(context, KEYGUARD_UNLOCK_SERVICE));
    }

    private void lockKeyguard(final Context context) {
        // final PackageManager packageManager = context.getPackageManager();
        // packageManager.setComponentEnabledSetting(new ComponentName(context, KEYGUARD_UNLOCK_SERVICE),
        //         PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        context.stopService(new Intent(context, KEYGUARD_UNLOCK_SERVICE));
    }

}
