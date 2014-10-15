package com.example.controllerbuster;

import com.google.android.gms.wearable.DataApi.GetFdForAssetResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogMaker {
	private Context context;
	
	public AlertDialogMaker(Context context) {
		this.context = context;
	}
	
	public void MakeAlert(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setInverseBackgroundForced(true);
		builder.setCancelable(false);
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						MainActivity mainActivity = (MainActivity) context;
						mainActivity.finish();
					}
				});
		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setMessage(message);
		alert.show();
	}
}
