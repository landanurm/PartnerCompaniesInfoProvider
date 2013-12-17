package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProvider;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryListAdapterProvider {
    private final ArrayAdapter<String> adapter;
    private final List<String> categoryTitles;
    private final PartnerCategoriesInfoProvider partnerCategoriesInfoProvider;

    public PartnerCategoryListAdapterProvider(Context context) {
        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(context);
        categoryTitles = partnerCategoriesInfoProvider.getPartnerCategoryTitles();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categoryTitles);
    }

    public PartnerCategoryListAdapterProvider(Context context, Serializable savedState) {
        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(context);
        categoryTitles = (List<String>) savedState;
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categoryTitles);
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    public void updateListFromDatabase() {
        categoryTitles.clear();
        categoryTitles.addAll(partnerCategoriesInfoProvider.getPartnerCategoryTitles());
        adapter.notifyDataSetChanged();
    }

    public String getPartnerCategoryTitleByPosition(int position) {
        return adapter.getItem(position);
    }

    public Serializable getState() {
        return (Serializable) categoryTitles;
    }
}
