package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.Cursor;

import com.landanurm.partner_companies_info_provider.SerializableConvertor;
import com.landanurm.partner_companies_info_provider.db_util.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.db_util.data_structure.PartnerPoint;

import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProviderContracts.PartnersContract;

import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersProvider extends DataProviderBase {

    public PartnersProvider(Context context) {
        super(context);
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
