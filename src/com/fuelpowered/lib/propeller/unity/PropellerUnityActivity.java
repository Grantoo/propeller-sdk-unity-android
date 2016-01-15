package com.fuelpowered.lib.propeller.unity;

import android.content.Intent;
import android.os.Bundle;
import com.unity3d.player.UnityPlayerActivity;

// Activity that manages communication between the Propeller activity, and Unity.
public class PropellerUnityActivity extends UnityPlayerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PropellerUnitySharedActivity.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		PropellerUnitySharedActivity.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		PropellerUnitySharedActivity.onResume();
	}

	@Override
	protected void onPause() {
		PropellerUnitySharedActivity.onPause();
		super.onPause();
	}

}
