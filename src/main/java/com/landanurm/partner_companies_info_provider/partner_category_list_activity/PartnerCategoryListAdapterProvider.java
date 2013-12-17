package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesDataProvider;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryListAdapterProvider {
    private final ArrayAdapter<String> adapter;
    private final List<String> categoryTitles;
    private final PartnerCategoriesDataProvider partnerCategoriesDataProvider;

    public PartnerCategoryListAdapterProvider(Context context) {
        partnerCategoriesDataProvider = new PartnerCategoriesDataProvider(context);
        categoryTitles = partnerCategoriesDataProvider.getPartnerCategoryTitles();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categoryTitles);
    }

    public PartnerCategoryListAdapterProvider(Context context, Serializable savedState) {
        partnerCategoriesDataProvider = new PartnerCategoriesDataProvider(context);
        categoryTitles = (List<String>) savedState;
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categoryTitles);
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    public void updateListFromDatabase() {
        categoryTitles.clear();
        categoryTitles.addAll(partnerCategoriesDataProvider.getPartnerCategoryTitles());
        adapter.notifyDataSetChanged();
    }

    public String getPartnerCategoryTitleByPosition(int position) {
        return adapter.getItem(position);
    }

    public Serializable getState() {
        return (Serializable) categoryTitles;
    }
}
