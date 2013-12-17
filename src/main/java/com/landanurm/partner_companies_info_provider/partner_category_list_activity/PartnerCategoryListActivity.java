package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListActivity;

import java.io.Serializable;

public class PartnerCategoryListActivity extends ListActivity
        implements OnDataUpdatingProgressListener, AdapterView.OnItemClickListener {

    private boolean updatedData;
    private PartnerCategoryListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = prepareAdapter(savedInstanceState);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        updatedData = updatedData(savedInstanceState);
        if (!updatedData) {
            startUpdate();
        }
    }

    private PartnerCategoryListAdapter prepareAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return PartnerCategoryListAdapter.newInstance(this);
        } else {
            Serializable state = savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
            return PartnerCategoryListAdapter.newInstance(this, state);
        }
    }

    private boolean updatedData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        return savedInstanceState.getBoolean(Keys.updatedData);
    }

    private void startUpdate() {
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
        outState.putSerializable(Keys.partnerCategoryListAdapterState, adapter.getState());
    }

    @Override
    public void onPreDataUpdating() {
        updatedData = false;
        progressDialog.show();
    }

    @Override
    public void onPostDataUpdating() {
        updatedData = true;
        adapter.updateListFromDatabase();
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory selectedCategory = adapter.getItem(position);
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategoryToShow, selectedCategory);
        startActivity(intent);
    }
}
