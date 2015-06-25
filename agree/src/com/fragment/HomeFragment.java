package com.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Group;
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

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater
					.inflate(R.layout.fragment_home, container, false);
			initUI(inflater);
			initNewTeam();
			adapter.notifyDataSetChanged();
			IntentFilter filter = new IntentFilter();
			filter.addAction("isRefresh");
			MyReceiver receiver = new MyReceiver();
			getActivity().registerReceiver(receiver, filter);
		}
		return mLayout;
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String refresh = intent.getStringExtra("refresh");
			if ("ok".equals(refresh)) {
				list.clear();
				initNewTeam();
				adapter.notifyDataSetChanged();
				Log.e("HomeFragment", "有接受到广播");
			}
		}

	}

	private void initNewTeam() {
		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
				0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		Log.e("HomeFragment", "isRegistered_one===" + isRegistered_one);
		if (isRegistered_one) {

		} else {
			int pk_user = LoginActivity.pk_user;
			ReadTeam(pk_user);
		}
	}

	private void ReadTeam(int pk_user) {
		Interface homeInterface = new Interface();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("HomeFragment", "读取用户小组信息2===" + A);
					users = homeback.getReturnData();
					Log.e("HomeFragment", "users的长度===" + users.size());
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
				int getcount = home_gridview.getCount();
				if ((getcount - 3) == arg2) {
					Intent intent = new Intent(getActivity(),
							NewteamActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							GroupActivity.class);
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
			Log.e("HomeFragment", "position  1=====" + position);
			View inflater = null;
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			int pos = home_gridview.getCount();
			Log.e("HomeFragment", "pos=====" + pos);
			if (position < pos - 3) {
				inflater = layoutInflater.inflate(R.layout.home_gridview_item,
						null);
				home_item_head = (ImageView) inflater
						.findViewById(R.id.home_item_head);
				home_item_name = (TextView) inflater
						.findViewById(R.id.home_item_name);
				if (list.size() > 0) {
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
				}

			} else {
				inflater = layoutInflater.inflate(R.layout.home_teamadd_item,
						null);
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
