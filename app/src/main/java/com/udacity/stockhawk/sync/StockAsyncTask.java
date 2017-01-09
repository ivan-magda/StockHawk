package com.udacity.stockhawk.sync;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public final class StockAsyncTask extends AsyncTask<String, Void, Stock> {

    public interface Callback {
        void block(Stock stock);
    }

    private Callback mCallback;

    @Override
    protected Stock doInBackground(String... params) {
        if (params == null || params.length <= 0) return null;

        final String symbol = params[0];
        if (TextUtils.isEmpty(symbol)) return null;
        try {
            Stock stock = YahooFinance.get(symbol);
            if (stock == null || stock.getName() == null) {
                return null;
            } else {
                return stock;
            }
        } catch (IOException e) {
            Timber.e("Failed to get stock with error: " + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Stock stock) {
        if (mCallback != null) mCallback.block(stock);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

}
