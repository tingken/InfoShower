package com.tingken.infoshower.view;

import android.app.Activity;
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
				// go to Login page
				Intent intent = new Intent(LoginInvalidActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		btnExit = (Button) findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// exit
				System.exit(0);
			}
		});
	}
}
