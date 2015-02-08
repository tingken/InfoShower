package com.tingken.infoshower.view;

import java.util.Timer;
import java.util.TimerTask;

import com.tingken.infoshower.R;
import com.tingken.infoshower.R.layout;
import com.tingken.infoshower.core.DataSource;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.PopupWindow;

public class MainActivity extends Activity {

	private WebView webContent;
	private Timer serverListener;
	private ShowService showService;
	private DataSource dataSource;
	private boolean connectionNoticeOpened;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webContent = (WebView) findViewById(R.id.webView1);
		webContent.getSettings().setSupportZoom(true);
		webContent.getSettings().setJavaScriptEnabled(true);
		webContent.getSettings().setBuiltInZoomControls(true);

		String contentPageAddress = getIntent().getStringExtra(
				"content_page_address");
		if (contentPageAddress != null) {
			webContent.loadUrl(contentPageAddress);
		}
	}

	@Override
	protected void onStart() {
		// start timer to monitor server commands
		if (serverListener == null) {
			// Log.e(TAG, "定时更新数据");
			serverListener = new Timer(true);
			// 每隔一段时间更新UI
			serverListener.schedule(new TimerTask() {

				@Override
				public void run() {
					// Get Server Command
					// If connection failed, popup connection notice
					ServerCommand command = showService.heartBeat(dataSource
							.getAuthCode());
					boolean networkAccessable = true;
					switch (command) {
					case SCREEN_CAPTURE:
						// capture
						break;
					case RESTART:
						// notice restart and prepare to do
						break;
					case CONNECTION_FAILED:
						// notice connection failed and save status
						networkAccessable = false;
						openConnectionNote();
						connectionNoticeOpened = true;
						break;
					}
					if (networkAccessable && connectionNoticeOpened) {
						// close
					}
				}

			}, 0, 1 * 60 * 1000);
		}

		super.onStart();
	}

	protected void openConnectionNote() {

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.connection_failed_notice, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view);
		pop.showAtLocation(inflater.inflate(R.layout.activity_main, null), 0,
				0, 0);

	}

	@Override
	protected void onStop() {
		// end timer to stop monitoring
		if (serverListener != null) {
			serverListener.cancel();
			serverListener = null;
		}
		super.onStop();
	}
}
