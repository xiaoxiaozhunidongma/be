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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.GridViewWithHeaderAndFooter;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.Interface.userJoin2gourpListenner;
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
public class HomeFragment extends Fragment implements OnClickListener , SwipeRefreshLayout.OnRefreshListener{

	private View mLayout;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private GridViewWithHeaderAndFooter home_gridview;
	private List<Group> users;
	private ArrayList<Group> list = new ArrayList<Group>();
	private ArrayList<Group> Codelist = new ArrayList<Group>();
	private MyGridviewAdapter adapter;
	private Interface homeInterface;
	private boolean iscode;
	private Integer fk_group;
	private Group readhomeuser;

	private MyReceiver receiver;
	private boolean refresh;
	private SwipeRefreshLayout swipeLayout;
	
	private String fileName = getSDPath() + "/" + "saveData";
	private String pk_user;
	private boolean isRegistered_one;
	private boolean login;
	private LayoutInflater layoutInflater;
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

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			layoutInflater=inflater;
			mLayout = inflater.inflate(R.layout.fragment_home, container, false);
			//提供gridview做布局判断
			SharedPreferences sp = getActivity().getSharedPreferences("Registered", 0);
			isRegistered_one = sp.getBoolean("isRegistered_one", false);
			SharedPreferences sp1 = getActivity().getSharedPreferences("isLogin", 0);
			login = sp1.getBoolean("Login", false);
			
			//获取sd卡中的pk_user
			FileInputStream fis;
			try {
				fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Person person = (Person) ois.readObject();
				pk_user = person.pk_user;
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
			IntentFilter filter = new IntentFilter();
			filter.addAction("isRefresh");
			receiver = new MyReceiver();
			getActivity().registerReceiver(receiver, filter);
			if (!iscode) {
				initNewTeam();
			}
			

			swipeLayout = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh);
			swipeLayout.setOnRefreshListener(this);
			
