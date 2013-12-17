package com.landanurm.partner_companies_info_provider.db_util.data_structure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonid on 02.12.13.
 */
public class Partner implements Serializable {
    public final int id;
    public final String title;
    public final String fullTitle;
    public final String saleType;
    public final List<PartnerPoint> partnerPoints;

    public Partner(int id,
                   String title,
                   String fullTitle,
                   String saleType,
                   List<PartnerPoint> partnerPoints) {
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
               partnerPoints.equals(other.partnerPoints);
    }

    @Override
    public int hashCode() {
        return id + title.hashCode() + fullTitle.hashCode() +
                saleType.hashCode() + partnerPoints.hashCode();
    }


}
