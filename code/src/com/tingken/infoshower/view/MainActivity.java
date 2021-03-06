package com.tingken.infoshower.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.tingken.infoshower.R;
import com.tingken.infoshower.UpgradeNoticeActivity;
import com.tingken.infoshower.R.layout;
import com.tingken.infoshower.core.LocalService;
import com.tingken.infoshower.core.LocalServiceFactory;
import com.tingken.infoshower.outside.ServerCommand;
import com.tingken.infoshower.outside.ShowService;
import com.tingken.infoshower.outside.ShowServiceFactory;
import com.tingken.infoshower.outside.VersionInfo;
import com.tingken.infoshower.outside.rest.HttpServiceWorker;
import com.tingken.infoshower.util.ScreenCaptureHelper;
import com.tingken.infoshower.util.UpgradeHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String APP_CACAHE_DIRNAME = "/webcache";

	private WebView webContent;
	private Timer serverListener;
	private ShowService showService = ShowServiceFactory.getSystemShowService();
	private LocalService localService = LocalServiceFactory.getSystemLocalService();
	private boolean connectionNoticeOpened;
	private PopupWindow connectionFailedNotice;
	private PopupWindow popMenu;
	private StringBuffer commandBuffer = new StringBuffer();

	enum MenuFocus {
		NONE, CHANGE_REG_NUM, CHANGE_AUTO_START, EXIT
	}

	MenuFocus focus = MenuFocus.NONE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webContent = (WebView) findViewById(R.id.webView1);
		webContent.setWebViewClient(new WebViewClient());
		webContent.getSettings().setSupportZoom(true);
		webContent.getSettings().setJavaScriptEnabled(true);
		webContent.getSettings().setBuiltInZoomControls(true);
		webContent.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				onKeyDown(keyCode, event);
				int disabledKey[] = { KeyEvent.KEYCODE_TAB, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN,
				        KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT };
				for (int key : disabledKey) {
					if (key == keyCode) {
						return true;
					}
				}
				return false;
			}

		});

		String contentPageAddress = getIntent().getStringExtra("content_page_address");
//		// 开启 DOM storage API 功能
//		webContent.getSettings().setDomStorageEnabled(true);
//		// 开启 database storage API 功能
//		webContent.getSettings().setDatabaseEnabled(true);
//		String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//		Log.i(TAG, "cacheDirPath=" + cacheDirPath);
//		// 设置数据库缓存路径
//		webContent.getSettings().setDatabasePath(cacheDirPath);
//		// 设置 Application Caches 缓存目录
//		webContent.getSettings().setAppCachePath(cacheDirPath);
		webContent.getSettings().setAppCacheEnabled(true);
		webContent.getSettings().setCacheMode(getIntent().getIntExtra("web_cache_setting", WebSettings.LOAD_DEFAULT));
