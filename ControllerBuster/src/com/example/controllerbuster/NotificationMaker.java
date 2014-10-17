package com.example.controllerbuster;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationMaker {
	private Context mContext;
	
	public NotificationMaker(Context context) {
		this.mContext = context;
	}
	
	@SuppressLint("NewApi")
	public void MakeNotification(String title, String text) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this.mContext).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(text);
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = 
				(NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}
}
