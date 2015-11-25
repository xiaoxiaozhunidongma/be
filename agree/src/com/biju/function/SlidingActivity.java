package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.produceRequestCodeListenner;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.chatroom.PersonalDataActivity;
import com.fragment.CommonFragment;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

@SuppressLint("CommitPrefEdits")
public class SlidingActivity extends Activity implements OnClickListener,OnItemClickListener {

	private ListView mSlidingMenu_member_listView;
	private TextView mSlidingMenu_requestcode_code;
	private RelativeLayout mSlidingMenu_requestcode;
	private RelativeLayout mSlidingMenu_Team_Setting;
	private MyMemBerAdapter adapter;
	private ArrayList<Group_ReadAllUser> Group_Readalluser_List = new ArrayList<Group_ReadAllUser>();

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private boolean isCode;
	private Integer pk_group;
	private Interface sildingInterface;
	private int isClick=0;
	private boolean MainClikc;
	private RelativeLayout mSliding_OK_layout;
	private boolean source;
	private Integer mGroupManager;
	public static GetReadGroupMember readGroupMember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding);
		Intent intent = getIntent();
		pk_group = intent.getIntExtra("group_group", 0);
		source = intent.getBooleanExtra("PersonalData", false);
		
		initUI();
		initSlidingMenuUI();// 侧滑ui
		initInterface();
		returndata();// 读取小组中的所有用户
		SharedPreferences Sliding_sp=getSharedPreferences(IConstant.SlidingClick, 0);
		int Click=Sliding_sp.getInt(IConstant.Click, 0);
		isClick=Click;
		Log.e("SlidingActivity", "这时候的Click=========" + Click);
		switch (Click) {
		case 0:
			mSlidingMenu_Team_Setting.setBackgroundResource(R.color.white);
			mSlidingMenu_requestcode.setBackgroundResource(R.color.white);
			break;
		case 1:
			mSlidingMenu_Team_Setting.setBackgroundResource(R.drawable.Sliding_choose_color);
			mSlidingMenu_requestcode.setBackgroundResource(R.color.white);
			break;
		case 2:
			mSlidingMenu_Team_Setting.setBackgroundResource(R.color.white);
			mSlidingMenu_requestcode.setBackgroundResource(R.drawable.Sliding_choose_color);
			break;
		default:
			break;
		}
		initReadGroupMember();//添加完好友后进行所有好友重新读取的接口
	}

	private void initReadGroupMember() {
		GetReadGroupMember readGroupMember=new GetReadGroupMember(){

			@Override
			public void ReadGroupMember() {
				returndata();
			}
			
		};
		this.readGroupMember=readGroupMember;
	}

	// 读取小组中的所有用户
	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(pk_group);
		sildingInterface.readAllPerRelation(SlidingActivity.this,readAllPerRelation_group);
	}

	private void initInterface() {
		sildingInterface = Interface.getInstance();
		// 邀请码监听
		sildingInterface.setPostListener(new produceRequestCodeListenner() {

			@Override
			public void success(String A) {
				Log.e("SlidingActivity", "邀请码返回=========" + A);
				try {
					JSONObject jsonObject = new JSONObject(A);
					Object object = jsonObject.get("returnData");
					if (object.toString().length() > 4) {
						java.lang.reflect.Type type = new TypeToken<GroupCodeback2>() {}.getType();
						GroupCodeback2 groupcodeback2 = GsonUtils.parseJsonArray(A, type);
						int Group_statusmsg2 = groupcodeback2.getStatusMsg();
						List<Group_Code2> grouplsit = (List<Group_Code2>) groupcodeback2.getReturnData();
						if (Group_statusmsg2 == 1) {
							Group_Code2 groupcode = grouplsit.get(0);
							String requestcode = groupcode.getPk_group_code();
							mSlidingMenu_requestcode_code.setText("邀请码:"+ requestcode);
							isCode = true;
						}
					} else {
						GroupCodeback groupcodeback = GsonUtils.parseJson(A,GroupCodeback.class);
						int Group_statusmsg = groupcodeback.getStatusMsg();
						if (Group_statusmsg == 1) {
							String requestcode = groupcodeback.getReturnData();
							mSlidingMenu_requestcode_code.setText("邀请码:"+ requestcode);
							isCode = true;
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

		sildingInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				Group_Readalluser_List.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("SlidingActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							Group_Readalluser_List.add(readAllUser);
						}
						SdPkUser.setGetGroup_ReadAllUser(Group_Readalluser_List);//把小组中的所有成员List传给小组添加成员界面
					}
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}
	private void initSlidingMenuUI() {
		mSlidingMenu_Team_Setting = (RelativeLayout) findViewById(R.id.SlidingMenu_Team_Setting);
		mSlidingMenu_Team_Setting.setOnClickListener(this);// 跳转至小组设置
		mSlidingMenu_requestcode = (RelativeLayout) findViewById(R.id.SlidingMenu_requestcode);
		mSlidingMenu_requestcode.setOnClickListener(this);// 生成小组邀请码
		mSlidingMenu_requestcode_code = (TextView) findViewById(R.id.SlidingMenu_requestcode_code);// 显示所生成的邀请码
		mSlidingMenu_member_listView = (ListView) findViewById(R.id.SlidingMenu_member_listView);// listview
		View mFooterView= View.inflate(SlidingActivity.this, R.layout.sliding_footer_item, null);
		RelativeLayout SlidingFooterLayout=(RelativeLayout) mFooterView.findViewById(R.id.SlidingFooterLayout);
		mSlidingMenu_member_listView.addFooterView(mFooterView);
		adapter = new MyMemBerAdapter();
		mSlidingMenu_member_listView.setAdapter(adapter);
		mSlidingMenu_member_listView.setOnItemClickListener(this);
	}

	private void initUI() {
		mSliding_OK_layout = (RelativeLayout) findViewById(R.id.Sliding_OK_layout);
		mSliding_OK_layout.setOnClickListener(this);
		findViewById(R.id.Sliding_back_layout).setOnClickListener(this);
		findViewById(R.id.Sliding_noshow_layout).setOnClickListener(this);
		findViewById(R.id.Sliding_show_layout).setOnClickListener(this);
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
					mGroupManager = group_ReadAllUser.getFk_user();
				} else {
					holder.Member_role.setText("成员");
				}

				String useravatar_path = group_ReadAllUser.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr+"mini-avatar";
				PreferenceUtils.saveImageCache(SlidingActivity.this,completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(SlidingActivity.this, completeURL, holder.Member_head);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Sliding_OK_layout:
			Sliding_OK_layout();
			break;
		case R.id.SlidingMenu_Team_Setting:
			SlidingMenu_Team_Setting();
			break;
		case R.id.SlidingMenu_requestcode:
			SlidingMenu_requestcode();
			break;
		case R.id.Sliding_back_layout:
			Sliding_back_layout();
			break;
		case R.id.Sliding_noshow_layout:
			Sliding_OK_layout();
			break;
		default:
			break;
		}
	}


	private void Sliding_back_layout() {
		MainClikc=true;
		finish();
		Intent intent = new Intent(SlidingActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	private void Sliding_OK_layout() {
		GroupActivity.getSliding.SlidingClick();//调用时改变字
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
		if(source){
			PersonalDataActivity.getClose.Close();
		}else {
			CommonFragment.getClose.Close();
		}
	}

	private void SlidingMenu_requestcode() {
		isClick=2;
		mSlidingMenu_Team_Setting.setBackgroundResource(R.color.white);
		mSlidingMenu_requestcode.setBackgroundResource(R.drawable.Sliding_choose_color);
		if (!isCode) {
			Group Group_teamsetting = new Group();
			Group_teamsetting.setPk_group(pk_group);
			sildingInterface.produceRequestCode(SlidingActivity.this,Group_teamsetting);
		}
	}

	private void SlidingMenu_Team_Setting() {
		isClick=1;
		mSlidingMenu_Team_Setting.setBackgroundResource(R.drawable.Sliding_choose_color);
		mSlidingMenu_requestcode.setBackgroundResource(R.color.white);
		Intent intent = new Intent(SlidingActivity.this,TeamSetting2Activity.class);
		intent.putExtra("GroupManager", mGroupManager);
		intent.putExtra(IConstant.Group, pk_group);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(MainClikc)
		{
			//退出当前小组后，在Sliding中的点击效果也要保存,而且重新读取小组中的所有用户
			SharedPreferences Sliding_sp=getSharedPreferences(IConstant.SlidingClick, 0);
			Editor editor=Sliding_sp.edit();
			editor.putInt(IConstant.Click, 0);
			editor.commit();

		}else
		{
			//退出当前小组后，在Sliding中的点击效果也要保存,而且重新读取小组中的所有用户
			SharedPreferences Sliding_sp=getSharedPreferences(IConstant.SlidingClick, 0);
			Editor editor=Sliding_sp.edit();
			editor.putInt(IConstant.Click, isClick);
			editor.commit();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Sliding_OK_layout();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SdPkUser.setGetSource(3);
		Group_ReadAllUser group_ReadAllUser = Group_Readalluser_List.get(position);
		boolean isOpen=SdPkUser.GetOpen;
		if(isOpen){
			PersonalDataActivity.getClose.Close();
			PersonalDataActivity.getRefreshData.RefreshData(group_ReadAllUser);
		}else {
			GroupActivity.getSliding.SlidingClick();//打开好友信息界面时改变字
			SdPkUser.setGroup_ReadAllUser(group_ReadAllUser);
			Intent intent=new Intent(SlidingActivity.this, PersonalDataActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.leftin_item, R.anim.leftout_item);
		}
		finish();
	}
	
	public interface GetReadGroupMember{
		void ReadGroupMember();
	}
}
