package com.landanurm.partnercompaniesinfoprovider.data_structure;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPoint implements Serializable {
    public final String title;
    public final String address;
    public final String phone;
    public final BigDecimal longitude;
    public final BigDecimal latitude;

    public PartnerPoint(String title,
                        String address,
                        String phone,
                        BigDecimal longitude,
                        BigDecimal latitude) {
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
               longitude.equals(other.longitude) &&
               latitude.equals(other.latitude);
    }

    @Override
    public int hashCode() {
        return title.hashCode() + address.hashCode() + phone.hashCode()
                + longitude.hashCode() + latitude.hashCode();
    }
}
