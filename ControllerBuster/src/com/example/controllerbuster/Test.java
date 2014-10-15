package com.example.controllerbuster;

import android.content.Context;
import android.widget.Toast;

public class Test implements IAsyncResponse{
	private Context context;
	public Test(Context context) {
		this.context = context;
	}
	public void test() {
		String URL_ALL_LOCATIONS = context.getResources().getString(R.string.url_all_locations);
		new HttpRequester(this).execute(URL_ALL_LOCATIONS, "GET");
	}

	@Override
	public void processFinish(String data) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "From test " + data, Toast.LENGTH_LONG).show();
	}
}
