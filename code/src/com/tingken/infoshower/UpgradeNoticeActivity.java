package com.tingken.infoshower;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressLint("Instantiatable")
public class UpgradeNoticeActivity extends Dialog {

	public UpgradeNoticeActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade_notice);
	}
}
