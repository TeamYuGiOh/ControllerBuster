package com.example.controllerbuster;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookPosting {
	private Context context;
	private String FACEBOOK_APP_ID; 
	private Facebook facebook;
	String FILENAME = "AndroidSSO_data";

	public FacebookPosting(Context context) {
		this.context = context;
		FACEBOOK_APP_ID = context.getResources().getString(R.string.facebook_app_id);
		facebook = new Facebook(FACEBOOK_APP_ID);
	}

	@SuppressWarnings("deprecation")
	public void postToWall() {
		facebook.dialog(this.context, "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
				Toast.makeText(context, "Sorry, the post didn't make it to your wall!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(DialogError e) {
				Toast.makeText(context, "Sorry, you can't post right now!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete(Bundle values) {
				Toast.makeText(context, "You successfully posted on your wall!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				Toast.makeText(context, "You canceled the wall post!", Toast.LENGTH_LONG).show();
			}
		});
	}
}
