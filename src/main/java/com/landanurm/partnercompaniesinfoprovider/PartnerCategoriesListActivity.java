package com.landanurm.partnercompaniesinfoprovider;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

import java.net.URL;
import java.net.URLConnection;

public class PartnerCategoriesListActivity extends ListActivity {

    private static final String DATA_URL = "http://droogcompanii.ru/partner_categories.xml";

    private PartnerCategoriesAdapter adapter;
    private PartnerCategory[] partnerCategories = new PartnerCategory[] {};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new PartnerCategoriesAdapter(this);
        setListAdapter(adapter);

        progressDialog = prepareProgressDialog();

        if (savedInstanceState == null) {
            onActivityLaunched();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private ProgressDialog prepareProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please, wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    private void onActivityLaunched() {
        final PartnerCategoriesParsingTask task = new PartnerCategoriesParsingTask(this);
        task.execute();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                task.cancel(true);
            }
        });
    }

    private void onParsingComplete(PartnerCategory[] result) {
        if (result != null) {
            partnerCategories = result;
        } else {
            partnerCategories = new PartnerCategory[] {};
        }
        updatePartnerCategoriesList();
    }

    private void updatePartnerCategoriesList() {
        adapter.addAll(partnerCategories);
    }

    private void restoreState(Bundle savedInstanceState) {
        partnerCategories = (PartnerCategory[]) savedInstanceState.getSerializable("partner-categories");
        updatePartnerCategoriesList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("partner-categories", partnerCategories);
    }


    class PartnerCategoriesParsingTask extends AsyncTask<Void, Void, PartnerCategory[]> {

        private Context context;

        PartnerCategoriesParsingTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected PartnerCategory[] doInBackground(Void... params) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, getClass().getName()
            );
            wakeLock.acquire();
            PartnerCategory[] result = getResult();
            wakeLock.release();
            return result;
        }

        private PartnerCategory[] getResult() {
            PartnerCategory[] result = null;
            try {
                URL url = new URL(DATA_URL);
                URLConnection connection = url.openConnection();
                PartnerCategoriesParser parser = new PartnerCategoriesParser();
                result = parser.parsePartnerCategories(connection.getInputStream());
            } catch (Exception e) {
                Log.d("landanurm", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(PartnerCategory[] result) {
            super.onPostExecute(result);
            onParsingComplete(result);
            progressDialog.dismiss();
        }
    }
}
