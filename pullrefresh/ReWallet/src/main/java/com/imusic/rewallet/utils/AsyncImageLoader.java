package com.imusic.rewallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncImageLoader {

	private static AsyncImageLoader instance;

	public static AsyncImageLoader getIntance() {
		if (instance == null) {
			instance = new AsyncImageLoader();			
		}
		return instance;
	}

	public static void FreeDrawable(Drawable d){
		if(d != null){
			try{
				BitmapDrawable bd = (BitmapDrawable)d;
				bd.getBitmap().recycle();	
			}
			catch(Exception ex){
				//Vlog.getInstance(context)("AsyncImageLoader", ex.getMessage());
			}
		}
	}
	
	private AsyncImageLoader() {
		//imageCache = new HashMap<String, SoftReference<Drawable>>();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;
		String photopath = Constants.SDCARD + "/Innoactor/ImagesCache/";
		try {
			File dirFile = new File(photopath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
		} catch (Exception ex) {
			//SP.logE("AsyncImageLoader", ex.getMessage());
		}
	}

	private BitmapFactory.Options options = new BitmapFactory.Options();

	//private Map<String, SoftReference<Drawable>> imageCache;

	
	public Drawable loadDrawable(final String imageUrl, final Context context,
			final boolean isSaveIcon, final ImageCallback callback) {
		String photopath = Constants.SDCARD + "/Innoactor/ImagesCache/";
		String filepath = photopath.concat(MD5.encode(imageUrl)).concat(".").concat("tmp");
		Drawable drawable = null;
		File file = new File(filepath);
		try {
			if(file.exists()){
				//Bitmap bm = BitmapFactory.decodeFile(filepath);
				Bitmap bm = getSmallBitmap(filepath);
				drawable = convertBitmap2Drawable(bm);	
			}
		} catch (Error ex) {
			drawable = null;
			file.delete();	
		} catch (Exception ex){
			drawable = null;
			file.delete();
		}
		file = null;
		if(drawable != null){
			System.out.println("Load image file from local storage.");
			return drawable;
		}
		
		InternalDownloadHelper helper = new InternalDownloadHelper(callback, imageUrl, MD5.encode(imageUrl), context, isSaveIcon);
		helper.Start();
		return null;
	}

	//ArrayList<String> downloadingImageUrls = new ArrayList<String>();

	protected Drawable loadImageFromUrl(String imageUrl, Context context,
			boolean isSaveIcon, String imageUrlMd5) {
		//SP.logD("test", "loadImageFromUrl:" + imageUrl);
		try {
//			if (imageUrl.equals("null") || imageUrl.isEmpty()) {
//				return null;
//			}
//			if (downloadingImageUrls.contains(imageUrl)) {
//				SP.logD("test", "图片正在下载中.....");
//				return null;
//			}
			Drawable drawable = null;
			String photopath = Constants.SDCARD + "/Innoactor/ImagesCache/";
			//File pFile=new File(photopath);
//			if(!pFile.exists()){
//				pFile.mkdirs();
//			}
			// String filepath =
			// photopath.concat(imageUrlMd5).concat(".").concat(MimeTypeMap.getFileExtensionFromUrl(imageUrl));
			String filepath = photopath.concat(imageUrlMd5).concat(".").concat("tmp");
//			SP.logD("test", "filepath:" + filepath);
//			File file = new File(filepath);
//			if (file.exists()) {
//				try {
//					Bitmap bm = BitmapFactory.decodeFile(filepath);
//					drawable = convertBitmap2Drawable(bm);
//					if (drawable != null) {
//						return drawable;
//					}else{
//						file.delete();
//					}
//				} catch (Error e) {
//					return null;
//				}
//			}

			HttpURLConnection httpcon = (HttpURLConnection) new URL(imageUrl)
					.openConnection();
			//downloadingImageUrls.add(imageUrl);
			httpcon.setConnectTimeout(15000);
			InputStream is = httpcon.getInputStream();

			Bitmap bitmap = null;

			try {
				bitmap = BitmapFactory.decodeStream(is, null, options);
			} catch (Error ex) {
				//SP.logE("AsyncImageLoader", ex.getMessage());
				bitmap = null;
			}
			//downloadingImageUrls.remove(imageUrl);
			// Bitmap bitmap = BitmapFactory.decodeStream(is);
			if (bitmap != null) {
				drawable = new BitmapDrawable(bitmap);
			} else {
				return null;
			}

			if (drawable != null && isSaveIcon) {
				try {
					if (Util.hasSdcard()) {
						File iconFile = new File(filepath);
						//SP.logD("test", "save image, filepath=" + filepath);
						iconFile.createNewFile();
						// BitmapDrawable bitmapDrawable = (BitmapDrawable)
						// drawable;
						// Bitmap bitMap = bitmapDrawable.getBitmap();
						Bitmap bitMap = bitmap;

						BufferedOutputStream stream = new BufferedOutputStream(
								new FileOutputStream(iconFile));
						bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
						stream.flush();
						stream.close();
					}
				} catch (Exception e) {
					//SP.logD("test", "save image exception..." + e.getMessage());
					e.printStackTrace();
				}
			} else {
				//SP.logD("test", "download drawable is null...");
			}
			return drawable;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {

		}
	}

	protected Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	class InternalDownloadHelper {
		ImageCallback callback;
		String imageUrl;
		String imageUrlMd5;
		Context context;
		boolean isSaveIcon;
		Handler handler;

		public InternalDownloadHelper(ImageCallback callback,
				String imageUrl, String imageUrlMd5, Context context,
				boolean isSaveIcon) {
			this.callback = callback;
			this.imageUrl = imageUrl;
			this.context = context;
			this.isSaveIcon = isSaveIcon;
			this.imageUrlMd5 = imageUrlMd5;

			handler = new MyHandler(this);

		}

		public void doInHandler(Message msg) {
			callback.imageLoaded((Drawable) msg.obj, imageUrl);
			this.callback = null;
			this.handler = null;
		}

		public void Start() {
			new Thread() {
				public void run() {
					Drawable drawable = loadImageFromUrl(imageUrl, context, isSaveIcon, imageUrlMd5);
					if (drawable != null) {
						handler.sendMessage(handler.obtainMessage(0, drawable));
					}
				};
			}.start();
		}
	}

	static class MyHandler extends Handler {
		InternalDownloadHelper helper;

		public MyHandler(InternalDownloadHelper helper) {
			this.helper = helper;
		}

		@Override
		public void handleMessage(Message msg) {
			if (helper != null) {
				helper.doInHandler(msg);
			}
		}
	}
	
	
	///0000000000000000000000000
	public static Bitmap getSmallBitmap(String filePath) {
    	
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 90, 80);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if(bm == null){
			return  null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm,degree) ;
		ByteArrayOutputStream baos = null ;
		try{
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
			
		}finally{
			try {
				if(baos != null)
					baos.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm ;

	}
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
		if(bitmap == null)
			return null ;
		
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}
	private static int readPictureDegree(String path) {  
	       int degree  = 0;  
	       try {  
	               ExifInterface exifInterface = new ExifInterface(path);  
	               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	               switch (orientation) {  
	               case ExifInterface.ORIENTATION_ROTATE_90:  
	                       degree = 90;  
	                       break;  
	               case ExifInterface.ORIENTATION_ROTATE_180:  
	                       degree = 180;  
	                       break;  
	               case ExifInterface.ORIENTATION_ROTATE_270:  
	                       degree = 270;  
	                       break;  
	               }  
	       } catch (IOException e) {  
	               e.printStackTrace();  
	       }  
	       return degree;  
	   } 
	
}
