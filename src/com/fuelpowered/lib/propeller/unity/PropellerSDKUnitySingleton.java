package com.fuelpowered.lib.propeller.unity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.fuelpowered.lib.propeller.PropellerSDK;
import com.fuelpowered.lib.propeller.PropellerSDKNotificationType;
import com.fuelpowered.lib.propeller.gcm.PropellerSDKGCM;
import com.unity3d.player.UnityPlayer;

// helper class. manages communication between unity and java.
public final class PropellerSDKUnitySingleton {

	/**
	 * Data type enumeration.
	 */
	private static enum DataType {

		INTEGER("0"),
		LONG("1"),
		FLOAT("2"),
		DOUBLE("3"),
		BOOLEAN("4"),
		STRING("5");

		/**
		 * Mapping of values to their enumeration.
		 */
		private static Map<String, DataType> mValueEnumMap;

		/**
		 * Static initializer.
		 */
		static {
			mValueEnumMap = new HashMap<String, DataType>();

			for (DataType dataType : DataType.values()) {
				mValueEnumMap.put(dataType.mValue, dataType);
			}
		}

		/**
		 * Enumeration value.
		 */
		private String mValue;

		/***********************************************************************
		 * Constructor.
		 * 
		 * @param value Value to bind to the enumeration.
		 */
		private DataType(String value) {
			mValue = value;
		}

		/***********************************************************************
		 * Retrieves the enumeration that matches the given value.
		 * 
		 * @param value Value to retrieve the matching enumeration for.
		 * @return The matching enumeration to the given value, null if there is
		 *         no match.
		 */
		public static DataType findByValue(String value) {
			return mValueEnumMap.get(value);
		}

	}

	private static final String kLogTag = "PropellerUnitySingleton";

	private static PropellerUnityListener m_propellerUnityListener;

	// Functions called from Unity, into Java
	public static void Initialize(String gameID, String gameSecret, String screenOrientation, boolean hasLogin, boolean hasInvite, boolean hasShare) {
		Log.i(kLogTag, "Initialize Start");

		m_propellerUnityListener = new PropellerUnityListener();

		PropellerSDK.setNotificationListener(new PropellerUnityNotificationListener());

		Log.i(kLogTag, "initialize");
		PropellerSDK.initialize(gameID, gameSecret, hasLogin, hasInvite, hasShare);

		Log.i(kLogTag, "Setting screen orientation: " + screenOrientation);

		if (screenOrientation.equals("auto")) {
			PropellerSDK.instance().setOrientation("currentAuto");
		} else {
			PropellerSDK.instance().setOrientation(screenOrientation + "Auto");
		}
	}

	public static void InitializeDynamics(String gameID, String gameSecret) {
		Log.i(kLogTag, "InitializeDynamics Start");

		Log.i(kLogTag, "initializeDynamics");
		PropellerSDK.initializeDynamics(gameID, gameSecret);
	}

	public static void OnPause() {
		Log.i(kLogTag, "OnPause");
	}

	public static void OnResume() {
		Log.i(kLogTag, "OnResume");
	}

	public static void OnQuit() {
		Log.i(kLogTag, "OnQuit");
	}

	public static void SetLanguageCode(String languageCode) {
		Log.i(kLogTag, "Setting language code: " + languageCode);
		PropellerSDK.instance().setLanguageCode(languageCode);
	}

	public static boolean SetNotificationIcon(String notificationIcon) {
		Log.i(kLogTag, "Setting notification icon: " + notificationIcon);
		return PropellerSDK.instance().setNotificationIcon(notificationIcon);
	}

	public static boolean Launch() {
		Log.i(kLogTag, "Launch");
		return PropellerSDK.instance().launch(m_propellerUnityListener);
	}

