package com.example.wificardscanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion


class AutoConnectToWifi {

    fun wifiConnect (ssid: String, password: String, context: Context) {

        val suggestion1 = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setIsAppInteractionRequired(true) // Optional (Needs location permission)
            .build();

        val suggestion2 = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .setIsAppInteractionRequired(true) // Optional (Needs location permission)
            .build();

        val suggestion3 = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa3Passphrase(password)
            .setIsAppInteractionRequired(true) // Optional (Needs location permission)
            .build();

        //val passpointConfig = PasspointConfiguration(); // configure passpointConfig to include a valid Passpoint configuration
        //val suggestion4 = WifiNetworkSuggestion.Builder()
        //    .setPasspointConfig(passpointConfig)
        //    .setIsAppInteractionRequired(true) // Optional (Needs location permission)
        //    .build();

        val suggestionsList = listOf(suggestion1, suggestion2, suggestion3);
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager;

        val status = wifiManager.addNetworkSuggestions(suggestionsList);
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            // do error handling here
        }

// Optional (Wait for post connection broadcast to one of your suggestions)
        val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                    return;
                }
                // do post connect processing here
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter);

    }
}