package com.imusic.rewallet.widgets;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.RewardVideoPlayerActivity;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class RewardDetailVideoPlayer extends LinearLayout {

	private static final String TAG = "RewardDetailVideoPlayer";
	private VideoView mVideoView;
	private String playPath;
	private String videoCover;
	private boolean isPlaying = false;
	private SeekBar seekBar;
	private TextView currTimeTxt;
	private TextView totalTimeTxt;
	private ImageView videedit_play;
	private ImageView iv_videoScreenShot;
	private int videoDuration;
	private ImageView videoCoverImgView;
	private boolean isPlayCompleted = false;
	private int screenWidth;
	private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForDetail();
	private Context _context;
	private VideoPlayListener _listener;
	private ImageButton fullScreenBtn;
	private Timer mTimer;
	public RewardDetailVideoPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		_imageOptions = Util.getImageLoaderOptionForDetail(_context);
		LayoutInflater.from(context).inflate(R.layout.view_rewarddetail_videoplayer, this);
		setupView();
	}
	public void prepareVideo(String Cover,String url, VideoPlayListener l){
		playPath = url;
		videoCover = Cover;
		_listener = l;
		Vlog.getInstance().info(false, TAG, "path:"+playPath + "\n cover:"+videoCover);
		initVideoView();
		loadVieoCover();
		mTimer = new Timer();
		mTimer.schedule(mTimerTask, 0, 1000);
	}
	private void setupView(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = _context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		RelativeLayout videoLayoutView = (RelativeLayout) findViewById(R.id.videoInfoLayout);
		videoLayoutView.setLayoutParams(new LinearLayout.LayoutParams(
				screenWidth, screenWidth * 10 / 16));
		Vlog.getInstance().info(false, TAG, "reset video layout params: "+screenWidth+ ","+screenWidth * 9 / 16);
		mVideoView = (VideoView) findViewById(R.id.surface_view);

		seekBar = (SeekBar) findViewById(R.id.SeekBar01);
		seekBar.setEnabled(false);
		seekBar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		currTimeTxt = (TextView) findViewById(R.id.currTime);
		totalTimeTxt = (TextView) findViewById(R.id.totalTime);

		videedit_play = (ImageView) findViewById(R.id.videedit_play);
		videedit_play.setOnClickListener(clickListener);

		iv_videoScreenShot = (ImageView) findViewById(R.id.video_thumb);
		iv_videoScreenShot.setVisibility(View.VISIBLE);
		
		fullScreenBtn = (ImageButton)findViewById(R.id.fullScreenBtn);
		fullScreenBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(_context, RewardVideoPlayerActivity.class);
				_context.startActivity(intent);
			}
		});

		
//		videoCoverImgView = (ImageView) findViewById(R.id.video_cover_img);
		
	}
	private void loadVieoCover(){
		if(videoCover != null && videoCover.length() > 0){
			ImageLoader.getInstance().displayImage(videoCover, iv_videoScreenShot,_imageOptions);
		}
	}
	private void initVideoView() {
		if(playPath!=null && !playPath.isEmpty()){			
			mVideoView.setVideoURI(Uri.parse(playPath));

			findViewById(R.id.video_temp_layout).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (isPlaying) {
								//pauseVideo();
							}
						}
					});
			mVideoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					videoDuration = mVideoView.getDuration();
					DecimalFormat dfTime = new DecimalFormat("0.0");
					totalTimeTxt.setText(dfTime.format(videoDuration / 1000.0) + "");
				}
			});
			mVideoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					videedit_play.setVisibility(View.VISIBLE);
					videedit_play.setImageResource(R.drawable.button_viewedit_play);
					isPlaying = false;

					int duration = mVideoView.getDuration();
					if(duration<0){
						duration=0;
					}
					DecimalFormat dfTime = new DecimalFormat("0.0");
					currTimeTxt.setText(dfTime.format(duration / 1000.0) + "");
					if (duration > 0) {
						seekBar.setProgress(seekBar.getMax());
						setPlayCompleted(true);
					}
				}
			});
			mVideoView.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Vlog.getInstance().error(false, TAG, "video View onError: what:" + what + ",extra:"+extra);
					return false;
				}
			});
		}
	}
	public void setPlayCompleted(boolean isPlayCompleted) {
		this.isPlayCompleted = isPlayCompleted;
		if(_listener != null)
			_listener.onVidePlayCompleted(isPlayCompleted);
	}
	public void playVideo() {
		iv_videoScreenShot.setVisibility(View.INVISIBLE);
		if (!isPlaying) {
			// videedit_play.setImageResource(R.drawable.videoedit_pause);
			videedit_play.setVisibility(View.GONE);

			playSDVideo();
			isPlaying = true;
		} else {
			pauseVideo();
		}
	}
	private void playSDVideo() {
		if (playPath != null) {
			mVideoView.requestFocus();
			mVideoView.start();
		}
	}
	public void pauseVideo() {
		mVideoView.pause();
		videedit_play.setVisibility(View.VISIBLE);
		videedit_play.setImageResource(R.drawable.button_viewedit_play);
		isPlaying = false;
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.videedit_play:
				playVideo();
				break;
			}
		}

	};
	
	
	/*******************************************************
	 * ͨ����ʱ����Handler�����½�����
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mVideoView == null)
				return;
			try {
				if (mVideoView.isPlaying() && seekBar.isPressed() == false) {
					handleProgress.sendEmptyMessage(0);
				}
			} catch (Exception e) {
			}

		}
	};
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (mVideoView == null) {
				return;
			}
			int position = mVideoView.getCurrentPosition();
			if(position<0){
				position=0;
			}
			DecimalFormat dfTime = new DecimalFormat("0.0");
			currTimeTxt.setText(dfTime.format(position / 1000.0) + "");
			int duration = mVideoView.getDuration();
			totalTimeTxt.setText(dfTime.format(duration / 1000.0) + "");
			if (duration > 0) {
				long pos = seekBar.getMax() * position / duration;
				seekBar.setProgress((int) pos);
			}
		};
	};
	
	
	public interface VideoPlayListener{
		public void onVidePlayCompleted(boolean isCompleted);
	}
	
	public void cancelTimer(){
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
}
