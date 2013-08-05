package com.appsbybirbeck.android.wifikeyguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements AdapterView.OnItemClickListener {

    private final List<WifiConfiguration> networks = new ArrayList<WifiConfiguration>();

    private ListView listView;
    private WifiConfigurationListAdapter listAdapter;
    private WifiPreferenceUtil wifiPreferenceUtil;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        wifiPreferenceUtil = new WifiPreferenceUtil(this);

        listAdapter = new WifiConfigurationListAdapter(this, networks);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadNetworks();
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final CheckedTextView checkedTextView = (CheckedTextView) view;
        final String ssid = checkedTextView.getText().toString();
        if (checkedTextView.isChecked()) {
            wifiPreferenceUtil.disableKeyguardForNetwork(ssid);
        } else {
            wifiPreferenceUtil.enableKeyguardForNetork(ssid);
        }

        final Intent intent = new Intent(WifiStateReceiver.ACTION_NETWORK_TOGGLED);
        intent.putExtra(WifiStateReceiver.EXTRA_SSID, ssid);
        sendBroadcast(intent);
    }

    private void reloadNetworks() {
        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        networks.clear();
        networks.addAll(wifiManager.getConfiguredNetworks());
        listAdapter.notifyDataSetChanged();

        for (int i = 0; i < listView.getCount(); i++) {
            final String ssid = listAdapter.getItem(i).SSID;
            if (wifiPreferenceUtil.isKeyguardDisabledForNetwork(ssid)) {
                listView.setItemChecked(i, true);
            }
        }
    }

}
