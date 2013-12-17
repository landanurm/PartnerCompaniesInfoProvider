package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListActivity;

import java.io.Serializable;

public class PartnerCategoryListActivity extends ListActivity
        implements OnDataUpdatingProgressListener, AdapterView.OnItemClickListener {

    private boolean updatedData;
    private PartnerCategoryListAdapterProvider adapterProvider;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapterProvider = prepareAdapterProvider(savedInstanceState);
        setListAdapter(adapterProvider.getAdapter());
        getListView().setOnItemClickListener(this);

        updatedData = updatedData(savedInstanceState);
        if (!updatedData) {
            updateData();
        }
    }

    private PartnerCategoryListAdapterProvider prepareAdapterProvider(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new PartnerCategoryListAdapterProvider(this);
        } else {
            Serializable state = savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
            return new PartnerCategoryListAdapterProvider(this, state);
        }
    }

    private boolean updatedData(Bundle savedInstanceState) {
        return (savedInstanceState != null) &&
               (savedInstanceState.getBoolean(Keys.updatedData) == true);
    }

    private void updateData() {
        progressDialog = ProgressDialogProvider.prepareProgressDialog(this);

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
        outState.putBoolean(Keys.updatedData, updatedData);
        outState.putSerializable(Keys.partnerCategoryListAdapterState, adapterProvider.getState());
    }

    @Override
    public void onPreDataUpdating() {
        updatedData = false;
        progressDialog.show();
    }

    @Override
    public void onPostDataUpdating() {
        updatedData = true;
        adapterProvider.updateDataFromDatabase();
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String titleOfSelectedCategory = adapterProvider.getPartnerCategoryTitleByPosition(position);
        showPartnerCategoryInfo(titleOfSelectedCategory);
    }

    private void showPartnerCategoryInfo(String partnerCategoryTitle) {
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategoryTitle, partnerCategoryTitle);
        startActivity(intent);
    }
}
