package com.landanurm.partner_companies_info_provider.partner_list_activity;

import android.content.Context;

import com.landanurm.partner_companies_info_provider.simple_array_adapter.ItemToTitleConvertor;
import com.landanurm.partner_companies_info_provider.simple_array_adapter.SimpleArrayAdapter;

import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerListAdapter extends SimpleArrayAdapter<PartnerListItem> {

    public PartnerListAdapter(Context context, List<PartnerListItem> partners) {
        super(context, partners, new ItemToTitleConvertor<PartnerListItem>() {
            @Override
            public String getTitle(PartnerListItem item) {
                return item.title;
            }
        });
    }
}
