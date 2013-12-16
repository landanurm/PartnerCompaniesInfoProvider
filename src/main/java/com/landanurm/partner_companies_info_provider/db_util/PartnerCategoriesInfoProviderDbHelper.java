package com.landanurm.partner_companies_info_provider.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnersContract;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerCategoriesInfoProviderDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PARTNER_CATEGORIES_TABLE =
            "CREATE TABLE " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.TABLE_NAME + " (" +
                    PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                    PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS + " BLOB NOT NULL" +
                    " )";

    private static final String SQL_CREATE_PARTNERS_TABLE =
            "CREATE TABLE " + PartnersContract.TABLE_NAME + " (" +
                    PartnersContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PartnersContract.COLUMN_NAME_PARTNER_ID + " INTEGER NOT NULL, " +
                    PartnersContract.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                    PartnersContract.COLUMN_NAME_FULL_TITLE + " TEXT NOT NULL, " +
                    PartnersContract.COLUMN_NAME_SALE_TYPE + " TEXT NOT NULL, " +
                    PartnersContract.COLUMN_NAME_PARTNER_POINTS + " BLOB NOT NULL" +
            " )";

    private static final String SQL_DELETE_PARTNER_CATEGORIES_TABLE =
            "DROP TABLE IF EXISTS " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.TABLE_NAME;

    private static final String SQL_DELETE_PARTNERS_TABLE =
            "DROP TABLE IF EXISTS " + PartnersContract.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PartnerCategoriesInfoProvider.db";

    public PartnerCategoriesInfoProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_PARTNERS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_DELETE_PARTNERS_TABLE);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
