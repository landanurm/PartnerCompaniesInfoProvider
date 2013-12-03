package com.landanurm.partnercompaniesinfoprovider.partner_list_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.landanurm.partnercompaniesinfoprovider.Keys;
import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

public class PartnerListActivity extends ListActivity {

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

}
