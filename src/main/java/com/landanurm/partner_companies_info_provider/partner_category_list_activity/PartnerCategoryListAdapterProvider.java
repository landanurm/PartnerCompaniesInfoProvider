package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProvider;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryListAdapterProvider {
    private final ArrayAdapter<String> adapter;
    private final List<String> itemTitles;
    private final PartnerCategoriesInfoProvider partnerCategoriesInfoProvider;

    public PartnerCategoryListAdapterProvider(Context context) {
        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(context);
        itemTitles = partnerCategoriesInfoProvider.getPartnerCategoryTitles();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, itemTitles);
    }

    public PartnerCategoryListAdapterProvider(Context context, Bundle savedInstanceState) {
        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(context);
        itemTitles = (List<String>) savedInstanceState.getSerializable(Keys.partnerCategories);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, itemTitles);
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    public void updateDataFromDatabase() {
        itemTitles.clear();
        itemTitles.addAll(partnerCategoriesInfoProvider.getPartnerCategoryTitles());
        adapter.notifyDataSetChanged();
    }

    public String getPartnerCategoryTitleByPosition(int position) {
        return adapter.getItem(position);
    }

    public void saveStateInto(Bundle outState) {
        outState.putSerializable(Keys.partnerCategories, (Serializable) itemTitles);
    }
}
