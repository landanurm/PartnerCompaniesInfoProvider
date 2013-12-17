package com.landanurm.partner_companies_info_provider.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.landanurm.partner_companies_info_provider.ParsedData;
import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;
import com.landanurm.partner_companies_info_provider.db_util.DroogCompaniiContracts.PartnerCategoriesContract;
import com.landanurm.partner_companies_info_provider.db_util.DroogCompaniiContracts.PartnerPointsContract;
import com.landanurm.partner_companies_info_provider.db_util.DroogCompaniiContracts.PartnersContract;

import java.util.List;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerCategoriesSaver {
    private final Context context;
    private SQLiteDatabase db;

    public PartnerCategoriesSaver(Context context) {
        this.context = context;
    }

    public void saveToDatabase(ParsedData parsedData) {
        DroogCompaniiDbHelper dbHelper = new DroogCompaniiDbHelper(context);
        db = dbHelper.getWritableDatabase();
        savePartnerCategories(parsedData.partnerCategories);
        savePartners(parsedData.partners);
        savePartnerPoints(parsedData.partnerPoints);
        db.close();
        db = null;
        dbHelper.close();
    }

    private void savePartnerCategories(List<PartnerCategory> partnerCategories) {
        for (PartnerCategory each : partnerCategories) {
            savePartnerCategory(each);
        }
    }

    private void savePartnerCategory(PartnerCategory partnerCategory) {
        String sql = "INSERT INTO " + PartnerCategoriesContract.TABLE_NAME + " (" +
                         PartnerCategoriesContract.COLUMN_NAME_ID + ", " +
                         PartnerCategoriesContract.COLUMN_NAME_TITLE +
                     ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerCategory.id);
        insertStatement.bindString(2, partnerCategory.title);
        insertStatement.executeInsert();
    }

    private void savePartners(List<Partner> partners) {
        for (Partner each : partners) {
            savePartner(each);
        }
    }

    private void savePartner(Partner partner) {
        String sql = "INSERT INTO " + PartnersContract.TABLE_NAME + " (" +
                         PartnersContract.COLUMN_NAME_ID + ", " +
                         PartnersContract.COLUMN_NAME_TITLE + ", " +
                         PartnersContract.COLUMN_NAME_FULL_TITLE + ", " +
                         PartnersContract.COLUMN_NAME_SALE_TYPE + ", " +
                         PartnersContract.COLUMN_NAME_CATEGORY_ID +
                     ") VALUES(?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partner.id);
        insertStatement.bindString(2, partner.title);
        insertStatement.bindString(3, partner.fullTitle);
        insertStatement.bindString(4, partner.saleType);
        insertStatement.bindLong(5, partner.categoryId);
        insertStatement.executeInsert();
    }

    private void savePartnerPoints(List<PartnerPoint> partnerPoints) {
        for (PartnerPoint each : partnerPoints) {
            savePartnerPoint(each);
        }
    }

    private void savePartnerPoint(PartnerPoint partnerPoint) {
        String sql = "INSERT INTO " + PartnerPointsContract.TABLE_NAME + " (" +
                         PartnerPointsContract.COLUMN_NAME_TITLE + ", " +
                         PartnerPointsContract.COLUMN_NAME_ADDRESS + ", " +
                         PartnerPointsContract.COLUMN_NAME_PHONE + ", " +
                         PartnerPointsContract.COLUMN_NAME_LONGITUDE + ", " +
                         PartnerPointsContract.COLUMN_NAME_LATITUDE + ", " +
                         PartnerPointsContract.COLUMN_NAME_PARTNER_ID +
                     ") VALUES(?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindString(1, partnerPoint.title);
        insertStatement.bindString(2, partnerPoint.address);
        insertStatement.bindString(3, partnerPoint.phone);
        insertStatement.bindDouble(4, partnerPoint.longitude);
        insertStatement.bindDouble(5, partnerPoint.latitude);
        insertStatement.bindLong(6, partnerPoint.partnerId);
        insertStatement.executeInsert();
    }
}
