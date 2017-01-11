package com.udacity.stockhawk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public final class Utilities {

    private Utilities() {
    }

    public static boolean isNetworkUp(@NonNull final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static List<Pair<Date, Float>> mapHistory(String history) {
        if (TextUtils.isEmpty(history)) return null;

        String[] historyEntries = history.split("\n");
        ArrayList<Pair<Date, Float>> historyMap = new ArrayList<>(historyEntries.length);
        for (String anEntry : historyEntries) {
            String[] anEntryValues = anEntry.split(", ");
            String dateString = anEntryValues[0];
            String priceString = anEntryValues[1];

            Date date = new Date(Long.parseLong(dateString));
            float price = Float.parseFloat(priceString);

            historyMap.add(new Pair<>(date, price));
        }

        return new ArrayList<>(historyMap);
    }

    public static LinkedHashMap<String, List<Float>> mappedHistoryByMonth(String history) {
        List<Pair<Date, Float>> mappedHistory = Utilities.mapHistory(history);
        if (mappedHistory == null) return null;

        mappedHistory = Lists.reverse(mappedHistory);
        LinkedHashMap<String, List<Float>> hashMapByMonth = new LinkedHashMap<>(mappedHistory.size() / 2);
        DateFormat dateFormat = new SimpleDateFormat("MMM yy", Locale.getDefault());

        for (Pair<Date, Float> pair : mappedHistory) {
            String key = dateFormat.format(pair.first);
            if (hashMapByMonth.get(key) == null) {
                hashMapByMonth.put(key, new ArrayList<Float>());
            }
            hashMapByMonth.get(key).add(pair.second);
        }

        return new LinkedHashMap<>(hashMapByMonth);
    }

}
