package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

/**
 * Created by Leonid on 03.12.13.
 */
public class DataUrlProvider {
    private static final String DATA_URL = "http://droogcompanii.ru/partner_categories.xml";

    public static String getDataUrl() {
        return DATA_URL;
    }
}