	public static boolean SubmitMatchResult(String matchResultJSONString) {
		Log.i(kLogTag, "SubmitMatchResult");

		JSONObject matchResultJSON = null;

		try {
			matchResultJSON = new JSONObject(matchResultJSONString);
		} catch (JSONException jsonException) {
			return false;
		}

		Object matchResultObject = normalizeJSONObject(matchResultJSON);

		if (matchResultObject == null) {
			return false;
		}

		if (!(matchResultObject instanceof Map)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> matchResult = (Map<String, Object>) matchResultObject;

		return PropellerSDK.instance().submitMatchResult(matchResult);
	}

	public static void SyncChallengeCounts() {
		Log.i(kLogTag, "SyncChallengeCounts");
		PropellerSDK.instance().syncChallengeCounts();
	}

	public static void SyncTournamentInfo() {
		Log.i(kLogTag, "SyncTournamentInfo");
		PropellerSDK.instance().syncTournamentInfo();
	}

	public static void SyncVirtualGoods() {
		Log.i(kLogTag, "SyncVirtualGoods");
		PropellerSDK.instance().syncVirtualGoods();
	}

	public static void AcknowledgeVirtualGoods(String transactionId, boolean consumed) {
		Log.i(kLogTag, "AcknowledgeVirtualGoods");
		PropellerSDK.instance().acknowledgeVirtualGoods(transactionId, consumed);
	}

	public static boolean EnableNotification(PropellerSDKNotificationType notificationType) {
		Log.i(kLogTag, "EnableNotification");
		return PropellerSDK.enableNotification(notificationType);
	}

	public static boolean DisableNotification(PropellerSDKNotificationType notificationType) {
		Log.i(kLogTag, "DisableNotification");
		return PropellerSDK.disableNotification(notificationType);
	}

	public static boolean IsNotificationEnabled(PropellerSDKNotificationType notificationType) {
		Log.i(kLogTag, "IsNotificationEnabled");
		return PropellerSDK.isNotificationEnabled(UnityPlayer.currentActivity, notificationType);
	}

	public static void InitializeGCM(String googleProjectID) {
		PropellerSDKGCM.onCreate(UnityPlayer.currentActivity, googleProjectID);
	}

	public static void SdkSocialLoginCompleted(Map<String, Object> loginInfo) {
		// This is a hack for indicating that social login completed with a failure
		// since Unity won't allow passing of a 'null' argument through the JNI
		if (loginInfo.isEmpty()) {
			loginInfo = null;
		}

		PropellerSDK.instance().sdkSocialLoginCompleted(loginInfo);
	}

	public static void SdkSocialInviteCompleted() {
		PropellerSDK.instance().sdkSocialInviteCompleted();
	}

	public static void SdkSocialShareCompleted() {
		PropellerSDK.instance().sdkSocialShareCompleted();
	}

	public static boolean SetUserConditions(String conditionsJSONString) {
		Log.i(kLogTag, "SetUserConditions");

		JSONObject conditionsJSON = null;

		try {
			conditionsJSON = new JSONObject(conditionsJSONString);
		} catch (JSONException jsonException) {
			return false;
		}

		Object conditionsObject = normalizeJSONObject(conditionsJSON);

		if (conditionsObject == null) {
			return false;
		}

		if (!(conditionsObject instanceof Map)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> conditions = (Map<String, Object>) conditionsObject;

		return PropellerSDK.instance().setUserConditions(conditions);
	}

	public static boolean SyncUserValues() {
		Log.i(kLogTag, "SyncUserValues");
		return PropellerSDK.instance().syncUserValues();
	}

	/***************************************************************************
	 * Normalizes a JSON object into its deserialized form. Used to deserialize
	 * JSON which includes value meta-data in order to preserve it's type. Does
	 * not suffer the type conversion issues between ints versus longs, and
	 * floats versus doubles since the internal representation of primitives are
	 * strings.
	 * 
	 * @param json JSON object to normalize.
	 * @return The normalized JSON object, null otherwise.
	 */
	private static Object normalizeJSONObject(JSONObject json) {
		if (json == null) {
			return null;
		}

		if (isNormalizedJSONValue(json)) {
			return normalizeJSONValue(json);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();

		Iterator<?> iterator = json.keys();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();

			if (key == null) {
				continue;
			}

			Object value = json.opt(key);

			if (value == null) {
				continue;
			}

			Object normalizedValue = null;

			if (value instanceof JSONArray) {
				normalizedValue = normalizeJSONArray((JSONArray) value);
			} else if (value instanceof JSONObject) {
				normalizedValue = normalizeJSONObject((JSONObject) value);
			} else {
				continue;
			}

			if (normalizedValue == null) {
				continue;
			}

			resultMap.put(key, normalizedValue);
		}

		return resultMap;
	}

	/***************************************************************************
	 * Normalizes a JSON array into its deserialized form. Used to deserialize
	 * JSON which includes value meta-data in order to preserve it's type. Does
	 * not suffer the type conversion issues between ints versus longs, and
	 * floats versus doubles since the internal representation of primitives are
	 * strings.
	 * 
	 * @param json JSON array to normalize.
	 * @return The normalized JSON array, null otherwise.
	 */
	private static Object normalizeJSONArray(JSONArray json) {
		if (json == null) {
			return null;
		}

		List<Object> resultList = new ArrayList<Object>();

		int count = json.length();

		for (int index = 0; index < count; index++) {
			Object value = json.opt(index);

			if (value == null) {
				continue;
			}

			Object normalizedValue = null;

			if (value instanceof JSONArray) {
				normalizedValue = normalizeJSONArray((JSONArray) value);
			} else if (value instanceof JSONObject) {
				normalizedValue = normalizeJSONObject((JSONObject) value);
			} else {
				continue;
			}

			if (normalizedValue == null) {
				continue;
			}

			resultList.add(normalizedValue);
		}

		return resultList;
	}

	/***************************************************************************
	 * Normalizes a JSON value into its deserialized form. Used to deserialize
	 * JSON which includes value meta-data in order to preserve it's type. Does
	 * not suffer the type conversion issues between ints versus longs, and
	 * floats versus doubles since the internal representation of primitives are
	 * strings.
	 * 
	 * @param json JSON value to normalize.
	 * @return The normalized JSON value, null otherwise.
	 */
	private static Object normalizeJSONValue(JSONObject json) {
		if (json == null) {
			return null;
		}

		String type = (String) json.opt("type");
		String value = (String) json.opt("value");

		if ((type == null) || (value == null)) {
			return null;
		}

		DataType dataType = DataType.findByValue(type);

		if (dataType == null) {
			return null;
		}

		switch (dataType) {
			case INTEGER:
				try {
					return Integer.parseInt(value);
				} catch (NumberFormatException numberFormatException) {
					return null;
				}
			case LONG:
				try {
					return Long.parseLong(value);
				} catch (NumberFormatException numberFormatException) {
					return null;
				}
			case FLOAT:
				try {
					return Float.parseFloat(value);
				} catch (NumberFormatException numberFormatException) {
					return null;
				}
			case DOUBLE:
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException numberFormatException) {
					return null;
				}
			case BOOLEAN:
				return Boolean.parseBoolean(value);
			case STRING:
				return value;
			default:
				return null;
		}
	}

	/***************************************************************************
	 * Validates whether or not the given JSON object is a serialized JSON
	 * value.
	 * 
	 * @param json JSON object to validate.
	 * @return True if the given JSON object is a serialized JSON value, false
	 *         otherwise.
	 */
	private static boolean isNormalizedJSONValue(JSONObject json) {
		if (json == null) {
			return false;
		}

		Object checksumObject = json.opt("checksum");

		if (!(checksumObject instanceof String)) {
			return false;
		}

		String checksum = (String) checksumObject;

		if (!checksum.equals("faddface")) {
			return false;
		}

		Object typeObject = json.opt("type");

		if (!(typeObject instanceof String)) {
			return false;
		}

		String type = (String) json.opt("type");

		if (DataType.findByValue(type) == null) {
			return false;
		}

		Object valueObject = json.opt("value");

		if (!(valueObject instanceof String)) {
			return false;
		}

		return true;
	}

}
