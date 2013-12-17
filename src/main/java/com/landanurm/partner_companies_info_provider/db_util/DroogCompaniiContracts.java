package com.landanurm.partner_companies_info_provider.db_util;

import android.provider.BaseColumns;

/**
 * Created by Leonid on 09.12.13.
 */
public final class DroogCompaniiContracts {

    public DroogCompaniiContracts() { }

    public static abstract class PartnerCategoriesContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_categories";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public static abstract class PartnersContract implements BaseColumns {
        public static final String TABLE_NAME = "partners";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FULL_TITLE = "full_title";
        public static final String COLUMN_NAME_SALE_TYPE = "sale_type";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
    }

    public static abstract class PartnerPointsContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_points";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_PARTNER_ID = "partner_id";
    }
}