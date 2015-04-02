package com.tingken.infoshower.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.tingken.infoshower.R;

/**
 * @author tingken.com
 * @date 2015-3-12
 */

public class UpgradeHelper {
	/* 下载中 */
	public static final int DOWNLOAD = 1;
	/* 下载结束 */
	public static final int DOWNLOAD_FINISH = 2;
	/* 下载保存路径 */
	private String mSavePath;
	private String apkFileName;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Activity mParentActivity;
	/* 更新进度条 */
	private Handler upgradeHandler;
	private PopupWindow restartNotice;

	public UpgradeHelper(Activity parent, Handler upgradeHandler) {
		this.mParentActivity = parent;
		this.upgradeHandler = upgradeHandler;
	}

	/**
	 * 检测软件更新
	 * 
	 * @return
	 */
	public boolean checkUpdate(int serviceCode) {
		boolean neadUpgrade = isUpdate(serviceCode);
		if (neadUpgrade) {
			// 显示提示对话框
			showNoticeDialog();
		}
		return neadUpgrade;
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	public boolean isUpdate(int serviceCode) {
		// 获取当前软件版本
		int versionCode = getVersionCode(mParentActivity);
		if (serviceCode > versionCode) {
			return true;
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.tingken.infoshower", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo("com.tingken.infoshower", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		if (restartNotice == null) {
			// create view and PopupWindow
			LayoutInflater inflater = LayoutInflater.from(mParentActivity);
			View view = inflater.inflate(R.layout.activity_upgrade_notice, null);
			restartNotice = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
			restartNotice.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * 下载apk文件
	 */
	public void downloadApk(String url) {
		// 启动新线程下载软件
		new downloadApkThread(url).start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author tingken.com
	 * @date 2015-3-12
	 */
	private class downloadApkThread extends Thread {
		String downloadUrl;

		public downloadApkThread(String url) {
			downloadUrl = url.trim();
		}

		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(downloadUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					int nameIndex = downloadUrl.lastIndexOf('\\') > downloadUrl.lastIndexOf('/') ? downloadUrl
					        .lastIndexOf('\\') : downloadUrl.lastIndexOf('/');
					apkFileName = downloadUrl.substring(nameIndex);
					File apkFile = new File(mSavePath, apkFileName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						upgradeHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							upgradeHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			// restartNotice.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	public void installApk() {
		File apkfile = new File(mSavePath, apkFileName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mParentActivity.startActivity(i);
		mParentActivity.finish();
	}
}
