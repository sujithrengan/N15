package com.delta.nittfest;



import java.util.concurrent.ExecutionException;
import static com.delta.nittfest.CommonUtilities.SENDER_ID;
import static com.delta.nittfest.CommonUtilities.displayMessage;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;


import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(TAG);
	}

	final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
	String registrationId = prefs.getString("registration_id", "");

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { 

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				displayMessage(getApplicationContext(),
						getString(R.string.gcm_error, extras.toString()));
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				displayMessage(getApplicationContext(),
						getString(R.string.gcm_error, extras.toString()));
				try {
					ServerUtilities.unregister(getApplicationContext(),
							registrationId);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				
				try {
					ServerUtilities.register(getApplicationContext(), registrationId);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			Myreceiver.completeWakefulIntent(intent);
		}

	}

	private SharedPreferences getGCMPreferences(Context applicationContext) {
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

}
