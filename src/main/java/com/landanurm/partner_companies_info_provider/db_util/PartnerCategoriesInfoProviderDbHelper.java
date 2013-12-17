package com.landanurm.partner_companies_info_provider.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnersContract;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerCategoriesInfoProviderDbHelper extends SQLiteOpenHelper {

    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String BLOB_TYPE = " BLOB ";
    private static final String PRIMARY_KEY_AUTOINCREMENT_TYPE = " PRIMARY KEY AUTOINCREMENT ";
    private static final String COMMA = ", ";
    private static final String NOT_NULL = " NOT_NULL ";

    private static final String SQL_CREATE_PARTNER_CATEGORIES_TABLE =
            "CREATE TABLE " + PartnerCategoriesContract.TABLE_NAME + " (" +
                    PartnerCategoriesContract._ID + INTEGER_TYPE + " " + PRIMARY_KEY_AUTOINCREMENT_TYPE + COMMA +
                    PartnerCategoriesContract.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS + BLOB_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PARTNERS_TABLE =
            "CREATE TABLE " + PartnersContract.TABLE_NAME + " (" +
                    PartnersContract._ID + INTEGER_TYPE + " " + PRIMARY_KEY_AUTOINCREMENT_TYPE + COMMA +
                    PartnersContract.COLUMN_NAME_PARTNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_TITLE  + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_FULL_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_SALE_TYPE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_PARTNER_POINTS + BLOB_TYPE + NOT_NULL +
            " )";

    private static final String SQL_DELETE_PARTNER_CATEGORIES_TABLE =
            "DROP TABLE IF EXISTS " + PartnerCategoriesContract.TABLE_NAME;

    private static final String SQL_DELETE_PARTNERS_TABLE =
            "DROP TABLE IF EXISTS " + PartnersContract.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DroogCompanii.db";

    public PartnerCategoriesInfoProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_PARTNERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_DELETE_PARTNERS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
