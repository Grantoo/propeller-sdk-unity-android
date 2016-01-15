package com.fuelpowered.lib.propeller.unity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import android.text.TextUtils;
import android.util.Log;
import com.fuelpowered.lib.propeller.JSONHelper;
import com.fuelpowered.lib.propeller.PropellerSDKListener;
import com.unity3d.player.UnityPlayer;

public class PropellerUnityListener extends PropellerSDKListener {

	private static final String kLogTag = "PropellerUnityListener";

	private String m_tournamentID;

	private String m_matchID;

	public String GetTournamentID() {
		return m_tournamentID;
	}

	public String GetMatchID() {
		return m_matchID;
	}

	@Override
	public void sdkCompletedWithExit() {
		Log.d(kLogTag, "sdkCompletedWithExit");
		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkCompletedWithExit", "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sdkCompletedWithMatch(Map<String, Object> data) {
		List<String> paramList = new ArrayList<String>();

		m_tournamentID = (String) data.get("tournamentID");

		if (!TextUtils.isEmpty(m_tournamentID)) {
			paramList.add(m_tournamentID);
		}

		m_matchID = (String) data.get("matchID");

		if (!TextUtils.isEmpty(m_matchID)) {
			paramList.add(m_matchID);
		}

		String paramsJSON = null;
		String paramsJSONUrlEncoded = null;

		try {
			Map<String, Object> params = (Map<String, Object>) data.get("params");
			JSONObject jsonObject = JSONHelper.toJSONObject(params);

			if (jsonObject != null) {
				paramsJSON = jsonObject.toString();
				paramsJSONUrlEncoded = URLEncoder.encode(paramsJSON, "UTF-8");

				if (!TextUtils.isEmpty(paramsJSONUrlEncoded)) {
					paramList.add(paramsJSONUrlEncoded);
				}
			}
		} catch (Exception exception) {
			Log.w(kLogTag, exception);
		}

		Log.d(kLogTag, "sdkCompletedWithMatch - " + (m_tournamentID == null ? "" : m_tournamentID) + " - " + (m_matchID == null ? "" : m_matchID) + " - " + (paramsJSON == null ? "" : paramsJSON));

		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkCompletedWithMatch", TextUtils.join("&", paramList));
	}

	@Override
	public void sdkFailed(String message, Map<String, Object> data) {
		String failReason = "";

		if (!TextUtils.isEmpty(message)) {
			failReason += " - " + message;
		}

		Log.d(kLogTag, "sdkFailed" + failReason);
		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkFailed", failReason);
	}

	@Override
	public boolean sdkSocialLogin(boolean allowCache) {
		Log.d(kLogTag, "sdkSocialLogin");

		String resultString = allowCache ? "true" : "false";
		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkSocialLogin", resultString);

		return true;
	}

	@Override
	public boolean sdkSocialInvite(Map<String, Object> inviteDetail) {
		try
		{
			StringBuilder stringBuilder = new StringBuilder();

			boolean first = true;

			for (String key : inviteDetail.keySet()) {
				if (key == null) {
					continue;
				}

				if (first) {
					first = false;
				} else {
					stringBuilder.append('&');
				}

				String value = (String) inviteDetail.get(key);

				if (value == null) {
					continue;
				}

				stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
				stringBuilder.append('=');
				stringBuilder.append(URLEncoder.encode(value, "UTF-8"));
			}

			UnityPlayer.UnitySendMessage(
				"PropellerSDK",
				"PropellerOnSdkSocialInvite",
				stringBuilder.toString());

			Log.d(kLogTag, "sdkSocialInvite - " + stringBuilder.toString());

			return true;
		} catch (Exception exception) {
			Log.w(kLogTag, exception);
			return false;
		}
	}

	@Override
	public boolean sdkSocialShare(Map<String, Object> shareDetail) {
		try
		{
			StringBuilder stringBuilder = new StringBuilder();

			boolean first = true;

			for (String key : shareDetail.keySet()) {
				if (key == null) {
					continue;
				}

				if (first) {
					first = false;
				} else {
					stringBuilder.append('&');
				}

				String value = (String) shareDetail.get(key);

				if (value == null) {
					continue;
				}

				stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
				stringBuilder.append('=');
				stringBuilder.append(URLEncoder.encode(value, "UTF-8"));
			}

			UnityPlayer.UnitySendMessage(
				"PropellerSDK",
				"PropellerOnSdkSocialShare",
				stringBuilder.toString());

			Log.d(kLogTag, "sdkSocialShare - " + stringBuilder.toString());

			return true;
		} catch (Exception exception) {
			Log.w(kLogTag, exception);
			return false;
		}
	}

}
