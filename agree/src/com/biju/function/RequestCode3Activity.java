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
import com.BJ.javabean.GroupNumber;
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
	private GroupNumber readhomeuser;
	private ImageView mRequestcode2_head;
	private TextView mRequestcode2_User_nickname;
	private RelativeLayout mRequestcode2_number;
	private RelativeLayout mRequestcode2_introduce;
	private RelativeLayout mRequestcode2_public_phone;
	private RelativeLayout mRequestcode2_add_team;
	public static findTeamInterface findTeamInterface;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private int number;
	private TextView mRequestcode2_txet;
	private boolean isPublic;
	private ImageView mRequestcode2_public_choose;
	private Integer public_phone=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code3);
		// 从SD卡中获取到的Pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("RequestCodeActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);
		Intent intent = getIntent();
		readhomeuser=(GroupNumber) intent.getSerializableExtra(IConstant.Requestcode_readhomeuser);
		number = intent.getIntExtra("RequestCodeNumber", 0);
		initInterface();
		initUI();
		initData();
	}

	private void initData() {
		String Avatar_path=readhomeuser.getAvatar_path();
		completeURL = beginStr + Avatar_path + endStr+"avatar";
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
		mRequestcode2_public_choose = (ImageView) findViewById(R.id.requestcode2_public_choose);
		mRequestcode2_txet = (TextView) findViewById(R.id.Requestcode2_txet);
		findViewById(R.id.Requestcode3_back).setOnClickListener(this);
		findViewById(R.id.RequestCode3_back_layout).setOnClickListener(this);
		mRequestcode2_head = (ImageView) findViewById(R.id.Requestcode3_head);// 查找小组的头像
		mRequestcode2_User_nickname = (TextView) findViewById(R.id.Requestcode3_User_nickname);// 查找小组的名称
		mRequestcode2_number = (RelativeLayout) findViewById(R.id.Requestcode3_number);// 小组人数
		mRequestcode2_introduce = (RelativeLayout) findViewById(R.id.Requestcode3_introduce);// 小组介绍
		mRequestcode2_public_phone = (RelativeLayout) findViewById(R.id.Requestcode3_public_phone);// 是否公开手机号码
		mRequestcode2_public_phone.setOnClickListener(this);
		mRequestcode2_add_team = (RelativeLayout) findViewById(R.id.Requestcode3_add_team);// 加入小组
		mRequestcode2_add_team.setOnClickListener(this);
		mRequestcode2_txet.setText(number+"");
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
		case R.id.Requestcode3_public_phone:
			Requestcode3_public_phone();
			break;
		default:
			break;
		}
	}

	private void Requestcode3_public_phone() {
		isPublic=!isPublic;
		if(isPublic){
			public_phone=1;
			mRequestcode2_public_choose.setVisibility(View.VISIBLE);
		}else {
			public_phone=0;
			mRequestcode2_public_choose.setVisibility(View.GONE);
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
		group_User.setPublic_phone(public_phone);
		requestcode3_interface.userJoin2gourp(RequestCode3Activity.this,group_User);
	}

	public interface findTeamInterface {
		void findTeam(String code);
	}
}
