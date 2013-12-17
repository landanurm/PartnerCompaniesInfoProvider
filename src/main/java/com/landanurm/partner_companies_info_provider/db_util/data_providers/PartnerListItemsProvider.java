package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.Cursor;

import com.landanurm.partner_companies_info_provider.SerializableConvertor;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerListItemsProvider extends DataProviderBase {

    public PartnerListItemsProvider(Context context) {
        super(context);
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
                PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS +
                " FROM " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.TABLE_NAME +
                " WHERE " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_TITLE + " = ? ;";

        Cursor cursor = db.rawQuery(sql, new String[]{partnerCategoryTitle});
        List<Integer> partnerIds = getPartnerIdsFromCursor(cursor);
        cursor.close();
        return partnerIds;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getPartnerIdsFromCursor(Cursor cursor) {
        int indexOfPartnerIdsColumn = cursor.getColumnIndexOrThrow(PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_PARTNER_IDS);
        cursor.moveToFirst();
        byte[] partnerIdsBlob = cursor.getBlob(indexOfPartnerIdsColumn);
        return (List<Integer>) SerializableConvertor.serializableFromBytes(partnerIdsBlob);
    }

    private List<PartnerListItem> getPartnerListItems(List<Integer> partnerIds) {
        String sql = "SELECT " +
                PartnerCategoriesInfoProviderContracts.PartnersContract.COLUMN_NAME_PARTNER_ID + ", " +
                PartnerCategoriesInfoProviderContracts.PartnersContract.COLUMN_NAME_TITLE +
                " FROM " + PartnerCategoriesInfoProviderContracts.PartnersContract.TABLE_NAME + " " +
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
            condition.append(PartnerCategoriesInfoProviderContracts.PartnersContract.COLUMN_NAME_PARTNER_ID);
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
                cursor.getColumnIndexOrThrow(PartnerCategoriesInfoProviderContracts.PartnersContract.COLUMN_NAME_PARTNER_ID)
        );
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(PartnerCategoriesInfoProviderContracts.PartnersContract.COLUMN_NAME_TITLE)
        );
        return new PartnerListItem(id, title);
    }
}
