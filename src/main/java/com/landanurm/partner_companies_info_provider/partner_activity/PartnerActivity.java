package com.landanurm.partner_companies_info_provider.partner_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.landanurm.partner_companies_info_provider.db_util.data_providers.PartnerPointsProvider;

import java.io.Serializable;
import java.util.List;

public class PartnerActivity extends Activity {

    private GoogleMap googleMap;
    private List<PartnerPoint> partnerPointsToShow;
    private Partner partnerToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        prepareDataToShow(savedInstanceState);

        googleMap = getMap();
        showPartnerInfo();
    }

    private void prepareDataToShow(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            prepareDataToShow(getIntent());
        } else {
            restoreDataToShow(savedInstanceState);
        }
    }

    private void prepareDataToShow(Intent intent) {
        partnerToShow = (Partner) intent.getSerializableExtra(Keys.partnerToShow);
        PartnerPointsProvider partnerPointsProvider = new PartnerPointsProvider(this);
        partnerPointsToShow = partnerPointsProvider.getPartnerPoints(partnerToShow);
    }

    private void restoreDataToShow(Bundle savedInstanceState) {
        partnerToShow = (Partner) savedInstanceState.getSerializable(Keys.partnerToShow);
        partnerPointsToShow = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPointsToShow);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerToShow, partnerToShow);
        outState.putSerializable(Keys.partnerPointsToShow, (Serializable) partnerPointsToShow);
    }

    @SuppressLint("NewApi")
    private GoogleMap getMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        return mapFragment.getMap();
    }

    private void showPartnerInfo() {
        setTitle(partnerToShow.title);
        textViewById(R.id.full_title).setText(partnerToShow.fullTitle);
        textViewById(R.id.sale_type).setText(partnerToShow.saleType);
        showPartnerPoints();
    }

    private TextView textViewById(int textViewId) {
        return (TextView) findViewById(textViewId);
    }

    private void showPartnerPoints() {
        for (PartnerPoint each : partnerPointsToShow) {
            googleMap.addMarker(prepareMarkerOptions(each));
        }
        runAfterCreatingMapView(new Runnable() {
            @Override
            public void run() {
                fitAllPartnerPointsOnScreen();
            }
        });
    }

    private MarkerOptions prepareMarkerOptions(PartnerPoint partnerPoint) {
        return new MarkerOptions()
                .title(partnerPoint.title)
                .snippet(prepareSnippet(partnerPoint))
                .position(new LatLng(partnerPoint.latitude, partnerPoint.longitude));
    }

    private String prepareSnippet(PartnerPoint partnerPoint) {
        String snippet = "";
        if (partnerPointHasAddress(partnerPoint)) {
            snippet = (partnerPoint.address + "  ");
        }
        if (partnerPointHasPhoneNumber(partnerPoint)) {
            snippet += (getPhoneDescriptionText() + " " + partnerPoint.phone);
        }
        return snippet;
    }

    private boolean partnerPointHasAddress(PartnerPoint partnerPoint) {
        return containsNonWhitespaceCharacters(partnerPoint.address);
    }

    private boolean partnerPointHasPhoneNumber(PartnerPoint partnerPoint) {
        return containsNonWhitespaceCharacters(partnerPoint.phone);
    }

    private boolean containsNonWhitespaceCharacters(String text) {
        return !text.trim().isEmpty();
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
        for (PartnerPoint each : partnerPointsToShow) {
            builder.include(new LatLng(each.latitude, each.longitude));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        googleMap.moveCamera(cameraUpdate);
    }

    private boolean noPartnerPoints() {
        return partnerPointsToShow.isEmpty();
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }
}
