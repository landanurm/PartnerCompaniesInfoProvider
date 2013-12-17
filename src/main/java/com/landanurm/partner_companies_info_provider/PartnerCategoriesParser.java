package com.landanurm.partner_companies_info_provider;

import android.util.Xml;

import com.landanurm.partner_companies_info_provider.db_util.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.db_util.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.db_util.data_structure.PartnerPoint;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerCategoriesParser {

    private static final String NAMESPACE = null;

    public List<PartnerCategory> parsePartnerCategories(InputStream in) throws Exception {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return parsePartnerCategories(parser);
        } finally {
            tryClose(in);
        }
    }

    private void tryClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    private List<PartnerCategory> parsePartnerCategories(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-categories");

        List<PartnerCategory> partnerCategories = new ArrayList<PartnerCategory>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner-category")) {
                partnerCategories.add(parsePartnerCategory(parser));
            } else {
                skip(parser);
            }
        }
        return partnerCategories;
    }

    private PartnerCategory parsePartnerCategory(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-category");

        String title = null;
        List<Partner> partners = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = parseTitle(parser);
            } else if (name.equals("partners")) {
                partners = parsePartners(parser);
            } else {
                skip(parser);
            }
        }
        return new PartnerCategory(title, partners);
    }

    private String parseTitle(XmlPullParser parser) throws Exception {
        return parseTextValueByTag(parser, "title");
    }

    private String parseTextValueByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return value;
    }

    private String readText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private List<Partner> parsePartners(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partners");

        List<Partner> partners = new ArrayList<Partner>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner")) {
                partners.add(parsePartner(parser));
            } else {
                skip(parser);
            }
        }
        return partners;
    }

    private Partner parsePartner(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner");

        Integer id = null;
        String title = null;
        String fullTitle = null;
        String saleType = null;
        List<PartnerPoint> partnerPoints = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                id = parseId(parser);
            } else if (name.equals("title")) {
                title = parseTitle(parser);
            } else if (name.equals("full-title")) {
                fullTitle = parseFullTitle(parser);
            } else if (name.equals("sale-type")) {
                saleType = parseSaleType(parser);
            } else if (name.equals("partner-points")) {
                partnerPoints = parsePartnerPoints(parser);
            } else {
                skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, saleType, partnerPoints);
    }

    private Integer parseId(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "id");
        int id = readInteger(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, "id");
        return id;
    }

    private int readInteger(XmlPullParser parser) throws Exception {
        return Integer.valueOf(readText(parser));
    }

    private String parseFullTitle(XmlPullParser parser) throws Exception {
        return parseTextValueByTag(parser, "full-title");
    }

    private String parseSaleType(XmlPullParser parser) throws Exception {
        return parseTextValueByTag(parser, "sale-type");
    }

    private List<PartnerPoint> parsePartnerPoints(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-points");

        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner-point")) {
                partnerPoints.add(parsePartnerPoint(parser));
            } else {
                skip(parser);
            }
        }
        return partnerPoints;
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-point");

        String title = null;
        String address = null;
        String phone = null;
        double longitude = 0.0;
        double latitude = 0.0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = parseTitle(parser);
            } else if (name.equals("address")) {
                address = parseAddress(parser);
            } else if (name.equals("phone")) {
                phone = parsePhone(parser);
            } else if (name.equals("longitude")) {
                longitude = parseLongitude(parser);
            } else if (name.equals("latitude")) {
                latitude = parseLatitude(parser);
            } else {
                skip(parser);
            }
        }
        return new PartnerPoint(title, address, phone, longitude, latitude);
    }

    private String parseAddress(XmlPullParser parser) throws Exception {
        return parseTextValueByTag(parser, "address");
    }

    private String parsePhone(XmlPullParser parser) throws Exception {
        return parseTextValueByTag(parser, "phone");
    }

    private double parseLongitude(XmlPullParser parser) throws Exception {
        return parseBigDecimalValueByTag(parser, "longitude");
    }

    private double parseBigDecimalValueByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        double result = readBigDecimal(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }

    private double readBigDecimal(XmlPullParser parser) throws Exception {
        return Double.valueOf(readText(parser));
    }

    private double parseLatitude(XmlPullParser parser) throws Exception {
        return parseBigDecimalValueByTag(parser, "latitude");
    }

    private void skip(XmlPullParser parser) throws Exception {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
