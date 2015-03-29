package com.tingken.infoshower.view;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.tingken.infoshower.R;
import com.tingken.infoshower.core.LocalService;
import com.tingken.infoshower.core.LocalServiceFactory;
import com.tingken.infoshower.outside.ShowService;
import com.tingken.infoshower.util.ScreenCaptureHelper;

public class LoginInvalidActivity extends Activity {

	Button btnRetry;
	Button btnExit;
	private StringBuffer commandBuffer = new StringBuffer();
	private LocalService localService = LocalServiceFactory.getSystemLocalService();

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// handle the key from controller
		if (KeyEvent.ACTION_UP == event.getAction() && 0 == event.getRepeatCount()) {
			if (KeyEvent.KEYCODE_0 <= keyCode && KeyEvent.KEYCODE_9 >= keyCode) {
				commandBuffer.append(keyCode - KeyEvent.KEYCODE_0);
			} else if (KeyEvent.KEYCODE_NUMPAD_0 <= keyCode && KeyEvent.KEYCODE_NUMPAD_9 >= keyCode) {
				commandBuffer.append(keyCode - KeyEvent.KEYCODE_NUMPAD_0);
			}
			switch (keyCode) {
			case KeyEvent.KEYCODE_TAB:
				if (commandBuffer.length() > 0) {
					if (LocalService.SET_SERVER_ADDRESS_COMMAND.equals(commandBuffer.toString())) {
						// open dialog to configure
						openServerAddressConfig();
					}
					commandBuffer = new StringBuffer();
				}
				return true;
			case KeyEvent.KEYCODE_DEL:
				commandBuffer = new StringBuffer();
				break;
			default:
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void openServerAddressConfig() {
		// create view and PopupWindow
		LayoutInflater inflater = LayoutInflater.from(LoginInvalidActivity.this);
		final View view = inflater.inflate(R.layout.config_server_address, null);
		final PopupWindow configServerAddressDialog = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
		        LayoutParams.WRAP_CONTENT, true);

		Button btnConfig = (Button) view.findViewById(R.id.btn_config);
		final EditText editServerAddress = (EditText) view.findViewById(R.id.edit_server_address);
		String serviceAddress = localService.getShowServiceAddress();
		editServerAddress.setText(serviceAddress != null ? serviceAddress : ShowService.DEFAULT_SERVER_ADDRESS);
		btnConfig.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// configure server address
				localService.saveShowServiceAddress(editServerAddress.getText().toString());
				configServerAddressDialog.dismiss();
			}
		});

		Button btnCancel = (Button) view.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// exit dialog
				configServerAddressDialog.dismiss();
			}
		});
		configServerAddressDialog.showAtLocation(inflater.inflate(R.layout.activity_login_invalid, null),
		        Gravity.CENTER, 0, 0);
	}
}
