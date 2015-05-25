package com.delta.nittfest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

public class APost extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {

		// params[0] is body, params[1] is endpoint
		byte[] bytes = params[0].getBytes();
		URL url;

		HttpURLConnection conn = null;
		try {
			
			url = new URL(params[1]);
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			
			out.write(bytes);
			
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;

	}

	public void execute(String serverUrl, URL url) {

	}

	@Override
	protected void onPostExecute(String result) {

	}

}
