package com.landanurm.partnercompaniesinfoprovider.data_structure;

import java.util.Arrays;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerCategory {
    public final String title;
    public final Partner[] partners;

    public PartnerCategory(String title, Partner[] partners) {
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
        return title.equals(other.title) && Arrays.equals(partners, other.partners);
    }

    @Override
    public int hashCode() {
        return title.hashCode() + ArrayHashCodeCalculator.hashCodeOf(partners);
    }
}
