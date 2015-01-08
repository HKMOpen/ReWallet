package com.imusic.rewallet.dataUtil;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.imusic.rewallet.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyApplication extends Application implements
		UncaughtExceptionHandler {
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static Boolean MYLOG_WRITE_TO_FILE = true;

	@Override
	public void onCreate() {
		super.onCreate();
		// add log
		if (MYLOG_WRITE_TO_FILE) {
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(this);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		saveCrashInfo2File(throwable);
		mDefaultHandler.uncaughtException(thread, throwable);
	}

	public String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
//		SP.logE("test", "saveCrashInfo2File:"+result);
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_ss");
			String time = formatter.format(new Date());
			String fileName = "crash_" + time + "-" + timestamp + ".txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = Constants.Directory.getCrashLogFolder();
				
				File logFile = new File(String.format("%s/%s", path,fileName));
				if (!logFile.exists()) {
					logFile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(String.format("%s/%s", path,fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.d("test", "an error occured while writing file...");
		}
		return null;
	}
}
