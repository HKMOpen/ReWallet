package com.imusic.rewallet.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.ImageSliderItem;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ImageSlider extends RelativeLayout {

	private final static String TAG = "ImageSlider";
	
	private int _currentindex;
	private float _sizeScale = -99;
	
	private Context _context;
	private View _view;
	private ViewPager _viewPager;
	private LinearLayout _indicatorLayout;
	private ArrayList<ImageView> _indicators;
	private ArrayList<ImageView> _images;
	private ArrayList<ImageSliderItem> _items;
	private sampleViewPagerAdapter _adapter;
	
	private ImageSliderItem _currentItem;
	private OnClickListener _listener;
	
	//private int _currentNexttoTime = 1000;
	
	private Handler _taskHandler = new Handler(){  
	    @Override  
	    public void handleMessage(Message msg) {
	    	super.handleMessage(msg);
			if (_images != null && _images.size() > 0) {
				_currentindex = (_currentindex + 1) % _images.size();
				_viewPager.setCurrentItem(_currentindex);
			}
	    }
	}; 
	
	//private float _nextToTime;
	
	public ImageSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		_view = LayoutInflater.from(context).inflate(R.layout.view_imageslider, this);
		setupView();
	}
	
	public void setupView(){
//		if(_sizeScale != -99){
//			DisplayMetrics dm = new DisplayMetrics();
//			dm = _context.getResources().getDisplayMetrics();
//			View layout = _view.findViewById(R.id.imageSlider_layout);
//			layout.setLayoutParams(new LayoutParams(dm.widthPixels, dm.widthPixels * 9 / 16));
//		}
		_viewPager = (ViewPager)_view.findViewById(R.id.imageSlider_viewpager);
		_indicatorLayout = (LinearLayout)_view.findViewById(R.id.imageSlider_indicators);
		_images = new ArrayList<ImageView>();
		_indicators = new ArrayList<ImageView>();
		_adapter = new sampleViewPagerAdapter(_images,_items);
		_viewPager.setAdapter(_adapter);
		_viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				draw_Point(arg0);
				_currentindex = arg0;
				_currentItem = _items.get(arg0);
//				Vlog.getInstance().debug(false, TAG, "send message to handler....."+ _currentItem.nextToTime);
				//_taskHandler.sendEmptyMessageDelayed(0, (long)_currentItem.nextToTime);
				switchNextImage();
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	public void onStopImageSwitch(){
		if(_taskHandler == null)
			return;
		_taskHandler.removeMessages(1);
	}
	public void switchNextImage(){
		if(_taskHandler == null || _currentItem == null)
			return;
		_taskHandler.removeMessages(1);
		_taskHandler.sendEmptyMessageDelayed(1, (long)_currentItem.nextToTime);
	}
	public void onStartImageSwitchScheduled(){
		if (_images != null && _images.size() > 0) {
			_currentindex = 0; 
			_viewPager.setCurrentItem(0);
			_currentItem = _items.get(_currentindex);
			Vlog.getInstance().debug(false, TAG, "send message to handler....."+ _currentItem.nextToTime);
			//_taskHandler.sendEmptyMessageDelayed(0, (long)_currentItem.nextToTime);
			switchNextImage();
			draw_Point(_currentindex);
		}
	}
	public ImageSliderItem getSliderItem(int position){
		return _items == null ? null : _items.get(position);
	}
	public ImageView getSliderImageView(int position){
		return _images == null ? null: _images.get(position);
	}
	public ImageSliderItem getCurrentSliderItem(){
		return _currentItem;
	}
	public void setContent(ArrayList<ImageSliderItem> datas){
		_items = datas;
		resetViewPager();
	}
	public void setImageSliderItemOnClickListener(OnClickListener l){
		_listener = l;
	}
	
	private void resetViewPager(){
		if(_images != null){
			_images.removeAll(_images);
			_images = null;
		}
		if(_indicators != null){
			_indicators.removeAll(_indicators);
			_indicators = null;
		}
		_images = new ArrayList<ImageView>();
		_indicators = new ArrayList<ImageView>();
		_viewPager.removeAllViews();
		_indicatorLayout.removeAllViews();
		ImageView img;
		ImageView ind;
		 for (int i = 0; i < _items.size(); i++) {
			 img = new ImageView(_context);
			 final int postId = _items.get(i).postId ;
			 if(postId > 0){
				 if(_listener != null){
					 img.setOnClickListener(_listener);
				 }
			 }
			 img.setScaleType(ScaleType.FIT_XY);
			 _images.add(img);
			 ind= new ImageView(_context); 
			 ind.setBackgroundResource(R.drawable.slider_indicator_unchecked);
			 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
	                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,  
	                            LayoutParams.WRAP_CONTENT));  
	            layoutParams.leftMargin = 5;  
	            layoutParams.rightMargin = 5;  
	            _indicatorLayout.addView(ind, layoutParams);  
	            _indicators.add(ind);  
		 }
		 Vlog.getInstance().error(false, TAG, "images count:" + _images.size());
		 Vlog.getInstance().error(false, TAG, "item count:" + _items.size());
		_adapter.resetData(_images, _items);
		_adapter.notifyDataSetChanged();
		
		onStartImageSwitchScheduled();
	}
	
	private void draw_Point(int index) {  
        for (int i = 0; i < _indicators.size(); i++) {  
        	_indicators.get(i).setImageResource(R.drawable.slider_indicator_unchecked);  
        }  
        _indicators.get(index).setImageResource(R.drawable.slider_indicator_checked);  
    }  

	
	
	 
	
	
	class sampleViewPagerAdapter extends PagerAdapter{

		private List<ImageView> views;
		private List<ImageSliderItem> _lstItem;
		private DisplayImageOptions imageOptions = Util.getImageLoaderOptionForDetail(_context);
//		
		sampleViewPagerAdapter(List<ImageView> lstView, List<ImageSliderItem> lstItem){
			views = lstView;
			_lstItem = lstItem;
		}
		public void resetData(List<ImageView> lstView, List<ImageSliderItem> lstItem){
			views = lstView;
			_lstItem = lstItem;
		}
		//ImageView _img;
		
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override  
	    public Object instantiateItem(View container, int position) {  
			ImageView img = (ImageView)views.get(position);
			String url = _lstItem.get(position).url;
			ImageLoader.getInstance().displayImage(url, img,imageOptions);
			((ViewPager) container).addView(img);
			return img;
	    }
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		} 
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}
	}
	
}
