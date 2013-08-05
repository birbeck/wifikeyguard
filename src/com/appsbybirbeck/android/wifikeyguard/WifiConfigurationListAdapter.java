package com.appsbybirbeck.android.wifikeyguard;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;

public class WifiConfigurationListAdapter extends ArrayAdapter<WifiConfiguration> {

    private static final int layoutResourceId = android.R.layout.simple_list_item_multiple_choice;
    private static final int textViewResourceId = android.R.id.text1;

    private final WifiPreferenceUtil wifiPreferenceUtils;

    private static class ViewHolder {
        CheckedTextView label;
    }

    public WifiConfigurationListAdapter(final Context context, final List<WifiConfiguration> objects) {
        super(context, layoutResourceId, textViewResourceId, objects);
        wifiPreferenceUtils = new WifiPreferenceUtil(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(layoutResourceId, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.label = (CheckedTextView) view.findViewById(textViewResourceId);
            view.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final String ssid = getItem(position).SSID;
        viewHolder.label.setText(WifiPreferenceUtil.normalizeSSID(ssid));
        viewHolder.label.setChecked(wifiPreferenceUtils.isKeyguardDisabledForNetwork(ssid));
        return view;
    }

}
