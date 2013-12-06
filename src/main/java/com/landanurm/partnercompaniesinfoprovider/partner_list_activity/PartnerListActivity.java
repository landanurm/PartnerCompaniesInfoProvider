package com.landanurm.partnercompaniesinfoprovider.partner_list_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partnercompaniesinfoprovider.Keys;
import com.landanurm.partnercompaniesinfoprovider.data_structure.Partner;
import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;
import com.landanurm.partnercompaniesinfoprovider.partner_info_activity.PartnerInfoActivity;

import java.io.Serializable;

public class PartnerListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private PartnerCategory partnerCategory;
    private PartnerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            partnerCategory = getPartnerCategoryPassedThroughIntent();
        } else {
            partnerCategory = restoreSavedPartnerCategory(savedInstanceState);
        }

        setTitle(partnerCategory.title);

        adapter = new PartnerListAdapter(this, partnerCategory.partners);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private PartnerCategory getPartnerCategoryPassedThroughIntent() {
        Intent intent = getIntent();
        return (PartnerCategory) intent.getSerializableExtra(Keys.partnerCategory);
    }

    private PartnerCategory restoreSavedPartnerCategory(Bundle savedInstanceState) {
        return (PartnerCategory) savedInstanceState.getSerializable(Keys.partnerCategory);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        showPartnerInfo(partner);
    }

    private void showPartnerInfo(Partner partner) {
        Intent intent = new Intent(this, PartnerInfoActivity.class);
        intent.putExtra(Keys.partner, (Serializable) partner);
        startActivity(intent);
    }
}
