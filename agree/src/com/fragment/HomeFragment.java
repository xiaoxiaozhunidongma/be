package com.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.GridViewWithHeaderAndFooter;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.biju.function.NewteamActivity;
import com.biju.function.RequestCodeActivity;
import com.biju.login.PhoneLoginActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {

	private View mLayout;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private GridViewWithHeaderAndFooter home_gridview;
	private List<Group> users;
	private ArrayList<Group> list = new ArrayList<Group>();
	private MyGridviewAdapter adapter;
	private Interface homeInterface;
	private SwipeRefreshLayout swipeLayout;

	private Integer SD_pk_user;

	private String fileName = getSDPath() + "/" + "saveData";

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	public HomeFragment() {
	}

	@SuppressLint("InlinedApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_home, container, false);

			FileInputStream fis;
			try {
				fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Person person = (Person) ois.readObject();
				SD_pk_user = person.pk_user;
				SdPkUser.setsD_pk_user(SD_pk_user);
				ois.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			initUI(inflater);
			adapter.notifyDataSetChanged();
			initNewTeam();

			swipeLayout = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh);
			swipeLayout.setOnRefreshListener(this);

			// 顶部刷新的样式
			swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);

		}
		Log.e("HomeFragment", "进入了onCreateView()=========");
		return mLayout;
	}

	@Override
	public void onStart() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Person person = (Person) ois.readObject();
			SD_pk_user = person.pk_user;
			SdPkUser.setsD_pk_user(SD_pk_user);
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Log.e("HomeFragment", "进入了onStart()========");

		list.clear();
		initNewTeam();
		adapter.notifyDataSetChanged();
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.e("HomeFragment", "进入了onResume()========");
		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		boolean refresh = requestcode_sp.getBoolean(IConstant.Refresh, false);
		if (refresh) {
			list.clear();
			initNewTeam();
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}

//	public void prepareData(Integer pk_user) {
//		ReadTeam(pk_user);
//	}

	public void initNewTeam() {
		ReadTeam(SD_pk_user);
	}

	private void ReadTeam(int pk_user) {
		homeInterface = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				PhoneLoginActivity.list.clear();
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("HomeFragment", "读取用户小组信息222222222===" + A);
					users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser_1 = users.get(i);
							Log.e("HomeFragment", "readhomeuser==="+ readhomeuser_1.getPk_group());
							PhoneLoginActivity.list.add(readhomeuser_1);
						}
						Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="+ PhoneLoginActivity.list.toString());
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
		home_gridview = (GridViewWithHeaderAndFooter) mLayout.findViewById(R.id.home_gridview);
		home_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除gridview点击后的背景颜色

		home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (PhoneLoginActivity.list.size() == arg2) {
					Intent intent = new Intent(getActivity(),NewteamActivity.class);
					startActivity(intent);
				} else {
					Group group = PhoneLoginActivity.list.get(arg2);
					int pk_group = group.getPk_group();
					Intent intent = new Intent(getActivity(),GroupActivity.class);
					intent.putExtra(IConstant.HomePk_group, pk_group);
					Log.e("HomeFragment", "pk_group333333333" + pk_group);
					startActivity(intent);
				}

			}
		});

		View view = inflater.inflate(R.layout.home_gridview_foot, null);
		TextView home_requestcode = (TextView) view.findViewById(R.id.home_requestcode);
		home_gridview.addFooterView(view);
		home_requestcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),RequestCodeActivity.class);
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
			return (PhoneLoginActivity.list.size() + 1);

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
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();
			if(PhoneLoginActivity.list.size()==0)
			{
				inflater = layoutInflater.inflate(R.layout.home_teamadd_item, null);
				Log.e("HomeFragment", "PhoneLoginActivity.list.size()=======" + PhoneLoginActivity.list.size());
			}else
			{
				if (position < PhoneLoginActivity.list.size()) {
					inflater = layoutInflater.inflate(R.layout.home_gridview_item, null);
					home_item_head = (ImageView) inflater.findViewById(R.id.home_item_head);
					home_item_name = (TextView) inflater.findViewById(R.id.home_item_name);
					Group homeuser_gridview = PhoneLoginActivity.list.get(position);
					String homeAvatar_path = homeuser_gridview.getAvatar_path();
					String homenickname = homeuser_gridview.getName();
					home_item_name.setText(homenickname);
					completeURL = beginStr + homeAvatar_path + endStr;
					PreferenceUtils.saveImageCache(getActivity(), completeURL);
					homeImageLoaderUtils.getInstance().LoadImage(getActivity(),
							completeURL, home_item_head);
					
				} else {
					inflater = layoutInflater.inflate(R.layout.home_teamadd_item, null);
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

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				list.clear();
				initNewTeam();
				adapter.notifyDataSetChanged();
				swipeLayout.setRefreshing(false);
			}
		}, 3000);
	}
}
