package com.landanurm.partnercompaniesinfoprovider.partner_list_activity;

import android.content.Context;

import com.landanurm.partnercompaniesinfoprovider.data_structure.Partner;
import com.landanurm.partnercompaniesinfoprovider.simple_array_adapter.ItemToTitleConvertor;
import com.landanurm.partnercompaniesinfoprovider.simple_array_adapter.SimpleArrayAdapter;

import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerListAdapter extends SimpleArrayAdapter<Partner> {

    public PartnerListAdapter(Context context, List<Partner> partners) {
        super(context, partners, new ItemToTitleConvertor<Partner>() {
            @Override
            public String getTitle(Partner item) {
                return item.title;
            }
        });
    }
}
