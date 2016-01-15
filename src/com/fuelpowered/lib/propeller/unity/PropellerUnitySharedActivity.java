package com.fuelpowered.lib.propeller.unity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.fuelpowered.lib.propeller.PropellerSDK;
import com.fuelpowered.lib.propeller.PropellerSDKBroadcastReceiver;
import com.unity3d.player.UnityPlayer;

public class PropellerUnitySharedActivity {

	private static final String LOG_TAG = "PropellerUnitySharedActivity";

	private static Activity sActivity;

	private static BroadcastReceiver sBroadcastReceiver;

	private static IntentFilter sIntentFilter;

	public static void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate()");

		sActivity = UnityPlayer.currentActivity;

		PropellerSDK.onCreate(sActivity);

		sBroadcastReceiver = getBroadcastReceiver();

		sIntentFilter = new IntentFilter();
		sIntentFilter.addAction("PropellerSDKChallengeCountChanged");
		sIntentFilter.addAction("PropellerSDKTournamentInfo");
		sIntentFilter.addAction("PropellerSDKVirtualGoodList");
		sIntentFilter.addAction("PropellerSDKVirtualGoodRollback");
		sIntentFilter.addAction("PropellerSDKUserValues");
		sIntentFilter.addAction("PropellerSDKImplicitLaunch");
	}

	public static void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "onActivityResult()");

		PropellerSDK.onActivityResult(sActivity, requestCode, resultCode, data);
	}

	public static void onResume() {
		Log.d(LOG_TAG, "onResume()");

		LocalBroadcastManager.getInstance(sActivity).registerReceiver(
			sBroadcastReceiver,
			sIntentFilter);

		PropellerSDK.onResume(sActivity);
	}

	public static void onPause() {
		Log.d(LOG_TAG, "onPause()");

		LocalBroadcastManager.getInstance(sActivity).unregisterReceiver(sBroadcastReceiver);

		PropellerSDK.onPause(sActivity);
	}

	private static BroadcastReceiver getBroadcastReceiver() {
		return new PropellerSDKBroadcastReceiver() {

			@SuppressWarnings("unchecked")
			@Override
			public void onReceive(Context context, String action, Map<String, Object> data) {
				if (action.equals("PropellerSDKChallengeCountChanged")) {
					String message = null;

					if (data == null) {
						message = "";
					} else {
						String count = "";
						Object countObject = data.get("count");

						if ((countObject != null) &&
							(countObject instanceof Integer)) {
							count = ((Integer) countObject).toString();
						}

						message = count;
					}

					UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnChallengeCountChanged", message);
				} else if (action.equals("PropellerSDKTournamentInfo")) {
					String message = null;

					if (data == null) {
						message = "";
					} else {
						List<String> paramList = new ArrayList<String>();

						String name = "";

						try {
							Object nameObject = data.get("name");

							if ((nameObject != null) &&
								(nameObject instanceof String)) {
								name = URLEncoder.encode(
									(String) nameObject,
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(name);

						String campaignName = "";

						try {
							Object campaignNameObject = data.get("campaignName");

							if ((campaignNameObject != null) &&
								(campaignNameObject instanceof String)) {
								campaignName = URLEncoder.encode(
									(String) campaignNameObject,
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(campaignName);

						String sponsorName = "";

						try {
							Object sponsorNameObject = data.get("sponsorName");

							if ((sponsorNameObject != null) &&
								(sponsorNameObject instanceof String)) {
								sponsorName = URLEncoder.encode(
									(String) sponsorNameObject,
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(sponsorName);

						String startDate = "";

						try {
							Object startDateObject = data.get("startDate");

							if ((startDateObject != null) &&
								(startDateObject instanceof Long)) {
								startDate = URLEncoder.encode(
									((Long) startDateObject).toString(),
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(startDate);

						String endDate = "";

						try {
							Object endDateObject = data.get("endDate");

							if ((endDateObject != null) &&
								(endDateObject instanceof Long)) {
								endDate = URLEncoder.encode(
									((Long) endDateObject).toString(),
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(endDate);

						String logo = "";

						try {
							Object logoObject = data.get("logo");

							if ((logoObject != null) &&
								(logoObject instanceof String)) {
								logo = URLEncoder.encode(
									(String) logoObject,
									"UTF-8");
							}
						} catch (UnsupportedEncodingException unsupportedEncodingException) {
							Log.w(LOG_TAG, unsupportedEncodingException);
						}

						paramList.add(logo);

						message = TextUtils.join("&", paramList);
					}

					UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnTournamentInfo", message);
				} else if (action.equals("PropellerSDKVirtualGoodList")) {
					String message = null;

					if (data == null) {
						message = "";
					} else {
						List<String> paramList = new ArrayList<String>();

						String transactionId = "";
						Object transactionIdObject = data.get("transactionID");

						if ((transactionIdObject != null) &&
							(transactionIdObject instanceof String)) {
							transactionId = (String) transactionIdObject;
						}

						paramList.add(transactionId);

						Object virtualGoodsObject = data.get("virtualGoods");

						if ((virtualGoodsObject != null) &&
							(virtualGoodsObject instanceof List)) {
							List<Object> virtualGoods = (List<Object>) virtualGoodsObject;

							for (Object virtualGoodObject : virtualGoods) {
								if ((virtualGoodObject == null) ||
									!(virtualGoodObject instanceof Map)) {
									continue;
								}

								Map<String, Object> virtualGood = (Map<String, Object>) virtualGoodObject;
								Object virtualGoodIdObject = virtualGood.get("goodId");

								if ((virtualGoodIdObject == null) ||
									!(virtualGoodIdObject instanceof String)) {
									continue;
								}

								paramList.add((String) virtualGoodIdObject);
							}
						}

						message = TextUtils.join("&", paramList);
					}

					UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnVirtualGoodList", message);
				} else if (action.equals("PropellerSDKVirtualGoodRollback")) {
					String message = null;

					if (data == null) {
						message = "";
					} else {
						String transactionId = "";
						Object transactionIdObject = data.get("transactionID");

						if ((transactionIdObject != null) &&
							(transactionIdObject instanceof String)) {
							transactionId = (String) transactionIdObject;
						}

						message = transactionId;
					}

					UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnVirtualGoodRollback", message);
				} else if (action.equals("PropellerSDKUserValues")) {
					String message = null;

					if (data == null) {
						message = "";
					} else {
						List<String> paramList = new ArrayList<String>();

						Map<String, Object> variablesMap =
							(Map<String, Object>) data.get("variables");

						if (variablesMap != null) {
							for (String key : variablesMap.keySet()) {
								if (key == null) {
									continue;
								}

								Object valueObject = variablesMap.get(key);

								if ((valueObject == null) ||
									!(valueObject instanceof String)) {
									continue;
								}

								String value = (String) valueObject;

								paramList.add(key);
								paramList.add(value);
							}
						}

						Map<String, Object> dynamicConditionsMap =
							(Map<String, Object>) data.get("dynamicConditions");

						if (dynamicConditionsMap != null) {
							for (String key : dynamicConditionsMap.keySet()) {
								if (key == null) {
									continue;
								}

								Object valueObject = dynamicConditionsMap.get(key);

								if ((valueObject == null) ||
									!(valueObject instanceof String)) {
									continue;
								}

								String value = (String) valueObject;

								paramList.add(key);
								paramList.add(value);
							}
						}

						message = TextUtils.join("&", paramList);
					}

					UnityPlayer.UnitySendMessage("PropellerCommon", "PropellerOnUserValues", message);
				} else if (action.equals("PropellerSDKImplicitLaunch")) {
					String message = null;

					if (data != null) {
						message = (String) data.get("applicationState");
					}

					if (message == null) {
						message = "";
					}

					UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnImplicitLaunch", message);
				}
			}

		};
	}

}
