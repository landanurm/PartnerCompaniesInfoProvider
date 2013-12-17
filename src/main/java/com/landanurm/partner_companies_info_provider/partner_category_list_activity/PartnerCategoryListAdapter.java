package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;

import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.db_util.data_providers.PartnerCategoriesProvider;
import com.landanurm.partner_companies_info_provider.simple_array_adapter.ItemToTitleConvertor;
import com.landanurm.partner_companies_info_provider.simple_array_adapter.SimpleArrayAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {
    private final List<PartnerCategory> partnerCategories;
    private final PartnerCategoriesProvider partnerCategoriesProvider;

    public static PartnerCategoryListAdapter newInstance(Context context) {
        PartnerCategoriesProvider partnerCategoriesProvider = new PartnerCategoriesProvider(context);
        List<PartnerCategory> partnerCategories = partnerCategoriesProvider.getPartnerCategories();
        return new PartnerCategoryListAdapter(context, partnerCategoriesProvider, partnerCategories);
    }

    public static PartnerCategoryListAdapter newInstance(Context context, Serializable savedState) {
        PartnerCategoriesProvider partnerCategoriesProvider = new PartnerCategoriesProvider(context);
        List<PartnerCategory> partnerCategories = (List<PartnerCategory>) savedState;
        return new PartnerCategoryListAdapter(context, partnerCategoriesProvider, partnerCategories);

    }

    private PartnerCategoryListAdapter(Context context,
                                       PartnerCategoriesProvider partnerCategoriesProvider,
                                       List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories,
              new ItemToTitleConvertor<PartnerCategory>() {
                  @Override
                  public String getTitle(PartnerCategory item) {
                      return item.title;
                  }
              });
        this.partnerCategoriesProvider = partnerCategoriesProvider;
        this.partnerCategories = partnerCategories;
    }

    public void updateListFromDatabase() {
        partnerCategories.clear();
        partnerCategories.addAll(partnerCategoriesProvider.getPartnerCategories());
        notifyDataSetChanged();
    }

    public Serializable getState() {
        return (Serializable) partnerCategories;
    }
}
