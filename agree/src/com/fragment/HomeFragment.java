package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.GridViewWithHeaderAndFooter;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.biju.function.NewteamActivity;
import com.biju.function.RequestCodeActivity;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements OnClickListener {

	private View mLayout;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private GridViewWithHeaderAndFooter home_gridview;
	private List<Group> users;
	private ArrayList<Group> list = new ArrayList<Group>();
	private MyGridviewAdapter adapter;
	private boolean isTeam;
	private boolean isRegistered_one;
	private int returndata;
	private boolean login;
	private Interface homeInterface;
	private boolean iscode;
	private Integer fk_group;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater
					.inflate(R.layout.fragment_home, container, false);
			SharedPreferences sp = getActivity().getSharedPreferences(
					"Registered", 0);
			isRegistered_one = sp.getBoolean("isRegistered_one", false);
			Log.e("HomeFragment", "isRegistered_one===" + isRegistered_one);
			returndata = sp.getInt("returndata", returndata);
			SharedPreferences sp1 = getActivity().getSharedPreferences(
					"isLogin", 0);
			login = sp1.getBoolean("Login", false);

			initUI(inflater);
			adapter.notifyDataSetChanged();
			IntentFilter filter = new IntentFilter();
			filter.addAction("isRefresh");
			MyReceiver receiver = new MyReceiver();
			getActivity().registerReceiver(receiver, filter);
			if(!iscode)
			{
				initNewTeam();
			}
		}
		return mLayout;
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String refresh = intent.getStringExtra("refresh");
			iscode = intent.getBooleanExtra("isCode", false);
			Group readhomeuser = (Group) intent
					.getSerializableExtra("readhomeuser");
			fk_group = readhomeuser.getPk_group();
			Log.e("HomeFragment", "使用邀请码添加后的fk_group======" + fk_group);
			if (!iscode) {
				if ("ok".equals(refresh)) {
					list.clear();
					initNewTeam();
					adapter.notifyDataSetChanged();
					Log.e("HomeFragment", "有接受到广播");
				}
			} else {
//				list.add(0, readhomeuser);
//				initNewTeam();
				Group_User group_User = new Group_User();
				group_User.setFk_group(fk_group);
				if (isRegistered_one) {
					group_User.setFk_user(returndata);
				} else {
					if (login) {
						int pk_user = LoginActivity.pk_user;
						group_User.setFk_user(pk_user);
						Log.e("HomeFragment", "使用邀请码添加后的pk_user======" + pk_user);
					} else {
						group_User.setFk_user(returndata);
					}
				}
				group_User.setRole(2);
				group_User.setRole(1);
				group_User.setStatus(1);
				homeInterface.userJoin2gourp(getActivity(), group_User);
				list.add(0, readhomeuser);
				initNewTeam();
				adapter.notifyDataSetChanged();
			}
		}

	}

	private void initNewTeam() {
		if (isRegistered_one) {
			ReadTeam(returndata);
//			Log.e("HomeFragment", "进入注册的新建小组");
		} else {
			if (login) {
				int pk_user = LoginActivity.pk_user;
				ReadTeam(pk_user);
//				Log.e("HomeFragment", "进入登录的新建小组=======" + pk_user);
			} else {
				ReadTeam(returndata);
			}
		}
	}

	private void ReadTeam(int pk_user) {
		homeInterface = new Interface();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				if (!iscode) {
					Groupback homeback = GsonUtils
							.parseJson(A, Groupback.class);
					int homeStatusMsg = homeback.getStatusMsg();
					if (homeStatusMsg == 1) {
						Log.e("HomeFragment", "读取用户小组信息2===" + A);
						users = homeback.getReturnData();
//						Log.e("HomeFragment", "users的长度===" + users.size());
						if (users.size() > 0) {
							for (int i = 0; i < users.size(); i++) {
								Group readhomeuser = users.get(i);
								Log.e("HomeFragment", "readhomeuser==="
										+ readhomeuser);
								list.add(readhomeuser);
							}
						}
						adapter.notifyDataSetChanged();
					}
				} else {
					Log.e("HomeFragment", "读取用户小组信息使用邀请码添加后的===" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI(LayoutInflater inflater) {
		mLayout.findViewById(R.id.tab_home_new_layout).setOnClickListener(this);// 新建小组
		mLayout.findViewById(R.id.tab_home_new).setOnClickListener(this);// 新建小组
		home_gridview = (GridViewWithHeaderAndFooter) mLayout
				.findViewById(R.id.home_gridview);
		home_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除gridview点击后的背景颜色

		home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Log.e("HomeFragment", "(list.size()+1)==="+(list.size()+1));
				// Log.e("HomeFragment", "arg2==="+arg2);
				if (list.size() == arg2) {
					Intent intent = new Intent(getActivity(),
							NewteamActivity.class);
					startActivity(intent);
				} else {
					Group group = list.get(arg2);
					int pk_group = group.getPk_group();
					Intent intent = new Intent(getActivity(),
							GroupActivity.class);
					intent.putExtra("pk_group", pk_group);
					Log.e("HomeFragment", "pk_group" + pk_group);
					startActivity(intent);
				}
			}
		});

		View view = inflater.inflate(R.layout.home_gridview_foot, null);
		TextView home_requestcode = (TextView) view
				.findViewById(R.id.home_requestcode);
		home_gridview.addFooterView(view);
		home_requestcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						RequestCodeActivity.class);
				startActivity(intent);

			}
		});

		adapter = new MyGridviewAdapter();
		home_gridview.setAdapter(adapter);
	}

	class MyGridviewAdapter extends BaseAdapter {

		private ImageView home_item_head;
		private TextView home_item_name;

		@Override
		public int getCount() {
			return (list.size() + 1);
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
			// Log.e("HomeFragment", "position  1=====" + position);
			// Log.e("HomeFragment", "(list.size() + 1)=====" + (list.size() +
			// 1));
			// Log.e("HomeFragment", "(list.size())=====" + list.size());
			View inflater = null;
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			if (isRegistered_one) {
				if (list.size() > 0) {
					if (position < list.size()) {
						inflater = layoutInflater.inflate(
								R.layout.home_gridview_item, null);
						home_item_head = (ImageView) inflater
								.findViewById(R.id.home_item_head);
						home_item_name = (TextView) inflater
								.findViewById(R.id.home_item_name);
						Group homeuser_gridview = list.get(position);
						Log.e("HomeFragment", "position  2=====" + position);
						String homeAvatar_path = homeuser_gridview
								.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr;
						PreferenceUtils.saveImageCache(getActivity(),
								completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(
								getActivity(), completeURL, home_item_head);
						Log.e("HomeFragment", "进这来1");
					} else {
						inflater = layoutInflater.inflate(
								R.layout.home_teamadd_item, null);
					}
				} else {
					inflater = layoutInflater.inflate(
							R.layout.home_teamadd_item, null);
				}
			} else {
				if (position < list.size()) {
					inflater = layoutInflater.inflate(
							R.layout.home_gridview_item, null);
					home_item_head = (ImageView) inflater
							.findViewById(R.id.home_item_head);
					home_item_name = (TextView) inflater
							.findViewById(R.id.home_item_name);
					Group homeuser_gridview = list.get(position);
					Log.e("HomeFragment", "position  2=====" + position);
					String homeAvatar_path = homeuser_gridview.getAvatar_path();
					String homenickname = homeuser_gridview.getName();
					home_item_name.setText(homenickname);
					completeURL = beginStr + homeAvatar_path + endStr;
					PreferenceUtils.saveImageCache(getActivity(), completeURL);
					homeImageLoaderUtils.getInstance().LoadImage(getActivity(),
							completeURL, home_item_head);
					Log.e("HomeFragment", "进这来1");

				} else {
					inflater = layoutInflater.inflate(
							R.layout.home_teamadd_item, null);
				}
			}
			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_home_new_layout:
		case R.id.tab_home_new:
			tab_home_new_layout();
			break;

		default:
			break;
		}
	}

	private void tab_home_new_layout() {
		Intent intent = new Intent(getActivity(), NewteamActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}
}