//		HttpServiceWorker httpWorker = new HttpServiceWorker();
//		try {
//			httpWorker.executeGetStream(contentPageAddress);
//		} catch (Exception e) {
//			webContent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//		} finally {
//			httpWorker.close();
//		}
		if (contentPageAddress != null) {
			webContent.loadUrl(contentPageAddress);
		}
	}

	private class HeartBeatTask extends TimerTask {

		@Override
		public void run() {
			// Get Server Command
			// If connection failed, popup connection notice
			ServerCommand command = showService.heartBeat(localService.getLoginId());
			boolean networkAccessable = true;
			switch (command) {
			case SCREEN_CAPTURE:
				// capture
				showServiceHandler.sendEmptyMessage(0);
				if (serverListener != null) {
					serverListener.schedule(new HeartBeatTask(), 60000);
				}
				break;
			case RESTART:
				// notice restart and prepare to do
				showServiceHandler.sendEmptyMessage(4);
				break;
			case CONNECTION_FAILED:
				// notice connection failed and save status
				networkAccessable = false;
				showServiceHandler.sendEmptyMessage(2);
				// serverListener.schedule(new HeartBeatTask(), 60000);
				break;
			case UPGRADE:
				VersionInfo version = showService.getLatestVersion(localService.getLoginId());
				if (version != null && upgradeHelper.isUpdate(version.getVersionCode())) {
					Message upgradeMsg = new Message();
					upgradeMsg.what = 1;
					upgradeMsg.obj = version;
					showServiceHandler.sendMessage(upgradeMsg);
				}
				break;
			default:
				if (serverListener != null) {
					serverListener.schedule(new HeartBeatTask(), 60000);
				}
				break;
			}
			if (networkAccessable && connectionNoticeOpened) {
				// close
				showServiceHandler.sendEmptyMessage(3);
			}
		}

	};

	@Override
	protected void onStart() {
		// start timer to monitor server commands
		if (serverListener == null) {
			// Log.e(TAG, "定时更新数据");
			serverListener = new Timer(true);
			// 每隔一段时间更新UI
			// serverListener.schedule(heartBeatTask, 5000, 1 * 60 * 1000);
			// if schedule with a period, may be call by several threads on the
			// same time, that means need to re-start
			// timer after execute
			serverListener.schedule(new HeartBeatTask(), 5000);
		}

		super.onStart();
	}

	private Handler upgradeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case UpgradeHelper.DOWNLOAD:
				// 设置进度条位置
				break;
			case UpgradeHelper.DOWNLOAD_FINISH:
				// 安装文件
				upgradeHelper.installApk();
				break;
			default:
				break;
			}
		};
	};
	private UpgradeHelper upgradeHelper = new UpgradeHelper(this, upgradeHandler);

	static int connectionFailedTime = 0;
	private Handler showServiceHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Intent intent;
			switch (msg.what) {
			case 0:
				// screen capture
				Bitmap map = ScreenCaptureHelper.takeScreenShot(MainActivity.this);
				// File file = new File("sc.png");
				// new FileOutputStream(file);
				try {
					ScreenCaptureHelper.savePic(map, openFileOutput("sc.png", MODE_PRIVATE));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (serverListener != null) {
					serverListener.schedule(new TimerTask() {

						@Override
						public void run() {
							showService.uploadScreen(localService.getLoginId(), new Date(),
							        MainActivity.this.getFileStreamPath("sc.png"));
						}

					}, 0);
				}
				break;
			case 1:
				// upgrade
				VersionInfo version = (VersionInfo) msg.obj;
				if (version != null) {
					showUpgradeNotice(version.getVersionName());
					upgradeHelper.downloadApk(version.getDownloadAddress());
				}
				break;
			case 2:
				// open connection failed notice
				if (++connectionFailedTime > 0) {
					openConnectionNote();
				}
				break;
			case 3:
				// close connection failed notice
				connectionFailedTime = 0;
				closeConnectionNote();
				break;
			case 4:
				// restart
				openRestartNotice();
				if (serverListener != null) {
					serverListener.schedule(new TimerTask() {

						@Override
						public void run() {
							restartApp();
						}

					}, 5000);
				}
				break;
			}
			super.handleMessage(msg);
		}

	};
	private PopupWindow popupNotice;
	private TextView networkDisconnectAlertText;
	private Handler changeNetworkAlertTextHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what < 1) {
				closeConnectionNote();
				if (serverListener != null) {
					serverListener.schedule(new HeartBeatTask(), 60000);
				}
			} else {
				networkDisconnectAlertText.setText(String.format(
				        getResources().getString(R.string.network_disconnect_alert), msg.what));
			}
			super.handleMessage(msg);
		}
	};

	protected void openConnectionNote() {
		if (!connectionNoticeOpened) {
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			Point outSize = new Point();
			display.getSize(outSize);
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			// 引入窗口配置文件
			View view = inflater.inflate(R.layout.connection_failed_notice, null);
			connectionFailedNotice = new PopupWindow(view, outSize.x, outSize.y, false);
			networkDisconnectAlertText = (TextView) view.findViewById(R.id.network_disconnect_alert);
			final int originalSecondToConnect = 10;
			networkDisconnectAlertText.setText(String.format(getResources()
			        .getString(R.string.network_disconnect_alert),
			        originalSecondToConnect));
			if (serverListener != null) {
				serverListener.schedule(new TimerTask() {
					int iSecondToConnect = originalSecondToConnect;

					@Override
					public void run() {
						changeNetworkAlertTextHandler.sendEmptyMessage(--iSecondToConnect);
						if (iSecondToConnect < 1) {
							cancel();
						}
					}
				}, 1000, 1000);
			}
			connectionFailedNotice.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.BOTTOM, 0, 0);

			connectionNoticeOpened = true;
		}
	}

	protected void closeConnectionNote() {
		if (connectionFailedNotice != null) {
			connectionFailedNotice.dismiss();
			connectionFailedNotice = null;
			connectionNoticeOpened = false;
		}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// handle the key from controller
		if (KeyEvent.ACTION_UP == event.getAction() && 0 == event.getRepeatCount()) {
			if (popMenu == null) {
				if (KeyEvent.KEYCODE_0 <= keyCode && KeyEvent.KEYCODE_9 >= keyCode) {
					commandBuffer.append(keyCode - KeyEvent.KEYCODE_0);
				} else if (KeyEvent.KEYCODE_NUMPAD_0 <= keyCode && KeyEvent.KEYCODE_NUMPAD_9 >= keyCode) {
					commandBuffer.append(keyCode - KeyEvent.KEYCODE_NUMPAD_0);
				}
			}
			switch (keyCode) {
			case KeyEvent.KEYCODE_TAB:
				if (popMenu == null) {
					if (commandBuffer.length() > 0) {
						if (LocalService.SET_SERVER_ADDRESS_COMMAND.equals(commandBuffer.toString())) {
							// open dialog to configure
							openServerAddressConfig();
						}
						commandBuffer = new StringBuffer();
					}
				}
				return true;
			case KeyEvent.KEYCODE_DEL:
				commandBuffer = new StringBuffer();
				break;
			case KeyEvent.KEYCODE_NUMPAD_0:
				openConnectionNote();
				try {
					ScreenCaptureHelper.savePic(ScreenCaptureHelper.takeScreenShot(MainActivity.this),
					        openFileOutput("sc.png", MODE_PRIVATE));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//
				// ScreenCaptureHelper
				// .savePic(ScreenCaptureHelper.takeScreenShot(MainActivity.this),
				// "sdcard/Download/sc.png");
				return true;
			case KeyEvent.KEYCODE_BACK:
				// close menu if it's open
				if (popMenu != null) {
					closeMenu();
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				// execute if menu is open
				if (popMenu != null) {
					return true;
				} else {
					// open menu
					openMenu();
					// showServiceHandler.sendEmptyMessage(2);
					// openConnectionNote();
					return true;
				}
			case KeyEvent.KEYCODE_DPAD_UP:
				// move focus if menu is open
				// if (popMenu != null) {
				// switch (focus) {
				// case CHANGE_REG_NUM:
				// break;
				// case CHANGE_AUTO_START:
				// focus = MenuFocus.CHANGE_REG_NUM;
				// break;
				// case EXIT:
				// focus = MenuFocus.CHANGE_AUTO_START;
				// break;
				// case NONE:
				// break;
				// }
				// moveMenuFocus(focus);
				// return true;
				// }
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				// move focus if menu is open
				// if (popMenu != null) {
				// switch (focus) {
				// case CHANGE_REG_NUM:
				// focus = MenuFocus.CHANGE_AUTO_START;
				// break;
				// case CHANGE_AUTO_START:
				// focus = MenuFocus.EXIT;
				// break;
				// case EXIT:
				// break;
				// case NONE:
				// break;
				// }
				// moveMenuFocus(focus);
				// return true;
				// }
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				// move focus if menu is open
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				// move focus if menu is open
				break;
			default:
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void openMenu() {

		// 设置按钮功能实现
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		// 引入窗口配置文件
		final View view = inflater.inflate(R.layout.menu_setting, null);
		popMenu = new PopupWindow(view, 1920, 1080, false);
		// 需要设置一下此参数，点击外边可消失
		popMenu.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		popMenu.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		popMenu.setFocusable(true);
		Button btnChangeRegnum = (Button) view.findViewById(R.id.btnChangeRegnum);
		btnChangeRegnum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open edit dialog
				view.findViewById(R.id.child_menu_change_reg_num).setVisibility(View.VISIBLE);
				view.findViewById(R.id.child_menu_change_auto_start).setVisibility(View.INVISIBLE);
			}
		});
		Button btnApplyNewRegnum = (Button) view.findViewById(R.id.btn_apply_change);
		final EditText editRegnum = (EditText) view.findViewById(R.id.edit_reg_num);
		editRegnum.setText(localService.getAuthCode());
		btnApplyNewRegnum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// apply new register number
				localService.saveAuthCode(editRegnum.getText().toString());
			}
		});
		Button btnChangeAutoStart = (Button) view.findViewById(R.id.btnChangeAutoStart);
		btnChangeAutoStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open choice dialog
				view.findViewById(R.id.child_menu_change_reg_num).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.child_menu_change_auto_start).setVisibility(View.VISIBLE);
			}
		});
		Button btnSwitchAutoStart = (Button) view.findViewById(R.id.btn_switch_auto_start);
		boolean autoStart = localService.isAutoStart();
		if (!autoStart) {
			btnSwitchAutoStart.setBackgroundResource(R.drawable.off_btn_selector);
		}
		btnSwitchAutoStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// switch auto start configuration
				boolean autoStart = !localService.isAutoStart();
				localService.setAutoStart(autoStart);
				if (autoStart) {
					v.setBackgroundResource(R.drawable.on_btn_selector);
				} else {
					v.setBackgroundResource(R.drawable.off_btn_selector);
				}
			}
		});
		Button btnExit = (Button) view.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open confirm exit dialog
				openExitAlert();
			}
		});
		popMenu.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				popMenu = null;
			}
		});
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.ACTION_UP == event.getAction() && 0 == event.getRepeatCount()) {
					if (KeyEvent.KEYCODE_BACK == keyCode) {
						// close menu if it's open
						if (popMenu != null) {
							closeMenu();
							return true;
						}
					}
				}
				return false;
			}

		});
		popMenu.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.NO_GRAVITY, 170, 60);
		btnChangeRegnum.requestFocus();
		// focus = MenuFocus.CHANGE_AUTO_START;
		// moveMenuFocus(MenuFocus.CHANGE_AUTO_START);
	}

	private void closeMenu() {
		//
		if (popMenu != null) {
			popMenu.dismiss();
			popMenu = null;
		}
	}

	private void restartApp() {
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

	private void openRestartNotice() {
		if (popupNotice == null) {
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			Point outSize = new Point();
			display.getSize(outSize);
			// create view and PopupWindow
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View view = inflater.inflate(R.layout.activity_restart_alert, null);
			popupNotice = new PopupWindow(view, outSize.x, outSize.y, false);
			popupNotice.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
		}
	}

	private void closeRestartNotice() {
		if (popupNotice != null) {
			popupNotice.dismiss();
			popupNotice = null;
		}
	}

	private void openExitAlert() {
		if (popupNotice != null) {
			popupNotice.dismiss();
			popupNotice = null;
		}
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);
		// create view and PopupWindow
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View view = inflater.inflate(R.layout.exit_alert_dialog, null);
		final PopupWindow exitAlert = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		Button btnExit = (Button) view.findViewById(R.id.exit);
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
		Button btnCancel = (Button) view.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// exit dialog
				exitAlert.dismiss();
			}
		});
		exitAlert.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
	}

	private void openServerAddressConfig() {
		// create view and PopupWindow
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
		configServerAddressDialog.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
	}

	/**
	 * show upgrade dialog
	 */
	private void showUpgradeNotice(String newVersion) {
		if (popupNotice != null) {
			popupNotice.dismiss();
			popupNotice = null;
		}
		// create view and PopupWindow
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.activity_upgrade_notice, null);
		popupNotice = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		TextView currentVersion = (TextView) view.findViewById(R.id.current_version_info);
		currentVersion.setText(String.format(getResources().getString(R.string.current_version_info),
		        UpgradeHelper.getVersionName(this)));
		TextView newVersionAlert = (TextView) view.findViewById(R.id.alert_new_version);
		newVersionAlert.setText(String.format(getResources().getString(R.string.alert_new_version), newVersion));
		popupNotice.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
	}

}
