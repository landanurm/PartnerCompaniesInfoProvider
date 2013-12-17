package com.landanurm.partner_companies_info_provider.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.landanurm.partner_companies_info_provider.SerializableConvertor;
import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnersContract;

import java.io.Serializable;
import java.util.ArrayList;
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

    public void saveToDatabase(List<PartnerCategory> partnerCategories) {
        PartnerCategoriesInfoProviderDbHelper dbHelper = new PartnerCategoriesInfoProviderDbHelper(context);
        db = dbHelper.getWritableDatabase();
        for (PartnerCategory each : partnerCategories) {
            saveToDatabase(each);
        }
        db.close();
        db = null;
        dbHelper.close();
    }

    private void saveToDatabase(PartnerCategory partnerCategory) {
        List<Integer> partnerIds = savePartnersToDatabase(partnerCategory.partners);
        savePartnerCategoryToDatabase(partnerCategory.title, partnerIds);
    }

    private List<Integer> savePartnersToDatabase(List<Partner> partners) {
        List<Integer> partnerIds = new ArrayList<Integer>();
        for (Partner each : partners) {
            savePartnerToDatabase(each);
            partnerIds.add(each.id);
        }
        return partnerIds;
    }

    private void savePartnerToDatabase(Partner partner) {
        String sql = "INSERT INTO " + PartnersContract.TABLE_NAME + " (" +
                          PartnersContract.COLUMN_NAME_PARTNER_ID + ", " +
                          PartnersContract.COLUMN_NAME_TITLE + ", " +
                          PartnersContract.COLUMN_NAME_FULL_TITLE + ", " +
                          PartnersContract.COLUMN_NAME_SALE_TYPE + ", " +
                          PartnersContract.COLUMN_NAME_PARTNER_POINTS +
                     ") VALUES(?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partner.id);
        insertStatement.bindString(2, partner.title);
        insertStatement.bindString(3, partner.fullTitle);
        insertStatement.bindString(4, partner.saleType);
        Serializable serializablePartnerPoints = (Serializable) partner.partnerPoints;
        byte[] partnerPointsBlob = SerializableConvertor.serializableToBytes(serializablePartnerPoints);
        insertStatement.bindBlob(5, partnerPointsBlob);
        insertStatement.executeInsert();
    }

    private void savePartnerCategoryToDatabase(String title, List<Integer> partnerIds) {
        String sql = "INSERT INTO " + PartnerCategoriesContract.TABLE_NAME + " (" +
                          PartnerCategoriesContract.COLUMN_NAME_TITLE + ", " +
                          PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS +
                     ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindString(1, title);
        byte[] partnerIdsBlob = SerializableConvertor.serializableToBytes((Serializable) partnerIds);
        insertStatement.bindBlob(2, partnerIdsBlob);
        insertStatement.executeInsert();
    }
}
