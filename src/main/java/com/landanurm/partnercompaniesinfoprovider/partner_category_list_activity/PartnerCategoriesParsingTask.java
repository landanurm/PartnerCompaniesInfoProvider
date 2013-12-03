package com.landanurm.partnercompaniesinfoprovider.partner_category_list_activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import com.landanurm.partnercompaniesinfoprovider.PartnerCategoriesParser;
import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerCategoriesParsingTask extends AsyncTask<Void, Void, List<PartnerCategory>> {
    private final OnParsingProgressListener onParsingProgressListener;
    private final Context context;

    public PartnerCategoriesParsingTask(Context context, OnParsingProgressListener onParsingProgressListener) {
        this.context = context;
        this.onParsingProgressListener = onParsingProgressListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onParsingProgressListener.onPreParsingExecute();
    }

    @Override
    protected List<PartnerCategory> doInBackground(Void... params) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, getClass().getName()
        );
        wakeLock.acquire();
        List<PartnerCategory> result = getResult();
        wakeLock.release();
        return result;
    }

    private List<PartnerCategory> getResult() {
        List<PartnerCategory> result = null;
        try {
            result = downloadAndParsePartnerCategories();
        } catch (Exception e) {
            Log.d("landanurm", e.getMessage());
        }
        return result;
    }

    private List<PartnerCategory> downloadAndParsePartnerCategories() throws Exception {
        URL url = new URL(DataUrlProvider.getDataUrl());
        URLConnection connection = url.openConnection();
        PartnerCategoriesParser parser = new PartnerCategoriesParser();
        return parser.parsePartnerCategories(connection.getInputStream());
    }

    @Override
    protected void onPostExecute(List<PartnerCategory> result) {
        super.onPostExecute(result);
        onParsingProgressListener.onPostParsingExecute(result);
    }
}
