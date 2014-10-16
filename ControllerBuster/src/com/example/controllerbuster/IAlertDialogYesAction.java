package com.example.controllerbuster;

import android.content.Context;
import android.content.DialogInterface;

public interface IAlertDialogYesAction {
	Context getContext();
	void onAlertDialogYesPressed(DialogInterface dialog, int which);
}
