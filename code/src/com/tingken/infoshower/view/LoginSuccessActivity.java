package com.tingken.infoshower.view;

import com.tingken.infoshower.R;
import com.tingken.infoshower.R.layout;
import com.tingken.infoshower.core.LocalService;
import com.tingken.infoshower.core.LocalServiceFactory;
import com.tingken.infoshower.view.LoginActivity.UserLoginTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class LoginSuccessActivity extends Activity {

	private static final int AUTO_DELAY_MILLIS = 3000;

	private LocalService dataSource = LocalServiceFactory.getSystemLocalService();
	private Handler mSwitchHandler = new Handler();
	private Runnable mSwitchPageRunnable = new Runnable() {

		@Override
		public void run() {
			// go to main Page
			messageHandler.sendEmptyMessage(0);
		}

	};
	private Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			LoginSuccessActivity.this.finish();
			Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("content_page_address", dataSource.getCachedServerAddress());
			startActivity(intent);

			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_success);
	}

	@Override
	protected void onStart() {
		// delay to switch to Main Page
		mSwitchHandler.removeCallbacks(mSwitchPageRunnable);
		mSwitchHandler.postDelayed(mSwitchPageRunnable, AUTO_DELAY_MILLIS);
		super.onStart();
	}

}
