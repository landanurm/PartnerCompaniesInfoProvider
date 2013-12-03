package com.landanurm.partnercompaniesinfoprovider.partner_category_list_activity;

import android.content.Context;

import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;
import com.landanurm.partnercompaniesinfoprovider.simple_array_adapter.ItemToTitleConvertor;
import com.landanurm.partnercompaniesinfoprovider.simple_array_adapter.SimpleArrayAdapter;

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
