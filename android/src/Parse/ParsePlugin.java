package com.tealeaf.plugin.plugins;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.FindCallback;

import java.util.List;

public class ParsePlugin implements IPlugin {
	public class ParseEvent extends com.tealeaf.event.Event {
		int rank;
		int problems;
		String player_name = "";
		double score;

		public ParseEvent(int rank, int problems, String name, double score) {
			super("parseevent");
			this.rank = rank;
			this.problems = problems;
			this.player_name = name;
			this.score = score;
		}
	}

	private static final String TAG = "{NativeDialogPlugin}";
	private Activity mActivity;

	public void ParsePlugin() {}
	public void onCreateApplication(Context applicationContext) {
	    // Enable Local Datastore.
	    Parse.enableLocalDatastore(applicationContext);

	    // Add your initialization code here
	    Parse.initialize(applicationContext, "UJZ2OlusGRE01wEIuLkG4UjBAgzyQCB5l2Zf7fkL", "J3kEwPYFbU7G1h85RbPLHyOgMCQvofkZ5EinJiRS");
	    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

	    ParseUser.enableAutomaticUser();
	    ParseACL defaultACL = new ParseACL();
	    // Optionally enable public read access.
	    // defaultACL.setPublicReadAccess(true);
	    ParseACL.setDefaultACL(defaultACL, true);
	}

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

	private static int getJsonInt(JSONObject jObject, String key) {
		if (jObject == null) {
			return -1;
		}

		int res = -1;
		try {
			res = jObject.getInt(key);
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

	public void sendTestEvent(String jsonData) {
		android.util.Log.d("Tiendv parse", "HIhihihihih 2");
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					android.util.Log.d("Tiendv parse", "succedded");
					// myObjectSavedSuccessfully();
				} else {
					android.util.Log.d("Tiendv parse", "failed");
					e.printStackTrace();
					// myObjectSaveDidNotSucceed();
				}
			}
		});		
	}

	public void reloadData(String jsonData) {
		try {
			JSONObject jobject = new JSONObject(jsonData);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("HighScore");
			query = query.whereGreaterThan("finishTime", 0);
			query = query.addAscendingOrder("finishTime");
			query = query.setLimit(10);
			final int problems = getJsonInt(jobject, "problems");
			if (problems != -1) {
				query = query.whereEqualTo("problems", problems);
			}
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						android.util.Log.d("Tiendv parse", "objectsWereRetrievedSuccessfully");

						int rank = 1;
						for (ParseObject p : objects) {
							android.util.Log.d("Tiendv parse", " " +  p.getString("name") + " : " + p.getDouble("finishTime"));
							EventQueue.pushEvent(new ParseEvent(rank, problems, p.getString("name"), p.getDouble("finishTime")));
							rank++;
						}
					} else {
						android.util.Log.d("Tiendv parse", "objectRetrievalFailed");
						e.printStackTrace();
					}
				}
			});
		} 	catch (JSONException e) {
			e.printStackTrace();
		}
	}

}