package com.landanurm.partner_companies_info_provider.data_structure;

import java.io.Serializable;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPoint implements Serializable {
    public final String title;
    public final String address;
    public final String phone;
    public final double longitude;
    public final double latitude;

    public PartnerPoint(String title,
                        String address,
                        String phone,
                        double longitude,
                        double latitude) {
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PartnerPoint)) {
            return false;
        }

        PartnerPoint other = (PartnerPoint) obj;
        return title.equals(other.title) &&
               address.equals(other.address) &&
               phone.equals(other.phone) &&
               (longitude == other.longitude) &&
               (latitude == other.latitude);
    }

    @Override
    public int hashCode() {
        return title.hashCode() + address.hashCode() + phone.hashCode()
                + ((Double) longitude).hashCode() + ((Double) latitude).hashCode();
    }
}
