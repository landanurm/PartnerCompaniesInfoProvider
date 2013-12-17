package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import com.landanurm.partner_companies_info_provider.PartnerCategoriesParser;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesSaver;
import com.landanurm.partner_companies_info_provider.db_util.data_structure.PartnerCategory;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerCategoriesUpdatingTask extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private final OnDataUpdatingProgressListener listener;

    public PartnerCategoriesUpdatingTask(Context context, OnDataUpdatingProgressListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreDataUpdating();
    }

    @Override
    protected Void doInBackground(Void... params) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();
        try {
            updatePartnerCategories();
        } catch (Exception e) {
            processUpdatingException(e);
        } finally {
            wakeLock.release();
        }
        return null;
    }

    private void updatePartnerCategories() throws Exception {
        List<PartnerCategory> partnerCategories = downloadAndParsePartnerCategories();
        saveToDatabase(partnerCategories);
    }

    private List<PartnerCategory> downloadAndParsePartnerCategories() throws Exception {
        URL url = new URL(DataUrlProvider.getDataUrl());
        URLConnection connection = url.openConnection();
        PartnerCategoriesParser parser = new PartnerCategoriesParser();
        return parser.parsePartnerCategories(connection.getInputStream());
    }

    private void saveToDatabase(List<PartnerCategory> partnerCategories) {
        PartnerCategoriesSaver partnerCategoriesSaver = new PartnerCategoriesSaver(context);
        partnerCategoriesSaver.saveToDatabase(partnerCategories);
    }

    private void processUpdatingException(Exception e) {
        Log.d("com.landanurm.partner_companies_info_provider", e.getMessage());
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        listener.onPostDataUpdating();
    }
}
