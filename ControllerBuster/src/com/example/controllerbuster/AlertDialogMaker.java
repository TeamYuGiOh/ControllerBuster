package com.example.controllerbuster;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogMaker {
	private IAlertDialogYesAction delegate;

	public AlertDialogMaker(IAlertDialogYesAction delegate) {
		this.delegate = delegate;
	}

	public void makeChoiceAlert(String title, String message,
			String positiveButton, String negativeButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.delegate.getContext());
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setInverseBackgroundForced(true);
		builder.setCancelable(false);

		if (positiveButton != null) {
			builder.setPositiveButton(positiveButton,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							delegate.onAlertDialogYesPressed(dialog, which);
						}
					});
		}
		builder.setNegativeButton(negativeButton,
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

	public void makeConfirmAlert(String title, String message,
			String confirmButton) {
		this.makeChoiceAlert(title, message, null, confirmButton);
	}
}
