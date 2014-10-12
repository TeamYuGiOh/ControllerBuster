package com.example.controllerbuster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.widget.FacebookDialog;

public class FacebookActivity extends Activity {
	private UiLifecycleHelper uiHelper;
	private Context context = this;
	 private Facebook facebook;
	    @SuppressWarnings("deprecation")
		private AsyncFacebookRunner mAsyncRunner;
	    String FILENAME = "AndroidSSO_data";
	    private SharedPreferences mPrefs;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		facebook = new Facebook("875306629147305");
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        postToWall();
//		uiHelper = new UiLifecycleHelper(this, null);
//		uiHelper.onCreate(savedInstanceState);
//
//		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
//				.setLink(null).build();
//		uiHelper.trackPendingDialogCall(shareDialog.present());
	}

	@SuppressWarnings("deprecation")
	public void postToWall() {
	    // post on user's wall.
	    facebook.dialog(this, "feed", new DialogListener() {
	 
	        @Override
	        public void onFacebookError(FacebookError e) {
	        }
	 
	        @Override
	        public void onError(DialogError e) {
	        }
	 
	        @Override
	        public void onComplete(Bundle values) {
	        }
	 
	        @Override
	        public void onCancel() {
	        }
	    });
	 
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		uiHelper.onActivityResult(requestCode, resultCode, data,
//				new FacebookDialog.Callback() {
//					@Override
//					public void onError(FacebookDialog.PendingCall pendingCall,
//							Exception error, Bundle data) {
//						// Log.e("Activity", String.format("Error: %s",
//						// error.toString()));
//						Toast.makeText(context,
//								String.format("Error: %s", error.toString()),
//								Toast.LENGTH_LONG).show();
//					}
//
//					@Override
//					public void onComplete(
//							FacebookDialog.PendingCall pendingCall, Bundle data) {
//						// Log.i("Activity", "Success!");
//						Toast.makeText(context, "Success", Toast.LENGTH_LONG)
//								.show();
//					}
//				});
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		uiHelper.onResume();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		uiHelper.onSaveInstanceState(outState);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		uiHelper.onPause();
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		uiHelper.onDestroy();
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.controllers_map) {
			Toast.makeText(this, "controllers_map", Toast.LENGTH_LONG).show();
			return true;
		} else if (id == R.id.home) {
			Intent intent = new Intent(FacebookActivity.this,
					MainActivity.class);
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
