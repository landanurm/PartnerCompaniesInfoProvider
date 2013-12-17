package com.landanurm.partner_companies_info_provider;

import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;

import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class ParsedData {
    public final List<PartnerCategory> partnerCategories;
    public final List<Partner> partners;
    public final List<PartnerPoint> partnerPoints;

    public ParsedData(List<PartnerCategory> partnerCategories,
                      List<Partner> partners,
                      List<PartnerPoint> partnerPoints) {
        this.partnerCategories = partnerCategories;
        this.partners = partners;
        this.partnerPoints = partnerPoints;
    }
}
