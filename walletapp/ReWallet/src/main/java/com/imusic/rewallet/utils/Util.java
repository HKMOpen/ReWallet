package com.imusic.rewallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.imusic.rewallet.R;
import com.imusic.rewallet.widgets.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;


public class Util {
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static void delSDFile(String filename) {
		//SP.logD("test", "del file=" + filename);
		if (filename != null) {
			File file = new File(filename);
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						delSDFile(files[i].getName());
					}
				}
				file.delete();
			}
		}
	}
	
	public static Bitmap getVideoFirstImages(String path) {
		if (path == null) {
			return null;
		}
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(path);
		Bitmap bitmap = retriever.getFrameAtTime();
		retriever.release();
		return bitmap;
	}
	
	
	public static void initImageLoaderConfig(Context context){
		if(!ImageLoader.getInstance().isInited()){
			UnlimitedDiscCache ch = new UnlimitedDiscCache(new File(Constants.Directory.getImageCacheFolder()));
			ImageLoaderConfiguration config = new ImageLoaderConfiguration  
				    .Builder(context)  
				    //.memoryCacheExtraOptions(480, 800) // max width, max height���������ÿ�������ļ�����󳤿�  
				    .threadPoolSize(5)//�̳߳��ڼ��ص�����  
				    .threadPriority(Thread.NORM_PRIORITY - 2)  
				    .denyCacheImageMultipleSizesInMemory()  
				    .memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024)) // You can pass your own memory cache implementation/�����ͨ���Լ����ڴ滺��ʵ��  
				    .memoryCacheSize(5 * 1024 * 1024)
				    .tasksProcessingOrder(QueueProcessingType.LIFO)  
				    //.discCacheFileCount(500) //������ļ�����  
				    //.discCache(ch)//�Զ��建��·��  
				    .diskCacheFileCount(800)
				    .diskCache(ch)
				    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
				    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)��ʱʱ��  
				    //.writeDebugLogs() // Remove for release app  
				    .build();//��ʼ����  
			ImageLoader.getInstance().init(config);
		}
	}
	
	public static DisplayImageOptions getImageLoaderOptionForList(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.rewardchannel_error_image)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.rewardchannel_error_image)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		.cacheOnDisk(true)
		//.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		//.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF������ת����ת��
		//.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ  
		//.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������//  
		//.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//����ͼƬ�Ľ�������  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		//����ͼƬ���뻺��ǰ����bitmap��������  
		//.preProcessor(BitmapProcessor preProcessor)  
		.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
		//.displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����  
		//.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ��  
		
		.build();
		return options;
	}
	public static DisplayImageOptions getImageLoaderOptionForApp(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.rewardchannel_error_image)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.rewardchannel_error_image)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ��� 
		.cacheOnDisk(true)
		//.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		//.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF������ת����ת��
		//.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ  
		//.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������//  
		//.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//����ͼƬ�Ľ�������  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		//����ͼƬ���뻺��ǰ����bitmap��������  
		//.preProcessor(BitmapProcessor preProcessor)  
		.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
		.displayer(new RoundedBitmapDisplayer(40))//�Ƿ�����ΪԲ�ǣ�����Ϊ����  
		//.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ��  
		
		.build();
		return options;
	}
	public static DisplayImageOptions getImageLoaderOptionForDetail(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.rewardchannel_error_image)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.rewardchannel_error_image)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		//.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.cacheOnDisk(true)
		//.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF������ת����ת��
		//.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ  
		//.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������//  
		//.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//����ͼƬ�Ľ�������  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		//����ͼƬ���뻺��ǰ����bitmap��������  
		//.preProcessor(BitmapProcessor preProcessor)  
		//.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
		//.displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����  
		//.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ��  
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return options;
	}
	
	public static DisplayImageOptions getImageLoaderOptionForMissionList(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.mission_thumbnail_default)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.mission_thumbnail_default)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		//.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.cacheOnDisk(true)
		//.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF������ת����ת��
		//.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ  
		//.bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������//  
		//.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//����ͼƬ�Ľ�������  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		//����ͼƬ���뻺��ǰ����bitmap��������  
		//.preProcessor(BitmapProcessor preProcessor)  
		.resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
		//.displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����  
		//.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ��  
		
		.build();
		return options;
	}
	public static DisplayImageOptions getImageLoaderOptionForUserProfile(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.userprofile_nopic_default_big)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.userprofile_nopic_default_big)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.displayer(new CircleBitmapDisplayer())
		.build();
		return options;
	}
	
	public static DisplayImageOptions getImageLoaderOptionForUserProfile_actionbar(Context context){
		initImageLoaderConfig(context);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 //.showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ  
		.showImageForEmptyUri(R.drawable.userprofile_nopic_default_big)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ  
		.showImageOnFail(R.drawable.userprofile_nopic_default_big)  //����ͼƬ����/�������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.displayer(new CircleBitmapDisplayer())
		.build();
		return options;
	}
	
}
