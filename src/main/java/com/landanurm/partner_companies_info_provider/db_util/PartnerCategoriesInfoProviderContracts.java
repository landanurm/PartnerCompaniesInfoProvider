package com.landanurm.partner_companies_info_provider.db_util;

import android.provider.BaseColumns;

/**
 * Created by Leonid on 09.12.13.
 */
public final class PartnerCategoriesInfoProviderContracts {

    public PartnerCategoriesInfoProviderContracts() { }

    public static abstract class PartnerCategoriesContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_categories";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PARTNER_IDS = "partner_ids";
    }

    public static abstract class PartnersContract implements BaseColumns {
        public static final String TABLE_NAME = "partners";

        public static final String COLUMN_NAME_PARTNER_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FULL_TITLE = "full_title";
        public static final String COLUMN_NAME_SALE_TYPE = "sale_type";
        public static final String COLUMN_NAME_PARTNER_POINTS = "partner_points";
    }
}