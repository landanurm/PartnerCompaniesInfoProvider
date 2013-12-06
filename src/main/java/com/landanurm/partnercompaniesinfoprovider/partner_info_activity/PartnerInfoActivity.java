package com.landanurm.partnercompaniesinfoprovider.partner_info_activity;

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
import com.landanurm.partnercompaniesinfoprovider.Keys;
import com.landanurm.partnercompaniesinfoprovider.R;
import com.landanurm.partnercompaniesinfoprovider.data_structure.Partner;
import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerPoint;

import java.util.List;

public class PartnerInfoActivity extends Activity {

    private GoogleMap googleMap;
    private Partner partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        googleMap = getMap();

        if (savedInstanceState == null) {
            partner = getPartnerPassedThroughIntent();
        } else {
            partner = restoreSavedPartner(savedInstanceState);
        }
        showPartnerInfo();
    }

    @SuppressLint("NewApi")
    private GoogleMap getMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        return mapFragment.getMap();
    }

    private Partner getPartnerPassedThroughIntent() {
        Intent intent = getIntent();
        return (Partner) intent.getSerializableExtra(Keys.partner);
    }

    private Partner restoreSavedPartner(Bundle savedInstanceState) {
        return (Partner) savedInstanceState.getSerializable(Keys.partner);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partner);
    }

    private void showPartnerInfo() {
        setTitle(partner.title);
        setTextView(R.id.full_title, partner.fullTitle);
        setTextView(R.id.sale_type, partner.saleType);
        showPartnerPoints(partner.partnerPoints);
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
                .snippet(partnerPoint.address + "  " + getPhoneText() + " " + partnerPoint.phone)
                .position(new LatLng(partnerPoint.latitude, partnerPoint.longitude));
    }

    private String getPhoneText() {
        return getResources().getString(R.string.phoneText);
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
        for (PartnerPoint each : partner.partnerPoints) {
            builder.include(new LatLng(each.latitude, each.longitude));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        googleMap.moveCamera(cameraUpdate);
    }

    private boolean noPartnerPoints() {
        return partner.partnerPoints.isEmpty();
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }
}