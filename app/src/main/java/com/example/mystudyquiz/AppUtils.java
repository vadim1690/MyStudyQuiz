package com.example.mystudyquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AppUtils {


    public static void showAlertDialog(Context context,String message){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}
