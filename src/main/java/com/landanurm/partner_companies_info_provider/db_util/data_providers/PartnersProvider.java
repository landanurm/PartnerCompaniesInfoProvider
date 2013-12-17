package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.Cursor;

import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.db_util.DroogCompaniiContracts.PartnersContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersProvider extends BaseDataProvider {

    public PartnersProvider(Context context) {
        super(context);
    }

    public List<Partner> getPartners(PartnerCategory category) {
        initDatabase();
        List<Partner> partners = getPartnersByCategoryIdFromDatabase(category.id);
        releaseDatabase();
        return partners;
    }

    private List<Partner> getPartnersByCategoryIdFromDatabase(int categoryId) {
        String sql = "SELECT * FROM " + PartnersContract.TABLE_NAME +
                " WHERE " + PartnersContract.COLUMN_NAME_CATEGORY_ID + " = ? ;";
        String[] selectionArgs = { String.valueOf(categoryId) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Partner> partners = getPartnersFromCursor(cursor);
        cursor.close();
        return partners;
    }

    private List<Partner> getPartnersFromCursor(Cursor cursor) {
        List<Partner> partners = new ArrayList<Partner>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partners.add(getPartnerFromCursor(cursor));
            cursor.moveToNext();
        }
        return partners;
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_ID);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_TITLE);
        int fullTitleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_FULL_TITLE);
        int saleTypeColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_SALE_TYPE);
        int categoryIdColumnIndex = cursor.getColumnIndexOrThrow(PartnersContract.COLUMN_NAME_CATEGORY_ID);

        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String fullTitle = cursor.getString(fullTitleColumnIndex);
        String saleType = cursor.getString(saleTypeColumnIndex);
        int categoryId = cursor.getInt(categoryIdColumnIndex);

        return new Partner(id, title, fullTitle, saleType, categoryId);
    }
}
