package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.GroupCodeback;
import com.BJ.javabean.GroupCodeback2;
import com.BJ.javabean.Group_Code2;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.produceRequestCodeListenner;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.R;
import com.fragment.CommonFragment;
import com.fragment.PhotoFragment2;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

@SuppressWarnings("unused")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GroupActivity extends SlidingFragmentActivity implements
		OnClickListener {
	public static int pk_group;

	public static int getPk_group() {
		return pk_group;
	}

	public static void setPk_group(int pk_group) {
		GroupActivity.pk_group = pk_group;
	}

	private Interface groupInterface;
	public static Integer pk_group_user;

	public static Integer getPk_group_user() {
		return pk_group_user;
	}

	public static void setPk_group_user(Integer pk_group_user) {
		GroupActivity.pk_group_user = pk_group_user;
	}

	private boolean photo;
	private boolean partyDetails;

	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 180;
	private int minVelocity = 0;

	private Integer sD_pk_user;
	private TextView mGroup_setting;
	private TextView mGroup_setting_title;
	private ListView mSlidingMenu_member_listView;
	private ArrayList<Group_ReadAllUser> Group_Readalluser_List = new ArrayList<Group_ReadAllUser>();
	private MyMemBerAdapter adapter;

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private TextView mSlidingMenu_requestcode_code;
	private RelativeLayout mSlidingMenu_requestcode;
	private RelativeLayout mSlidingMenu_Team_Setting;
	
	private boolean isCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		// 自定义actionbar的布局
		setActionBarLayout(R.layout.actionbar_port_layou);
		// 设置滑动菜单
		initSlidingMenu();
		// 获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("GroupActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initFragment();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra(IConstant.HomePk_group, pk_group);
		String name = intent.getStringExtra(IConstant.HomeGroupName);
		mGroup_setting_title.setText(name);

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		partyDetails = PartyDetails_sp.getBoolean(IConstant.PartyDetails, false);

		initInterface();//监听
		initreadUserGroupRelation();//获取小组的关系ID
		Log.e("GroupActivity", "进入了=========+onCreate");
		returndata();//读取小组中的所有用户
		initSlidingMenuUI();//侧滑ui
	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		CommonFragment commonFragment = new CommonFragment();
		ft.add(R.id.container, commonFragment);
		ft.commit();
	}

	private void initSlidingMenuUI() {
		mSlidingMenu_Team_Setting = (RelativeLayout) findViewById(R.id.SlidingMenu_Team_Setting);
		mSlidingMenu_Team_Setting.setOnClickListener(this);// 跳转至小组设置
		mSlidingMenu_requestcode = (RelativeLayout) findViewById(R.id.SlidingMenu_requestcode);
		mSlidingMenu_requestcode.setOnClickListener(this);// 生成小组邀请码
		mSlidingMenu_requestcode_code = (TextView) findViewById(R.id.SlidingMenu_requestcode_code);//显示所生成的邀请码
		mSlidingMenu_member_listView = (ListView) findViewById(R.id.SlidingMenu_member_listView);// listview
		adapter = new MyMemBerAdapter();
	}

	class ViewHOlder {
		ImageView Member_head;
		TextView Member_name;
		TextView Member_role;
		TextView Member_line_1;
		TextView Member_line_2;
	}

	class MyMemBerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return Group_Readalluser_List.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflater = null;
			ViewHOlder holder = null;
			if (convertView == null) {
				holder = new ViewHOlder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.slidingmenu_member_item, null);
				holder.Member_head = (ImageView) inflater.findViewById(R.id.Member_head);
				holder.Member_name = (TextView) inflater.findViewById(R.id.Member_name);
				holder.Member_role = (TextView) inflater.findViewById(R.id.Member_role);
				holder.Member_line_1 = (TextView) inflater.findViewById(R.id.Member_line_1);
				holder.Member_line_2 = (TextView) inflater.findViewById(R.id.Member_line_2);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHOlder) inflater.getTag();
			}
			if (Group_Readalluser_List.size() > 0) {
				Group_ReadAllUser group_ReadAllUser = Group_Readalluser_List.get(position);
				holder.Member_name.setText(group_ReadAllUser.getNickname());

				Integer role = group_ReadAllUser.getRole();
				if (role == 1) {
					holder.Member_role.setText("群主");
				} else {
					holder.Member_role.setText("成员");
				}

				String useravatar_path = group_ReadAllUser.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr;
				PreferenceUtils.saveImageCache(GroupActivity.this, completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImage(GroupActivity.this,completeURL, holder.Member_head);
				if (position < Group_Readalluser_List.size() - 1) {
					holder.Member_line_1.setVisibility(View.GONE);
					holder.Member_line_2.setVisibility(View.VISIBLE);
				} else {
					holder.Member_line_1.setVisibility(View.VISIBLE);
					holder.Member_line_2.setVisibility(View.GONE);
				}
			}

			return inflater;
		}

	}

	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 *            布局Id
	 * 
	 * */
	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			int height = actionBar.getHeight();
			LayoutInflater inflator = (LayoutInflater) GroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflator.inflate(layoutId, null);

			view.findViewById(R.id.group_back_layout).setOnClickListener(this);// 返回
			view.findViewById(R.id.group_back).setOnClickListener(this);
			view.findViewById(R.id.group_setting_layout).setOnClickListener(this);
			mGroup_setting = (TextView) view.findViewById(R.id.group_setting);
			mGroup_setting.setOnClickListener(this);// 设置
			mGroup_setting_title = (TextView) view.findViewById(R.id.group_setting_title);// 小组名称

			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, R.dimen.title_height);
			actionBar.setCustomView(view, layout);
		}
	}

	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu() {
		// 设置滑动菜单打开后的视图界面
		setBehindContentView(R.layout.slidingmenu);
		// getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame,
		// new SampleListFragment()).commit();

		// 设置当打开滑动菜单时，ActionBar不能够跟随着一起滑动
		setSlidingActionBarEnabled(false);

		// 设置滑动菜单的属性值
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.RIGHT);// 从右边滑出
		// sm.setShadowWidthRes(R.dimen.shadow_width);
		// sm.setShadowDrawable(R.drawable.shadow);
		// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 设置边缘滑动
		// 设置滑动时拖拽效果
		sm.setBehindScrollScale(0);
		// sm.setOnOpenListener(onOpenListener);//监听slidingmenu打开
		sm.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				mGroup_setting.setText("关闭");
			}
		});// 监听slidingmenu打开后
			// sm.OnCloseListener(OnClosedListener);//监听slidingmenu关闭时事件
		sm.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				mGroup_setting.setText("成员");
			}
		});// 监听slidingmenu关闭后事件

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	// 读取小组中的所有用户
	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(pk_group);
		groupInterface.readAllPerRelation(GroupActivity.this,readAllPerRelation_group);
	}


	private void initInterface() {
		groupInterface = Interface.getInstance();
		groupInterface.setPostListener(new readUserGroupRelationListenner() {
			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						pk_group_user = group_user.getPk_group_user();

					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		groupInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				Group_Readalluser_List.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("GroupActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							Group_Readalluser_List.add(readAllUser);
						}
						Log.e("GroupActivity", "加入到list中的东西====="+ Group_Readalluser_List.toString());
					}
					if (Group_Readalluser_List.size() > 0) {
						mSlidingMenu_member_listView.setAdapter(adapter);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		//邀请码监听
		groupInterface.setPostListener(new produceRequestCodeListenner() {

			@Override
			public void success(String A) {
				Log.e("GroupActivity", "=========" + A);
				try {
					JSONObject jsonObject = new JSONObject(A);
					Object object = jsonObject.get("returnData");
					if (object.toString().length() > 4) {
						java.lang.reflect.Type type = new TypeToken<GroupCodeback2>() {
						}.getType();
						GroupCodeback2 groupcodeback2 = GsonUtils.parseJsonArray(A, type);
						int Group_statusmsg2 = groupcodeback2.getStatusMsg();
						List<Group_Code2> grouplsit = (List<Group_Code2>) groupcodeback2.getReturnData();
						if (Group_statusmsg2 == 1) {
							Group_Code2 groupcode = grouplsit.get(0);
							String requestcode = groupcode.getPk_group_code();
							mSlidingMenu_requestcode_code.setText("邀请码:"+requestcode);
							isCode=true;
						}
					} else {
						GroupCodeback groupcodeback = GsonUtils.parseJson(A,GroupCodeback.class);
						int Group_statusmsg = groupcodeback.getStatusMsg();
						if (Group_statusmsg == 1) {
							String requestcode = groupcodeback.getReturnData();
							mSlidingMenu_requestcode_code.setText("邀请码:"+requestcode);
							isCode=true;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void defail(Object B) {
			}
		});
	}

	private void initreadUserGroupRelation() {
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		group_User.setFk_user(sD_pk_user);
		groupInterface.readUserGroupRelation(GroupActivity.this, group_User);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.group_back_layout:
		case R.id.group_back:
			group_back();
			break;
		case R.id.group_setting_layout:
		case R.id.group_setting:
			group_setting();
			break;
		case R.id.SlidingMenu_Team_Setting:
			SlidingMenu_Team_Setting();
			break;
		case R.id.SlidingMenu_requestcode:
			SlidingMenu_requestcode();
			break;
		default:
			break;
		}
	}

	private void SlidingMenu_requestcode() {
		mSlidingMenu_Team_Setting.setBackgroundResource(R.color.white);
		mSlidingMenu_requestcode.setBackgroundResource(R.color.lightgray1);
		if(!isCode)
		{
			Group Group_teamsetting = new Group();
			Group_teamsetting.setPk_group(pk_group);
			groupInterface.produceRequestCode(GroupActivity.this,Group_teamsetting);
		}
	}

	private void SlidingMenu_Team_Setting() {
		mSlidingMenu_Team_Setting.setBackgroundResource(R.color.lightgray1);
		mSlidingMenu_requestcode.setBackgroundResource(R.color.white);
		Intent intent = new Intent(GroupActivity.this,TeamSetting2Activity.class);
		intent.putExtra(IConstant.Group,pk_group);
		startActivity(intent);
	}

	private void group_setting() {
		toggle();
	}

	public void group_back() {
		finish();
	}

	@Override
	protected void onStop() {
		SharedPreferences sp = getSharedPreferences("isPhoto", 0);
		Editor editor = sp.edit();
		editor.putBoolean("Photo", false);
		editor.commit();

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean(IConstant.PartyDetails, false);
		PartyDetails_editor.commit();

		SharedPreferences refresh_sp = getSharedPreferences(IConstant.AddRefresh, 0);
		Editor editor2 = refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh, false);
		editor2.commit();
		super.onStop();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("GroupActivity", "调用了这个onActivityResult");
		Log.e("GroupActivity", "调用了这个requestCode=="+requestCode);
		Log.e("GroupActivity", "调用了这个data=="+data);
		PhotoFragment2.onActivityResultInterface.onActivityResult(requestCode, resultCode, data);
	}

}
