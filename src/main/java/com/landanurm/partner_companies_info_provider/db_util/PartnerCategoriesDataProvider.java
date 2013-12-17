package com.landanurm.partner_companies_info_provider.db_util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.landanurm.partner_companies_info_provider.SerializableConvertor;
import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnersContract;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerCategoriesDataProvider {
    private final Context context;
    private PartnerCategoriesInfoProviderDbHelper dbHelper;
    private SQLiteDatabase db;

    public PartnerCategoriesDataProvider(Context context) {
        this.context = context;
    }
    
    private void initDatabase() {
        dbHelper = new PartnerCategoriesInfoProviderDbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    private void releaseDatabase() {
        db.close();
        db = null;
        dbHelper.close();
        dbHelper = null;
    }

    public List<String> getPartnerCategoryTitles() {
        initDatabase();
        List<String> partnerCategoryTitles = getPartnerCategoryTitlesFromDatabase();
        releaseDatabase();
        return partnerCategoryTitles;
    }

    private List<String> getPartnerCategoryTitlesFromDatabase() {
        String sql = "SELECT " + PartnerCategoriesContract.COLUMN_NAME_TITLE +
                     " FROM " + PartnerCategoriesContract.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        List<String> partnerCategoryTitles = getPartnerCategoryTitlesFromCursor(cursor);
        cursor.close();
        return partnerCategoryTitles;
    }

    private List<String> getPartnerCategoryTitlesFromCursor(Cursor cursor) {
        List<String> titles = new ArrayList<String>();
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_TITLE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(titleColumnIndex);
            titles.add(title);
            cursor.moveToNext();
        }
        return titles;
    }

    public List<PartnerListItem> getPartnerListItems(String partnerCategoryTitle) {
        initDatabase();
        List<PartnerListItem> partnerListItems = getPartnerListItemsFromDatabase(partnerCategoryTitle);
        releaseDatabase();
        return partnerListItems;
    }

    private List<PartnerListItem> getPartnerListItemsFromDatabase(String partnerCategoryTitle) {
        List<Integer> partnerIds = getPartnerIdsFromDatabase(partnerCategoryTitle);
        return getPartnerListItems(partnerIds);
    }

    private List<Integer> getPartnerIdsFromDatabase(String partnerCategoryTitle) {
        String sql = "SELECT " +
                          PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS +
                     " FROM " + PartnerCategoriesContract.TABLE_NAME +
                     " WHERE " + PartnerCategoriesContract.COLUMN_NAME_TITLE + " = ? ;";

        Cursor cursor = db.rawQuery(sql, new String[]{partnerCategoryTitle});
        List<Integer> partnerIds = getPartnerIdsFromCursor(cursor);
        cursor.close();
        return partnerIds;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getPartnerIdsFromCursor(Cursor cursor) {
        int indexOfPartnerIdsColumn = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS);
        cursor.moveToFirst();
        byte[] partnerIdsBlob = cursor.getBlob(indexOfPartnerIdsColumn);
        return (List<Integer>) SerializableConvertor.serializableFromBytes(partnerIdsBlob);
    }

    private List<PartnerListItem> getPartnerListItems(List<Integer> partnerIds) {
        String sql = "SELECT " +
                PartnersContract.COLUMN_NAME_PARTNER_ID + ", " +
                PartnersContract.COLUMN_NAME_TITLE +
                " FROM " + PartnersContract.TABLE_NAME + " " +
                prepareWhereCondition(partnerIds.size()) + ";";

        String[] selectionArgs = partnerIdsToStringArray(partnerIds);

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<PartnerListItem> items = getPartnerListItemsFromCursor(cursor);
        cursor.close();
        return items;
    }

    private String prepareWhereCondition(int numberOfIds) {
        if (numberOfIds <= 0) {
            return "";
        }
        StringBuilder condition = new StringBuilder();
        condition.append("WHERE ");
        for (int i = 0; i < numberOfIds; ++i) {
            if (i > 0) {
                condition.append("OR ");
            }
            condition.append(PartnersContract.COLUMN_NAME_PARTNER_ID);
            condition.append(" = ? ");
        }
        return condition.toString();
    }

    private String[] partnerIdsToStringArray(List<Integer> partnerIds) {
        String[] result = new String[partnerIds.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = String.valueOf(partnerIds.get(i));
        }
        return result;
    }

    private List<PartnerListItem> getPartnerListItemsFromCursor(Cursor cursor) {
        List<PartnerListItem> items = new ArrayList<PartnerListItem>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PartnerListItem item = getPartnerListItemFromCursor(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        return items;
    }

    private PartnerListItem getPartnerListItemFromCursor(Cursor cursor) {
        int id = cursor.getInt(
                cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_PARTNER_ID)
        );
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_TITLE)
        );
        return new PartnerListItem(id, title);
    }

    public Partner getPartnerById(int partnerId) {
        initDatabase();
        Partner partner = getPartnerByIdFromDatabase(partnerId);
        releaseDatabase();
        return partner;
    }

    private Partner getPartnerByIdFromDatabase(int partnerId) {
        String sql = "SELECT * FROM " + PartnersContract.TABLE_NAME +
                " WHERE " + PartnersContract.COLUMN_NAME_PARTNER_ID + " = ? ;";
        String[] selectionArgs = new String[] { String.valueOf(partnerId) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Partner partner = getPartnerFromCursor(cursor);
        cursor.close();
        return partner;
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_PARTNER_ID);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_TITLE);
        int fullTitleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_FULL_TITLE);
        int saleTypeColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_SALE_TYPE);
        int partnerPointsColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_PARTNER_POINTS);

        cursor.moveToFirst();
        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String fullTitle = cursor.getString(fullTitleColumnIndex);
        String saleType = cursor.getString(saleTypeColumnIndex);
        byte[] partnerPointsBlob = cursor.getBlob(partnerPointsColumnIndex);
        List<PartnerPoint> partnerPoints = (List<PartnerPoint>)
                SerializableConvertor.serializableFromBytes(partnerPointsBlob);

        return new Partner(id, title, fullTitle, saleType, partnerPoints);
    }
}