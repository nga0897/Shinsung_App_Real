package com.example.mmsapp.AlertError;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.mmsapp.R;

public class AlertError {

    public static void alertError(String text, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Warning!!!");
        alertDialog.setMessage(text); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
}
