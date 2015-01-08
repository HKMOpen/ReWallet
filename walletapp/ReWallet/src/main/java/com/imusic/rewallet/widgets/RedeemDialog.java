package com.imusic.rewallet.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.imusic.rewallet.R;

public class RedeemDialog extends Dialog{


	private TextView _tvRewardName;
	private TextView _tvProvider;
	private Button _btnClaim;
	private Button _btnLater;
	private Context _context;
	
	private static int default_width = 160; //Ĭ�Ͽ��
    private static int default_height = 120;//Ĭ�ϸ߶�
	
	public RedeemDialog(Context context, int theme) {
		super(context, theme);
		this._context = context;
		
		 
         
//		_tvRewardName = (TextView)_view.findViewById(R.id.redeemDialogView_tvRewardName);
//		_tvProvider = (TextView)_view.findViewById(R.id.redeemDialogView_tvProvider);
//		_btnClaim = (Button)_view.findViewById(R.id.redeemDialogView_btnClaim);
//		_btnLater = (Button)_view.findViewById(R.id.redeemDialogView_btnLater);
	}
	 private float getDensity(Context context) {
         Resources resources = context.getResources();
         DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
     }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_dialog_redeem);
		_tvRewardName = (TextView)findViewById(R.id.redeemDialogView_tvRewardName);
		_tvProvider = (TextView)findViewById(R.id.redeemDialogView_tvProvider);
		_btnClaim = (Button)findViewById(R.id.redeemDialogView_btnClaim);
		_btnLater = (Button)findViewById(R.id.redeemDialogView_btnLater);
	}
	public void setContent(String rewardname, String provider){
		_tvRewardName.setText(rewardname);
		_tvProvider.setText(provider);
	}
	public void setCustomSize(int width, int height){
		Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //set width,height by density and gravity
        float density = getDensity(_context);
        params.width = (int) (width*density);
        params.height = (int) (height*density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
	}

}
