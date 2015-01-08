package com.imusic.rewallet.UI;

import android.app.Activity;
import android.os.Bundle;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.id;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.widgets.RewardDetailVideoPlayer;
import com.imusic.rewallet.widgets.RewardDetailVideoPlayer.VideoPlayListener;

public class RewardVideoPlayerActivity extends Activity {

	private RewardDetailVideoPlayer _player;
	private String _url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getExtras() != null 
				&& getIntent().getExtras().getString("url").length() >0) {
			setContentView(R.layout.activity_reward_video_player);
			_url = getIntent().getExtras().getString("url");
			
			_player = (RewardDetailVideoPlayer)findViewById(R.id.rewardDetail_videoplayer);
			_player.prepareVideo(null, _url, vpListener);
		}else {
			finish();
		}
		
	}
	

	private VideoPlayListener vpListener = new VideoPlayListener(){
		@Override
		public void onVidePlayCompleted(boolean isCompleted) {
			if(isCompleted){
//				_btnRedeem.setButtonEnable(true);
				
//				_btnRedeem.setEnabled(true);
//				_rewardDetail_redeemCover.setVisibility(View.GONE);
//				postWatchVideo();
			}
		}
	};
}
