package com.tealeaf.plugin.plugins;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NativeDialogPlugin implements IPlugin {
	public class NativeDialogEvent extends com.tealeaf.event.Event {
		public static final int POSITIVE_BUTTON = 0;
		public static final int NEGATIVE_BUTTON = 1;
		public static final int NEUTRAL_BUTTON = 2;
		int buttonPressed;

		public NativeDialogEvent(int code) {
			super("nativedialog");
			this.buttonPressed = code;
		}
	}

	private static final String TAG = "{NativeDialogPlugin}";
	private Activity mActivity;

	public void NativeDialogPlugin() {}
	public void onCreateApplication(Context applicationContext) {}

	private final int BUTTON_COUNT = 3;

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		mActivity = activity;
	}

	public void onResume() {}

	public void onStart() {}

	public void onPause() {}

	public void onStop() {}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {}

	public void setInstallReferrer(String referrer) {}

	public void onActivityResult(Integer request, Integer result, Intent data) {
		android.util.Log.d("Tiendv", "onActivityResult =============== request:  " + request.toString() + " -- result: "+ result.toString());
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {}

	private static boolean getJsonBoolean(JSONObject jObject, String key) {
		if (jObject == null) {
			return false;
		}

		boolean res = false;
		try {
			res = jObject.getBoolean(key);
		} catch (Exception ex) {}
		return res;
	}

	private static String getJsonString(JSONObject jObject, String key) {
		if (jObject == null) {
			return "";
		}

		String res = "";
		try {
			res = jObject.getString(key);
		} catch (Exception ex) {}
		return res;
	}

	private static JSONArray getJsonArray(JSONObject jObject, String key) {
		if (jObject == null) {
			return null;
		}

		JSONArray res = null;
		try {
			res = jObject.getJSONArray(key);
		} catch (Exception ex) {}
		return res;

	}
	
	public void showDialog(String jsonData) {
		android.util.Log.d(TAG, "Call to show dialogBuilder hehe");
		try {
			JSONObject jobject = new JSONObject(jsonData);

			// Get Title
			String title = getJsonString(jobject, "title");

			// Get List of message
			JSONArray messages = getJsonArray(jobject, "messages");
			String msg = "";
			for (int i = 0; i < messages.length(); i++) {
				String message = messages.getString(i);
				msg += message + "\n";
			}

			JSONArray buttons = getJsonArray(jobject, "buttons");

			showDialog(mActivity, title, msg, buttons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void showDialog(final Activity act, final CharSequence title, final CharSequence message, final JSONArray buttons) {

		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(act);
				dialogBuilder.setTitle(title);
				dialogBuilder.setMessage(message);
				try {
					if (buttons.length() >= 1) {
						dialogBuilder.setPositiveButton(buttons.getString(0), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogBuilderInterface, int i) {
								android.util.Log.d(TAG, "onClick Positive Button");
								EventQueue.pushEvent(new NativeDialogEvent(NativeDialogEvent.POSITIVE_BUTTON));
							}
						});
					}

					if (buttons.length() >= 2) {
						dialogBuilder.setNegativeButton(buttons.getString(1), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogBuilderInterface, int i) {
								android.util.Log.d(TAG, "onClick Negative Button");
								EventQueue.pushEvent(new NativeDialogEvent(NativeDialogEvent.NEGATIVE_BUTTON));
							}
						});
					}

					if (buttons.length() >= 3) {
						dialogBuilder.setNeutralButton(buttons.getString(2), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogBuilderInterface, int i) {
								android.util.Log.d(TAG, "onClick Negative Button");
								EventQueue.pushEvent(new NativeDialogEvent(NativeDialogEvent.NEUTRAL_BUTTON));
							}
						});
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}


				AlertDialog dialog = dialogBuilder.show();
				dialog.setCanceledOnTouchOutside(false);

				// Must call show() prior to fetching text view
				try {
					TextView titleView = (TextView)dialog.findViewById(act.getResources().getIdentifier("alertTitle", "id", "android"));
					titleView.setGravity(Gravity.CENTER);		

					TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
					messageView.setGravity(Gravity.CENTER);		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}