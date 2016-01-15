package com.fuelpowered.lib.propeller.unity;

import android.content.Context;
import android.util.Log;
import com.fuelpowered.lib.propeller.PropellerSDKNotificationListener;
import com.fuelpowered.lib.propeller.PropellerSDKNotificationType;
import com.unity3d.player.UnityPlayer;

public class PropellerUnityNotificationListener implements PropellerSDKNotificationListener {

	private static final String kLogTag = "PropellerUnityNotificationListener";

	@Override
	public void sdkOnNotificationEnabled(Context context, PropellerSDKNotificationType notificationType) {
		Log.d(kLogTag, "sdkOnNotificationEnabled");
		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkOnNotificationEnabled", Integer.toString(notificationType.value()));
	}

	@Override
	public void sdkOnNotificationDisabled(Context context, PropellerSDKNotificationType notificationType) {
		Log.d(kLogTag, "sdkOnNotificationDisabled");
		UnityPlayer.UnitySendMessage("PropellerSDK", "PropellerOnSdkOnNotificationDisabled", Integer.toString(notificationType.value()));
	}

}
