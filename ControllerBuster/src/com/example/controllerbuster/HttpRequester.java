package com.example.controllerbuster;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequester extends AsyncTask<String, Void, String> {
	private final String USER_AGENT = "Mozilla/5.0";
	public IAsyncResponse delegate;

	public HttpRequester(IAsyncResponse delegate) {
		this.delegate = delegate;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			String tag = "HttpRequester";
			Log.d(tag, "in");
			String url = params[0];
			String requestMethod = params[1];
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj
					.openConnection();
			con.setRequestMethod(requestMethod);

			con.setRequestProperty("User-Agent", USER_AGENT);
			if (requestMethod == "POST") {
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				String urlParameters = params[2];
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
			}

			int responseCode = con.getResponseCode();
			Log.d(tag, "Sending");
			Log.d(tag, "Response code " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Log.d(tag, response.toString());
			return response.toString();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(String data) {
		delegate.processFinish(data);
	}
}
