package com.landanurm.partner_companies_info_provider.db_util.data_providers;

import android.content.Context;
import android.database.Cursor;

import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;
import com.landanurm.partner_companies_info_provider.db_util.DroogCompaniiContracts.PartnerPointsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerPointsProvider extends BaseDataProvider {

    public PartnerPointsProvider(Context context) {
        super(context);
    }

    public List<PartnerPoint> getPartnerPoints(Partner partner) {
        initDatabase();
        List<PartnerPoint> partnerPoints = getPartnerPointsFromDatabase(partner.id);
        releaseDatabase();
        return partnerPoints;
    }

    private List<PartnerPoint> getPartnerPointsFromDatabase(int partnerId) {
        String sql = "SELECT * " + " FROM " + PartnerPointsContract.TABLE_NAME +
                     " WHERE " + PartnerPointsContract.COLUMN_NAME_PARTNER_ID + " = ? ;";
        String[] selectionArgs = { String.valueOf(partnerId) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<PartnerPoint> partnerPoints = getPartnerPointsFromCursor(cursor);
        cursor.close();
        return partnerPoints;
    }

    private List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor) {
        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partnerPoints.add(getPartnerPointFromCursor(cursor));
            cursor.moveToNext();
        }
        return partnerPoints;
    }

    private PartnerPoint getPartnerPointFromCursor(Cursor cursor) {
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_TITLE);
        int addressColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_ADDRESS);
        int phoneColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PHONE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LATITUDE);
        int partnerIdColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PARTNER_ID);

        String title = cursor.getString(titleColumnIndex);
        String address = cursor.getString(addressColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);
        double longitude = cursor.getDouble(longitudeColumnIndex);
        double latitude = cursor.getDouble(latitudeColumnIndex);
        int partnerId = cursor.getInt(partnerIdColumnIndex);

        return new PartnerPoint(title, address, phone, longitude, latitude, partnerId);
    }
}
