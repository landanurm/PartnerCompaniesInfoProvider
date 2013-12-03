package com.landanurm.partnercompaniesinfoprovider.data_structure;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Leonid on 02.12.13.
 */
public class Partner implements Serializable {
    public final int id;
    public final String title;
    public final String fullTitle;
    public final String saleType;
    public final PartnerPoint[] partnerPoints;

    public Partner(int id,
                   String title,
                   String fullTitle,
                   String saleType,
                   PartnerPoint[] partnerPoints) {
        this.id = id;
        this.title = title;
        this.fullTitle = fullTitle;
        this.saleType = saleType;
        this.partnerPoints = partnerPoints;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Partner)) {
            return false;
        }

        Partner other = (Partner) obj;
        return (id == other.id) &&
               title.equals(other.title) &&
               fullTitle.equals(other.fullTitle) &&
               saleType.equals(other.saleType) &&
               Arrays.equals(partnerPoints, other.partnerPoints);
    }

    @Override
    public int hashCode() {
        return id + title.hashCode() + fullTitle.hashCode() + saleType.hashCode()
                + ArrayHashCodeCalculator.hashCodeOf(partnerPoints);
    }


}
