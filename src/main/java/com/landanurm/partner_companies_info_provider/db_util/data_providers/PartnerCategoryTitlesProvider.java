package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.Cursor;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryTitlesProvider extends DataProviderBase {

    public PartnerCategoryTitlesProvider(Context context) {
        super(context);
    }

    public List<String> getPartnerCategoryTitles() {
        initDatabase();
        List<String> partnerCategoryTitles = getPartnerCategoryTitlesFromDatabase();
        releaseDatabase();
        return partnerCategoryTitles;
    }

    private List<String> getPartnerCategoryTitlesFromDatabase() {
        String sql = "SELECT " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_TITLE +
                " FROM " + PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        List<String> partnerCategoryTitles = getPartnerCategoryTitlesFromCursor(cursor);
        cursor.close();
        return partnerCategoryTitles;
    }

    private List<String> getPartnerCategoryTitlesFromCursor(Cursor cursor) {
        List<String> titles = new ArrayList<String>();
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesInfoProviderContracts.PartnerCategoriesContract.COLUMN_NAME_TITLE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(titleColumnIndex);
            titles.add(title);
            cursor.moveToNext();
        }
        return titles;
    }
}
