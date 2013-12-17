package com.landanurm.partner_companies_info_provider.partner_category_list_activity;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Leonid on 17.12.13.
 */
public class ProgressDialogProvider {
    public static ProgressDialog prepareProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please, wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        return progressDialog;
    }
}
