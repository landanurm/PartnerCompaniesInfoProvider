package com.landanurm.partner_companies_info_provider.data_structure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerCategory implements Serializable {
    public final String title;
    public final List<Partner> partners;

    public PartnerCategory(String title, List<Partner> partners) {
        this.title = title;
        this.partners = partners;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PartnerCategory)) {
            return false;
        }

        PartnerCategory other = (PartnerCategory) obj;
        return title.equals(other.title) && partners.equals(other.partners);
    }

    @Override
    public int hashCode() {
        return title.hashCode() + partners.hashCode();
    }
}
