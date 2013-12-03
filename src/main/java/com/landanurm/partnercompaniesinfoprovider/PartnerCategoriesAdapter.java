package com.landanurm.partnercompaniesinfoprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.landanurm.partnercompaniesinfoprovider.data_structure.PartnerCategory;

/**
 * Created by Leonid on 03.12.13.
 */
public class PartnerCategoriesAdapter extends ArrayAdapter<PartnerCategory> {

    public PartnerCategoriesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = getItemView(convertView);
        initItemViewByPosition(itemView, position);
        return itemView;
    }

    private View getItemView(View convertView) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            return layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        return convertView;
    }

    private void initItemViewByPosition(View itemView, int position) {
        PartnerCategory item = getItem(position);
        String title = (item != null) ? item.title : "";
        setTitle(itemView, title);
    }

    private void setTitle(View itemView, String title) {
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        textView.setText(title);
    }

}
