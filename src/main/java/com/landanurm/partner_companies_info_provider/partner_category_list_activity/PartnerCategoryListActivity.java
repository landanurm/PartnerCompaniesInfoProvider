package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProvider;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartnerCategoryListActivity extends ListActivity implements OnDataUpdatingProgressListener, AdapterView.OnItemClickListener {

    private ArrayAdapter<String> adapter;
    private boolean dataUpdated;
    private List<String> itemTitles;
    private PartnerCategoriesInfoProvider partnerCategoriesInfoProvider;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(this);

        itemTitles = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemTitles);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        progressDialog = prepareProgressDialog();

        if (needToUpdateData(savedInstanceState)) {
            runDataUpdating();
        } else {
            updatePartnerCategoriesList();
        }
    }

    private ProgressDialog prepareProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please, wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    private boolean needToUpdateData(Bundle savedInstanceState) {
        return (savedInstanceState == null) ||
                (savedInstanceState.getBoolean(Keys.dataUpdated) == false);
    }

    private void runDataUpdating() {
        dataUpdated = false;
        final PartnerCategoriesUpdatingTask task = new PartnerCategoriesUpdatingTask(this, this);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                task.cancel(true);
            }
        });
        task.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Keys.dataUpdated, dataUpdated);
        if (dataUpdated) {
            outState.putSerializable(Keys.partnerCategories, (Serializable) itemTitles);
        }
    }

    private void updatePartnerCategoriesList() {
        itemTitles.clear();
        itemTitles.addAll(partnerCategoriesInfoProvider.getPartnerCategoryTitles());
        adapter.notifyDataSetChanged();
        dataUpdated = true;
    }

    @Override
    public void onPreDataUpdating() {
        progressDialog.show();
    }

    @Override
    public void onPostDataUpdating() {
        updatePartnerCategoriesList();
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = adapter.getItem(position);
        showPartnerCategoryInfo(selectedItem);
    }

    private void showPartnerCategoryInfo(String partnerCategoryTitle) {
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategoryTitle, partnerCategoryTitle);
        startActivity(intent);
    }
}
