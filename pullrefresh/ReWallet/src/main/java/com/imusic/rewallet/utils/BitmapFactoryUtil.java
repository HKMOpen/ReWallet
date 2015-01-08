package com.imusic.rewallet.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapFactoryUtil {
	private static final String TAG = "BitmapFactoryUtil";
	static DisplayMetrics metric = new DisplayMetrics();
	public BitmapFactoryUtil(){
		 
	}
	public static Bitmap getBitmapFromID(Context context, int intID){
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), intID);
		return bitmap;
	}
	
	public static void setBitmapScaleFitParent(Activity context,
			ImageView view, int intRid, int pW){
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources()
				, intRid, bfOptions);
		if(bitmap != null){
			int bW = bfOptions.outWidth;
	    	int bH = bfOptions.outHeight;
	    	float scale = (float)pW/(float)bW;
	    	float h = bH*scale;
	    	bitmap = resizeImage(bitmap, pW, (int)h);
	    	view.setImageBitmap(bitmap);
		}
	}
	
	public static void setBitmapScaleFitParent(Activity context, 
			ImageView view, String imgPath, int pW){
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		bfOptions.inDither=false;                     
	    bfOptions.inPurgeable=true;                 
	    bfOptions.inInputShareable=true;             
	    bfOptions.inTempStorage=new byte[32 * 1024]; 
	    Bitmap bm = null;
	    File file = new File(imgPath);
	    FileInputStream fs=null;
	    try {
	        fs = new FileInputStream(file);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }

	    try {
	        if(fs!=null) 
	        	 bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally{ 
	        if(fs!=null) {
	            try {
	                fs.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    if(bm != null){
	    	int bW = bfOptions.outWidth;
	    	int bH = bfOptions.outHeight;
	    	float scale = (float)pW/(float)bW;
	    	float h = bH*scale;
	    	bm = resizeImage(bm, pW, (int)h);
	    	view.setImageBitmap(bm);
	    }
	}
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h)   
    {    
        Bitmap BitmapOrg = bitmap;    
        int width = BitmapOrg.getWidth();    
        int height = BitmapOrg.getHeight();    
        int newWidth = w;    
        int newHeight = h;    
  
        float scaleWidth = ((float) newWidth) / width;    
        float scaleHeight = ((float) newHeight) / height;    
  
        Matrix matrix = new Matrix();    
        matrix.postScale(scaleWidth, scaleHeight);    
        // if you want to rotate the Bitmap     
        // matrix.postRotate(45);     
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,    
                        height, matrix, true);    
        //return new BitmapDrawable(resizedBitmap);
        return resizedBitmap;
    }  
	
	public static void setBitmapScaleFitScreen(Activity context, ImageButton view, int intRid){
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), intRid);
		if(bitmap != null){
			context.getWindowManager().getDefaultDisplay().getMetrics(metric);
			
			int height = bitmap.getHeight();
			float scalew = (float) metric.widthPixels / (float) bitmap.getWidth();
			view.setScaleType(ScaleType.MATRIX);
			float pH = height*scalew;
			LayoutParams p = (LayoutParams) view.getLayoutParams();
			p.height = (int)pH;
			view.setLayoutParams(p);
			if(!bitmap.isRecycled()){
				bitmap.recycle();
				System.gc();
			}
		}
	}
	
	public static Drawable getDrawableFromBitmap(Bitmap bmp){
		Drawable drawable =	new BitmapDrawable(bmp);
		return drawable;
	}
	
	public static Drawable getDrawableFromImageLoaderCache(String url) throws IOException,FileNotFoundException{
		Drawable drawable = null;
		File f = ImageLoader.getInstance().getDiskCache().get(url);
		if(f.exists()){
			
				FileInputStream fs = new FileInputStream(f);
				BufferedInputStream bs = new BufferedInputStream(fs); 
		        Bitmap btp = BitmapFactory.decodeStream(bs); 
		        drawable = getDrawableFromBitmap(btp);
		        bs.close(); 
		        fs.close(); 
		        btp = null;  
		 
		}
		return drawable;
	}
	
}
