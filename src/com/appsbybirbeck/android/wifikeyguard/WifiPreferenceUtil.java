package com.appsbybirbeck.android.wifikeyguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WifiPreferenceUtil {

    private static final String TAG = WifiPreferenceUtil.class.getName();
    private static final String PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY = "keyguardDisabledForNetworks";

    private final Context context;
    private final SharedPreferences preferences;

    public WifiPreferenceUtil(final Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isKeyguardDisabledForNetwork(final String ssid) {
        final Set<String> networks = preferences.getStringSet(PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY, Collections.EMPTY_SET);
        boolean containsNetwork = networks.contains(normalizeSSID(ssid));
        Log.d(TAG, "isKeyguardDisabledForNetwork(" + ssid + ") [ " + containsNetwork + " ].");
        return containsNetwork;
    }

    public void disableKeyguardForNetwork(final String ssid) {
        final Set<String> networks = preferences.getStringSet(PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY, new HashSet<String>());
        boolean added = networks.add(normalizeSSID(ssid));
        Log.d(TAG, "disableKeyguardForNetwork(" + ssid + ") [ " + added + " ].");

        if (added) {
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY, networks);
            editor.apply();
        }
    }

    public void enableKeyguardForNetork(final String ssid) {
        final Set<String> networks = preferences.getStringSet(PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY, Collections.EMPTY_SET);
        boolean removed = networks.remove(normalizeSSID(ssid));
        Log.d(TAG, "enableKeyguardForNetork(" + ssid + ") [ " + removed + " ].");

        if (removed) {
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(PREFS_KEYGUARD_DISABLED_FOR_NETWORKS_KEY, networks);
            editor.apply();
        }
    }

    public static String normalizeSSID(final String ssid) {
        if (ssid == null) {
            return null;
        }
        return ssid.replaceAll("\"", "");
    }

}