			// 顶部刷新的样式
			swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
					android.R.color.holo_blue_bright, android.R.color.holo_orange_light);

		}
		return mLayout;
	}


	public void prepareData(Integer pk_user) {
		ReadTeam(pk_user);
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			refresh = intent.getBooleanExtra("refresh", false);
			iscode = intent.getBooleanExtra("isCode", false);
			if (!iscode) {
				if (refresh) {
					list.clear();
					initNewTeam();
					adapter.notifyDataSetChanged();
					Log.e("HomeFragment", "有接受到广播123456789======");
				}
			} else {
				readhomeuser = (Group) intent
						.getSerializableExtra("readhomeuser");
				fk_group = readhomeuser.getPk_group();
				Log.e("HomeFragment", "使用邀请码添加后的fk_group======" + fk_group);
				Group_User group_User = new Group_User();
				group_User.setFk_group(fk_group);
				group_User.setFk_user(Integer.valueOf(pk_user));
				group_User.setRole(2);
				group_User.setStatus(1);
				homeInterface.userJoin2gourp(getActivity(), group_User);
				initNewTeam();
				Codelist.add(0, readhomeuser);
				adapter.notifyDataSetChanged();
			}
		}

	}

	public void initNewTeam() {
		ReadTeam(Integer.valueOf(pk_user));
	}

	private void ReadTeam(int pk_user) {
		homeInterface = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				if (iscode) {
					Codelist.clear();
					Groupback homeback = GsonUtils
							.parseJson(A, Groupback.class);
					int homeStatusMsg = homeback.getStatusMsg();
					if (homeStatusMsg == 1) {
						Log.e("HomeFragment", "读取用户小组信息2===" + A);
						users = homeback.getReturnData();
						if (users.size() > 0) {
							for (int i = 0; i < users.size(); i++) {
								Group readhomeuser_1 = users.get(i);
								Log.e("HomeFragment", "readhomeuser==="
										+ readhomeuser_1.getPk_group());
								Codelist.add(readhomeuser_1);
							}
							Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="
									+ Codelist.toString());
						}
					}
					adapter.notifyDataSetChanged();

				} else {
					if (refresh) {
						list.clear();
						Groupback homeback = GsonUtils.parseJson(A,
								Groupback.class);
						int homeStatusMsg = homeback.getStatusMsg();
						if (homeStatusMsg == 1) {
							Log.e("HomeFragment", "读取用户小组信息2===" + A);
							users = homeback.getReturnData();
							if (users.size() > 0) {
								for (int i = 0; i < users.size(); i++) {
									Group readhomeuser_1 = users.get(i);
									Log.e("HomeFragment", "readhomeuser==="
											+ readhomeuser_1.getPk_group());
									list.add(readhomeuser_1);
								}
								Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="
										+ list.toString());
							}
						}
						adapter.notifyDataSetChanged();

					} else {
						LoginActivity.list.clear();
						Groupback homeback = GsonUtils.parseJson(A,
								Groupback.class);
						int homeStatusMsg = homeback.getStatusMsg();
						if (homeStatusMsg == 1) {
							Log.e("HomeFragment", "读取用户小组信息2===" + A);
							users = homeback.getReturnData();
							if (users.size() > 0) {
								for (int i = 0; i < users.size(); i++) {
									Group readhomeuser_1 = users.get(i);
									Log.e("HomeFragment", "readhomeuser==="
											+ readhomeuser_1.getPk_group());
									LoginActivity.list.add(readhomeuser_1);
								}
								Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="
										+ LoginActivity.list.toString());
							}
						}
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		homeInterface.setPostListener(new userJoin2gourpListenner() {

			@Override
			public void success(String A) {
				Log.e("HomeFragment", "读取用户小组信息使用邀请码添加后的===" + A);
				adapter.notifyDataSetChanged();
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
				if (iscode) {
					if (Codelist.size() == arg2) {
						Intent intent = new Intent(getActivity(),
								NewteamActivity.class);
						startActivity(intent);
					} else {
						Group group = Codelist.get(arg2);
						int pk_group = group.getPk_group();
						Intent intent = new Intent(getActivity(),
								GroupActivity.class);
						intent.putExtra("pk_group", pk_group);
						Log.e("HomeFragment", "pk_group1111111111" + pk_group);
						startActivity(intent);
					}
				} else {
					if (refresh) {
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
							Log.e("HomeFragment", "pk_group2222222222" + pk_group);
							startActivity(intent);
						}

					} else {
						if (LoginActivity.list.size() == arg2) {
							Intent intent = new Intent(getActivity(),
									NewteamActivity.class);
							startActivity(intent);
						} else {
							Group group = LoginActivity.list.get(arg2);
							int pk_group = group.getPk_group();
							Intent intent = new Intent(getActivity(),
									GroupActivity.class);
							intent.putExtra("pk_group", pk_group);
							Log.e("HomeFragment", "pk_group333333333" + pk_group);
							startActivity(intent);
						}
					}
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
			if (iscode) {
				return (Codelist.size() + 1);
			} else {
				if (refresh) {
					return (list.size() + 1);
				} else {
					return (LoginActivity.list.size() + 1);
				}
			}
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
			
			if (iscode) {
				if (isRegistered_one) {
					if (Codelist.size() > 0) {
						if (position < Codelist.size()) {
								inflater = layoutInflater.inflate(
										R.layout.home_gridview_item, null);
							home_item_head = (ImageView) inflater
									.findViewById(R.id.home_item_head);
							home_item_name = (TextView) inflater
									.findViewById(R.id.home_item_name);
							Group homeuser_gridview = Codelist.get(position);
							String homeAvatar_path = homeuser_gridview
									.getAvatar_path();
							String homenickname = homeuser_gridview.getName();
							home_item_name.setText(homenickname);
							completeURL = beginStr + homeAvatar_path + endStr;
							PreferenceUtils.saveImageCache(getActivity(),
									completeURL);
							homeImageLoaderUtils.getInstance().LoadImage(
									getActivity(), completeURL, home_item_head);
						} else {
								inflater = layoutInflater.inflate(
										R.layout.home_teamadd_item, null);
						}
					} else {
							inflater = layoutInflater.inflate(
									R.layout.home_teamadd_item, null);
					}
				} else {
					if (position < Codelist.size()) {
							inflater = layoutInflater.inflate(
									R.layout.home_gridview_item, null);
						home_item_head = (ImageView) inflater
								.findViewById(R.id.home_item_head);
						home_item_name = (TextView) inflater
								.findViewById(R.id.home_item_name);
						Group homeuser_gridview = Codelist.get(position);
						String homeAvatar_path = homeuser_gridview
								.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr;
						PreferenceUtils.saveImageCache(getActivity(),
								completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(
								getActivity(), completeURL, home_item_head);

					} else {
							inflater = layoutInflater.inflate(
									R.layout.home_teamadd_item, null);
					}
				}

			} else {
				if (refresh) {
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
								String homeAvatar_path = homeuser_gridview
										.getAvatar_path();
								String homenickname = homeuser_gridview
										.getName();
								home_item_name.setText(homenickname);
								completeURL = beginStr + homeAvatar_path
										+ endStr;
								PreferenceUtils.saveImageCache(getActivity(),
										completeURL);
								homeImageLoaderUtils.getInstance().LoadImage(
										getActivity(), completeURL,
										home_item_head);
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
							String homeAvatar_path = homeuser_gridview
									.getAvatar_path();
							String homenickname = homeuser_gridview.getName();
							home_item_name.setText(homenickname);
							completeURL = beginStr + homeAvatar_path + endStr;
							PreferenceUtils.saveImageCache(getActivity(),
									completeURL);
							homeImageLoaderUtils.getInstance().LoadImage(
									getActivity(), completeURL, home_item_head);

						} else {
								inflater = layoutInflater.inflate(
										R.layout.home_teamadd_item, null);
						}
					}
				} else {
					if (isRegistered_one) {
						if (LoginActivity.list.size() > 0) {
							if (position < LoginActivity.list.size()) {
									inflater = layoutInflater.inflate(
											R.layout.home_gridview_item, null);
								home_item_head = (ImageView) inflater
										.findViewById(R.id.home_item_head);
								home_item_name = (TextView) inflater
										.findViewById(R.id.home_item_name);
								Group homeuser_gridview = LoginActivity.list
										.get(position);
								String homeAvatar_path = homeuser_gridview
										.getAvatar_path();
								String homenickname = homeuser_gridview
										.getName();
								home_item_name.setText(homenickname);
								completeURL = beginStr + homeAvatar_path
										+ endStr;
								PreferenceUtils.saveImageCache(getActivity(),
										completeURL);
								homeImageLoaderUtils.getInstance().LoadImage(
										getActivity(), completeURL,
										home_item_head);
							} else {
									inflater = layoutInflater.inflate(
											R.layout.home_teamadd_item, null);
							}
						} else {
								inflater = layoutInflater.inflate(
										R.layout.home_teamadd_item, null);
						}
					} else {
						inflater = layoutInflater.inflate(
								R.layout.home_gridview_item, null);
						if (position < LoginActivity.list.size()) {
							home_item_head = (ImageView) inflater
									.findViewById(R.id.home_item_head);
							home_item_name = (TextView) inflater
									.findViewById(R.id.home_item_name);
							Group homeuser_gridview = LoginActivity.list
									.get(position);
							String homeAvatar_path = homeuser_gridview
									.getAvatar_path();
							String homenickname = homeuser_gridview.getName();
							home_item_name.setText(homenickname);
							completeURL = beginStr + homeAvatar_path + endStr;
							PreferenceUtils.saveImageCache(getActivity(),
									completeURL);
							homeImageLoaderUtils.getInstance().LoadImage(
									getActivity(), completeURL, home_item_head);

						} else {
								inflater = layoutInflater.inflate(
										R.layout.home_teamadd_item, null);
						}
					}
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
				swipeLayout.setRefreshing(false);
				adapter.notifyDataSetChanged();
			}
		}, 5000);
	}
}
