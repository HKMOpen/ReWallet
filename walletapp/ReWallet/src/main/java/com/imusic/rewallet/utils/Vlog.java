package com.imusic.rewallet.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vlog {
	private static class Nested{
		static final Vlog instance = new Vlog();
	}
	private static Context _context;
	private  SimpleDateFormat _dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private  SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private  String _VlogName = "Log.txt";
	private  String _VlogDir = "VCoinAppLog";
	private  String _logLineFormat = "[%s][%s] [%s] %s";
	private Vlog(){}
	
	public static Vlog getInstance(){
		
		return Nested.instance;
	}
	public void error(boolean isSaveLog, String tag, String msg){
		Log.e(tag, msg);
		if(isSaveLog){
			writeLogtoFile("error",tag,msg);
		}
	}
	public void debug(boolean isSaveLog, String tag, String msg){
		Log.d(tag, msg);
		if(isSaveLog){
			writeLogtoFile("debug",tag,msg);
		}
	}
	public void info(boolean isSaveLog, String tag, String msg){
		Log.i(tag, msg);
		if(isSaveLog)
			writeLogtoFile("info",tag,msg);
	}
	private void writeLogtoFile(String level, String tag, String text) {
		Date nowtime = new Date();
		String needWriteFiel = _dateFormat.format(nowtime);
		String msg = String.format(_logLineFormat,level, _dateTimeFormat.format(nowtime),
				tag, text);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			  String path = String.format("%s/%s/", Environment.getExternalStorageDirectory().getPath(),_VlogDir);   
              File dir = new File(path);   
              if (!dir.exists()) {   
                  dir.mkdirs();   
              }
      		try {
              File logFile = new File(path + needWriteFiel+ _VlogName);
              if(!logFile.exists()){
              	logFile.createNewFile();
              }

				FileWriter filerWriter = new FileWriter(logFile, true);
				BufferedWriter bufWriter = new BufferedWriter(filerWriter);
				bufWriter.write(msg);
				bufWriter.newLine();
				bufWriter.close();

				filerWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
