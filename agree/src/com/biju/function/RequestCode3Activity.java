package com.biju.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.RequestCodeback;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.userJoin2gourpListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class RequestCode3Activity extends Activity implements OnClickListener{

	private EditText mRequest_edt_code;
	private TextView mRequest_tv_code;
	private TextView mRequest_OK;
	private Interface requestcode3_interface;
	private Integer sD_pk_user;
	private Group readhomeuser;
	private ImageView mRequestcode2_head;
	private TextView mRequestcode2_User_nickname;
	private RelativeLayout mRequestcode2_number;
	private RelativeLayout mRequestcode2_introduce;
	private RelativeLayout mRequestcode2_public_phone;
	private RelativeLayout mRequestcode2_add_team;
	public static findTeamInterface findTeamInterface;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code3);
		// 从SD卡中获取到的Pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("RequestCodeActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);
		Intent intent = getIntent();
		readhomeuser=(Group) intent.getSerializableExtra(IConstant.Requestcode_readhomeuser);
		initInterface();
		initUI();
		initData();
	}

	private void initData() {
		String Avatar_path=readhomeuser.getAvatar_path();
		completeURL = beginStr + Avatar_path + endStr;
		PreferenceUtils.saveImageCache(RequestCode3Activity.this,completeURL);
		homeImageLoaderUtils.getInstance().LoadImage(RequestCode3Activity.this,completeURL, mRequestcode2_head);
		mRequestcode2_User_nickname.setText(readhomeuser.getName());
	
	}

	private void initInterface() {
		requestcode3_interface = Interface.getInstance();

		// 读取用户小组信息使用邀请码添加后的监听
		requestcode3_interface.setPostListener(new userJoin2gourpListenner() {

			@Override
			public void success(String A) {
				RequestCodeback requestCodeback = GsonUtils.parseJson(A,RequestCodeback.class);
				Integer status = requestCodeback.getStatusMsg();
				if (status == 1) {
					Log.e("RequestCodeActivity", "读取用户小组信息使用邀请码添加后的===" + A);
					SdPkUser.setRefreshTeam(true);
					Intent intent=new Intent(RequestCode3Activity.this, MainActivity.class);
					startActivity(intent);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initUI() {
		findViewById(R.id.Requestcode3_back).setOnClickListener(this);
		findViewById(R.id.RequestCode3_back_layout).setOnClickListener(this);
		mRequestcode2_head = (ImageView) findViewById(R.id.Requestcode3_head);// 查找小组的头像
		mRequestcode2_User_nickname = (TextView) findViewById(R.id.Requestcode3_User_nickname);// 查找小组的名称
		mRequestcode2_number = (RelativeLayout) findViewById(R.id.Requestcode3_number);// 小组人数
		mRequestcode2_introduce = (RelativeLayout) findViewById(R.id.Requestcode3_introduce);// 小组介绍
		mRequestcode2_public_phone = (RelativeLayout) findViewById(R.id.Requestcode3_public_phone);// 是否公开手机号码
		mRequestcode2_add_team = (RelativeLayout) findViewById(R.id.Requestcode3_add_team);// 加入小组
		mRequestcode2_add_team.setOnClickListener(this);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_code3, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Requestcode3_add_team:
			Requestcode2_OK();
			break;
		case R.id.Requestcode3_back:
		case R.id.RequestCode3_back_layout:
			Requestcode3_back();
			break;
		default:
			break;
		}
	}

	private void Requestcode3_back() {
		finish();
	}

	public void Requestcode2_OK() {
		Integer fk_group = readhomeuser.getPk_group();
		Group_User group_User = new Group_User();
		group_User.setFk_group(fk_group);
		group_User.setFk_user(sD_pk_user);
		group_User.setRole(2);
		group_User.setStatus(1);
		requestcode3_interface.userJoin2gourp(RequestCode3Activity.this,group_User);
	}

	public interface findTeamInterface {
		void findTeam(String code);
	}
}
