package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderDbHelper;

/**
 * Created by Leonid on 17.12.13.
 */
class DataProviderBase {
    private final Context context;
    private PartnerCategoriesInfoProviderDbHelper dbHelper;
    protected SQLiteDatabase db;

    DataProviderBase(Context context) {
        this.context = context;
    }

    protected final void initDatabase() {
        dbHelper = new PartnerCategoriesInfoProviderDbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    protected final void releaseDatabase() {
        db.close();
        db = null;
        dbHelper.close();
        dbHelper = null;
    }
}
