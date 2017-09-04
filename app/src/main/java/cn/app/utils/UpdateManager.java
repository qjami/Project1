package cn.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.yhub.app.R;

/**
 * 版本更新
 */

public class UpdateManager {
	/* 下载�? */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/**
	 * 是否升级
	 */
	public static Boolean isUpdate = false;
	private String name = "ydwapp";
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数�? */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Activity mContext;
	private String urlDownload;
	/* 更新进度�? */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private boolean isForce;
	/* 更新的内容提示 */
	private ArrayList<String> updateContent;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位�?
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Activity context, ArrayList<String> updateContent,
                         String urlDownload, boolean isForce) {
		this.mContext = context;
		this.urlDownload = urlDownload;
		this.isForce = isForce;
		this.updateContent = updateContent;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(String content) {
		if (isUpdate) {
			// 显示提示对话框
			showNoticeDialog(content);
		} else {
			Toast.makeText(mContext, "没有更新！", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 显示软件更新对话�?
	 */
	private void showNoticeDialog(String content) {
		// 构�?�对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("更新提示！");
		String mContent = "";
		for (int i = 0; i < updateContent.size(); i++) {
			mContent += "\n" + updateContent.get(i);
		}
		builder.setMessage(mContent);
		// 更新
		builder.setPositiveButton("立即更新",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话�?
						showDownloadDialog();
					}
				});
		// 稍后更新
		builder.setNegativeButton("以后再说",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (isForce) {
							mContext.finish();
						}
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
		noticeDialog.setCanceledOnTouchOutside(false);
		noticeDialog.setCancelable(false);
		// 取消后，不更新
		noticeDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	/**
	 * 显示软件下载对话�?
	 */
	@SuppressLint("InflateParams")
	private void showDownloadDialog() {
		// 构�?�软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("更新提示！");
		// 给下载对话框增加进度�?
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 设置取消状�??
						cancelUpdate = true;
					}
				});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软�?

		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// Log.i("devices", "11:" +
				// Environment.getExternalStorageState());
				// Log.i("devices", "22:" + Environment.MEDIA_MOUNTED);

				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(urlDownload);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// Log.i("devices", "333:" + length);
					// 创建输入�?
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, name);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位�?
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下�?.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显�?
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, name);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		// PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
		// i, 0);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}
