package com.tingken.infoshower.view;

import com.tingken.infoshower.R;
import com.tingken.infoshower.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class LoginSucessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_sucess);
	}

	@Override
	protected void onStart() {
		// delay to switch to Main Page
		super.onStart();
	}
}
