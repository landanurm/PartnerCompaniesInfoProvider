package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.content.Context;

import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.simple_array_adapter.ItemToTitleConvertor;
import com.landanurm.partner_companies_info_provider.simple_array_adapter.SimpleArrayAdapter;

import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {

    public PartnerCategoryListAdapter(Context context, List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories, new ItemToTitleConvertor<PartnerCategory>() {
            @Override
            public String getTitle(PartnerCategory item) {
                return item.title;
            }
        });
    }
}
