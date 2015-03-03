package com.tingken.infoshower.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tingken.infoshower.R;

public class LoginInvalidActivity extends Activity {

	Button btnRetry;
	Button btnExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_invalid);
		btnRetry = (Button) findViewById(R.id.btnRetry);
		btnRetry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// back to Login page
				// Intent intent = new Intent(LoginInvalidActivity.this,
				// LoginActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				LoginInvalidActivity.this.finish();
			}
		});
		btnExit = (Button) findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// exit
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				System.exit(0);
			}
		});
	}
}
