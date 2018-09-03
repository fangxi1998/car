package com.example.fangxi1998.update;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

	public static void getDataJSON(final String url, final Handler handler) {
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection conn;
				InputStream is;
				try {
					conn = (HttpURLConnection) new URL(url).openConnection();
					conn.setRequestMethod("GET");
					is = conn.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));
					String line = "";
					StringBuilder result = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						result.append(line);
					}
					Message msg = new Message();
					msg.obj = result.toString();
					handler.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	

}
