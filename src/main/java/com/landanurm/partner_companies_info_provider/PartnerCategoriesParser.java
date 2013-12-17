package com.landanurm.partner_companies_info_provider;

import android.util.Xml;

import com.landanurm.partner_companies_info_provider.data_structure.Partner;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerCategory;
import com.landanurm.partner_companies_info_provider.data_structure.PartnerPoint;

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

    public ParsedData parsePartnerCategories(InputStream in) throws Exception {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return parse(parser);
        } finally {
            tryClose(in);
        }
    }

    private void tryClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    private ParsedData parse(XmlPullParser parser) throws Exception {
        List<PartnerCategory> outPartnerCategories = new ArrayList<PartnerCategory>();
        List<Partner> outPartners = new ArrayList<Partner>();
        List<PartnerPoint> outPartnerPoints = new ArrayList<PartnerPoint>();
        parse(parser, outPartnerCategories, outPartners, outPartnerPoints);
        return new ParsedData(outPartnerCategories, outPartners, outPartnerPoints);
    }

    private void parse(XmlPullParser parser,
                       List<PartnerCategory> outPartnerCategories,
                       List<Partner> outPartners,
                       List<PartnerPoint> outPartnerPoints) throws Exception {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-categories");

        int partnerCategoryId = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner-category")) {
                ++partnerCategoryId;
                PartnerCategory partnerCategory = parsePartnerCategory(
                        parser, outPartners, outPartnerPoints, partnerCategoryId
                );
                outPartnerCategories.add(partnerCategory);
            } else {
                skip(parser);
            }
        }
    }

    private PartnerCategory parsePartnerCategory(XmlPullParser parser,
                                                 List<Partner> outPartners,
                                                 List<PartnerPoint> outPartnerPoints,
                                                 int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-category");

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = parseTitle(parser);
            } else if (name.equals("partners")) {
                parsePartners(parser, outPartners, outPartnerPoints, partnerCategoryId);
            } else {
                skip(parser);
            }
        }
        return new PartnerCategory(partnerCategoryId, title);
    }

    private String parseTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, "title");
    }

    private String parseTextByTag(XmlPullParser parser, String tag) throws Exception {
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

    private void parsePartners(XmlPullParser parser,
                               List<Partner> outPartners,
                               List<PartnerPoint> outPartnerPoints,
                               int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partners");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner")) {
                Partner partner = parsePartner(parser, outPartnerPoints, partnerCategoryId);
                outPartners.add(partner);
            } else {
                skip(parser);
            }
        }
    }

    private Partner parsePartner(XmlPullParser parser,
                                 List<PartnerPoint> outPartnerPoints,
                                 int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner");

        int id = 0;
        String title = null;
        String fullTitle = null;
        String saleType = null;

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
                parsePartnerPoints(parser, outPartnerPoints, id);
            } else {
                skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, saleType, partnerCategoryId);
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
        return parseTextByTag(parser, "full-title");
    }

    private String parseSaleType(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, "sale-type");
    }

    private void parsePartnerPoints(XmlPullParser parser,
                                    List<PartnerPoint> outPartnerPoints,
                                    int partnerId)  throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "partner-points");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("partner-point")) {
                outPartnerPoints.add(parsePartnerPoint(parser, partnerId));
            } else {
                skip(parser);
            }
        }
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
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
        return new PartnerPoint(title, address, phone, longitude, latitude, partnerId);
    }

    private String parseAddress(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, "address");
    }

    private String parsePhone(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, "phone");
    }

    private double parseLongitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, "longitude");
    }

    private double parseLatitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, "latitude");
    }

    private double parseDoubleByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        double result = readDouble(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }

    private double readDouble(XmlPullParser parser) throws Exception {
        return Double.valueOf(readText(parser));
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
