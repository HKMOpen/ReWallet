package com.imusic.rewallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class AsyncThumbLoader {

	public AsyncThumbLoader() {
		options.inJustDecodeBounds = false;
		options.inSampleSize = 5;
	}

	private BitmapFactory.Options options = new BitmapFactory.Options();

	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	private Context mContext;
	public String videoPath=null;

	public Drawable loadDrawable(final String videoID, final Context context,
			final boolean isSaveIcon, final ImageCallback callback) {
		this.mContext = context;
		if (imageCache.containsKey(videoID)) {
			SoftReference<Drawable> softReference = imageCache.get(videoID);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				callback.imageLoaded((Drawable) msg.obj, videoID);
			}
		};
		new Thread() {
			public void run() {
				Drawable drawable = loadImageFromUrl(videoID, context,
						isSaveIcon);
				if(drawable!=null){
					imageCache.put(videoID, new SoftReference<Drawable>(drawable));
					handler.sendMessage(handler.obtainMessage(0, drawable));
				}
			};
		}.start();

		return null;
	}

	protected Drawable loadImageFromUrl(String videoID, Context context,
			boolean isSaveIcon) {
		
		Drawable drawable = null;
		try {
			if (videoID.equals("null") || videoID.isEmpty()) {
				return null;
			}
			Bitmap bm = Thumbnails.getThumbnail(mContext.getContentResolver(),
					Long.parseLong(videoID),
					MediaStore.Video.Thumbnails.MICRO_KIND,
					new BitmapFactory.Options());
			if (bm.getHeight() != 0) {
				drawable = new BitmapDrawable(bm);
			}

			return drawable;

		} catch (Exception e) {
			e.printStackTrace();
			Bitmap bm=Util.getVideoFirstImages(videoPath);
			if (bm!=null &&bm.getHeight() != 0) {
				drawable = new BitmapDrawable(bm);
			}
			return drawable;
		} finally {

		}
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String videoID);
	}
}
