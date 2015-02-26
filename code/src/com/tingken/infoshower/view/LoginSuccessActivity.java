package com.tingken.infoshower.view;

import com.tingken.infoshower.R;
import com.tingken.infoshower.R.layout;
import com.tingken.infoshower.core.DataSource;
import com.tingken.infoshower.view.LoginActivity.UserLoginTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class LoginSuccessActivity extends Activity {
	
	private static final int AUTO_DELAY_MILLIS = 3000;
	
	private DataSource dataSource;
	private Handler mHideHandler = new Handler();
	private Runnable mHideRunnable = new Runnable() {

		@Override
		public void run() {
			// go to main Page
			Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
			intent.putExtra("content_page_address", dataSource.getCachedServerAddress());
			startActivity(intent);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_suceess);
	}

	@Override
	protected void onStart() {
		// delay to switch to Main Page
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, AUTO_DELAY_MILLIS);
		super.onStart();
	}

}
