package com.landanurm.partnercompaniesinfoprovider.partner_category_list_activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partnercompaniesinfoprovider.Keys;
import com.landanurm.partnercompaniesinfoprovider.partner_list_activity.PartnerListActivity;
import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartnerCategoryListActivity extends ListActivity implements OnParsingProgressListener, AdapterView.OnItemClickListener {

    private PartnerCategoryListAdapter adapter;
    private List<PartnerCategory> partnerCategories;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        partnerCategories = new ArrayList<PartnerCategory>();
        adapter = new PartnerCategoryListAdapter(this, partnerCategories);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        progressDialog = prepareProgressDialog();

        if (savedInstanceState == null) {
            runDataUpdating();
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

    private void runDataUpdating() {
        final PartnerCategoriesParsingTask task = new PartnerCategoriesParsingTask(this, this);
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
        outState.putSerializable(Keys.partnerCategories, (Serializable) partnerCategories);
    }

    private void restoreState(Bundle savedInstanceState) {
        List<PartnerCategory> savedPartnerCategories =
                (List<PartnerCategory>) savedInstanceState.getSerializable(Keys.partnerCategories);
        updatePartnerCategoriesList(savedPartnerCategories);
    }

    private void updatePartnerCategoriesList(List<PartnerCategory> updatedPartnerCategories) {
        partnerCategories.clear();
        partnerCategories.addAll(updatedPartnerCategories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPreParsingExecute() {
        progressDialog.show();
    }

    @Override
    public void onPostParsingExecute(List<PartnerCategory> result) {
        if (result != null) {
            updatePartnerCategoriesList(result);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory partnerCategory = adapter.getItem(position);
        showPartnerCategoryInfo(partnerCategory);
    }

    private void showPartnerCategoryInfo(PartnerCategory partnerCategory) {
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategory, (Serializable) partnerCategory);
        startActivity(intent);
    }
}
