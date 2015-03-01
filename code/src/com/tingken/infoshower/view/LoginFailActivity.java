package com.tingken.infoshower.view;

import com.tingken.infoshower.R;
import com.tingken.infoshower.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class LoginFailActivity extends Activity {

	private static final int AUTO_DELAY_MILLIS = 3000;

	private Handler mLoginHandler = new Handler();
	private Runnable mLoginRunnable = new Runnable() {

		@Override
		public void run() {
			// go to Login Page
			Intent intent = new Intent(LoginFailActivity.this, LoginActivity.class);
			// whether add old regNum
			startActivity(intent);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_fail);
	}

	@Override
	protected void onStart() {
		// delay to switch to Main Page
		mLoginHandler.removeCallbacks(mLoginRunnable);
		mLoginHandler.postDelayed(mLoginRunnable, AUTO_DELAY_MILLIS);
		super.onStart();
	}
}
