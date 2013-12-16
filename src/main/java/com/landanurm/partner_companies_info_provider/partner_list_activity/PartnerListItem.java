package com.landanurm.partner_companies_info_provider.partner_list_activity;

import java.io.Serializable;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerListItem implements Serializable {
    public final int id;
    public final String title;

    public PartnerListItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PartnerListItem)) {
            return false;
        }
        PartnerListItem other = (PartnerListItem) obj;
        return (id == other.id) && title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return id + title.hashCode();
    }
}
