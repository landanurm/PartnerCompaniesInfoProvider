package com.landanurm.partner_companies_info_provider.partner_info_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.landanurm.partner_companies_info_provider.Keys;
import com.landanurm.partner_companies_info_provider.R;
import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;
import com.landanurm.partner_companies_info_provider.db_util.PartnerCategoriesInfoProvider;
import com.landanurm.partner_companies_info_provider.partner_list_activity.PartnerListItem;

import java.util.List;

public class PartnerInfoActivity extends Activity {

    private GoogleMap googleMap;
    private Partner partnerToShow;
    private PartnerCategoriesInfoProvider partnerCategoriesInfoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        partnerCategoriesInfoProvider = new PartnerCategoriesInfoProvider(this);

        partnerToShow = getPartnerToShow(savedInstanceState);
        googleMap = getMap();
        showPartnerInfo();
    }

    private Partner getPartnerToShow(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getPartnerToShow();
        } else {
            return restorePartnerToShow(savedInstanceState);
        }
    }

    private Partner getPartnerToShow() {
        PartnerListItem partnerListItem = (PartnerListItem)
                getIntent().getSerializableExtra(Keys.partnerListItemToShow);
        return partnerCategoriesInfoProvider.getPartnerById(partnerListItem.id);
    }

    private Partner restorePartnerToShow(Bundle savedInstanceState) {
        return (Partner) savedInstanceState.getSerializable(Keys.partner);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partnerToShow);
    }

    @SuppressLint("NewApi")
    private GoogleMap getMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        return mapFragment.getMap();
    }

    private void showPartnerInfo() {
        setTitle(partnerToShow.title);
        setTextView(R.id.full_title, partnerToShow.fullTitle);
        setTextView(R.id.sale_type, partnerToShow.saleType);
        showPartnerPoints(partnerToShow.partnerPoints);
    }

    private void setTextView(int textViewId, String text) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(text);
    }

    private void showPartnerPoints(List<PartnerPoint> partnerPoints) {
        for (PartnerPoint eachPartnerPoint : partnerPoints) {
            googleMap.addMarker(prepareMarkerOption(eachPartnerPoint));
        }
        runAfterCreatingMapView(new Runnable() {
            @Override
            public void run() {
                fitAllPartnerPointsOnScreen();
            }
        });
    }

    private MarkerOptions prepareMarkerOption(PartnerPoint partnerPoint) {
        return new MarkerOptions()
                .title(partnerPoint.title)
                .snippet(prepareSnippet(partnerPoint))
                .position(new LatLng(partnerPoint.latitude, partnerPoint.longitude));
    }

    private String prepareSnippet(PartnerPoint partnerPoint) {
        String snippet = "";
        if (partnerPointHasAddress(partnerPoint)) {
            snippet = partnerPoint.address + "  ";
        }
        if (partnerPointHasPhoneNumber(partnerPoint)) {
            snippet += (getPhoneDescriptionText() + " " + partnerPoint.phone);
        }
        return snippet;
    }

    private boolean partnerPointHasAddress(PartnerPoint partnerPoint) {
        return notEmpty(partnerPoint.address);
    }

    private boolean partnerPointHasPhoneNumber(PartnerPoint partnerPoint) {
        return notEmpty(partnerPoint.phone);
    }

    private boolean notEmpty(String str) {
        return !str.trim().isEmpty();
    }

    private String getPhoneDescriptionText() {
        return getResources().getString(R.string.phoneDescriptionText);
    }

    private void runAfterCreatingMapView(final Runnable runnable) {
        final View mapView = getMapView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    removeOnGlobalLayoutListener(mapView, this);
                    runnable.run();
                }
            });
        }
    }

    @SuppressLint("NewApi")
    private View getMapView() {
        return ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getView();
    }

    private static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    private void fitAllPartnerPointsOnScreen() {
        if (noPartnerPoints()) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PartnerPoint each : partnerToShow.partnerPoints) {
            builder.include(new LatLng(each.latitude, each.longitude));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        googleMap.moveCamera(cameraUpdate);

    }

    private boolean noPartnerPoints() {
        return partnerToShow.partnerPoints.isEmpty();
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }
}
