package com.landanurm.partnercompaniesinfoprovider.partner_category_list_activity;

import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public interface OnParsingProgressListener {
    void onPreParsingExecute();
    void onPostParsingExecute(List<PartnerCategory> result);
}
