package com.landanurm.partner_companies_info_provider.partner_list_activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.db_util.data_providers.PartnerListItemsProvider;
import com.landanurm.partner_companies_info_provider.partner_info_activity.PartnerInfoActivity;

import java.util.List;

public class PartnerListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<PartnerListItem> items;
    private PartnerListItemsProvider partnerListItemsProvider;
    private PartnerListAdapter adapter;
    private String partnerCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        partnerCategoryTitle = getPartnerCategoryTitle(savedInstanceState);
        setTitle(partnerCategoryTitle);

        partnerListItemsProvider = new PartnerListItemsProvider(this);

        items = partnerListItemsProvider.getPartnerListItems(partnerCategoryTitle);
        adapter = new PartnerListAdapter(this, items);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private String getPartnerCategoryTitle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getIntent().getStringExtra(Keys.partnerCategoryTitle);
        } else {
            return savedInstanceState.getString(Keys.partnerCategoryTitle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Keys.partnerCategoryTitle, partnerCategoryTitle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerListItem item = adapter.getItem(position);
        showPartnerInfo(item);
    }

    private void showPartnerInfo(PartnerListItem partnerListItem) {
        Intent intent = new Intent(this, PartnerInfoActivity.class);
        intent.putExtra(Keys.partnerListItemToShow, partnerListItem);
        startActivity(intent);
    }
}
