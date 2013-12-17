package com.landanurm.partner_companies_info_provider.partner_list_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.db_util.data_providers.PartnersProvider;
import com.landanurm.partner_companies_info_provider.partner_activity.PartnerActivity;

import java.util.List;

public class PartnerListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private PartnerListAdapter adapter;
    private PartnerCategory partnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        partnerCategory = getPartnerCategory(savedInstanceState);
        setTitle(partnerCategory.title);

        PartnersProvider partnersProvider = new PartnersProvider(this);
        List<Partner> partners = partnersProvider.getPartners(partnerCategory);
        adapter = new PartnerListAdapter(this, partners);
        Log.d("landanurm", "number of partners: " + partners.size());

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private PartnerCategory getPartnerCategory(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getPartnerCategory(getIntent());
        } else {
            return restorePartnerCategory(savedInstanceState);
        }
    }

    private PartnerCategory getPartnerCategory(Intent intent) {
        return (PartnerCategory) intent.getSerializableExtra(Keys.partnerCategoryToShow);
    }

    private PartnerCategory restorePartnerCategory(Bundle savedInstanceState) {
        return (PartnerCategory) savedInstanceState.getSerializable(Keys.partnerCategoryToShow);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategoryToShow, partnerCategory);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Partner partnerToShow = adapter.getItem(position);
        Intent intent = new Intent(this, PartnerActivity.class);
        intent.putExtra(Keys.partnerToShow, partnerToShow);
        startActivity(intent);
    }
}
