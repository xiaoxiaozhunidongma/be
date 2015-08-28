package com.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.AsynImageLoader;
import com.BJ.utils.FooterView;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

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
	private GridView home_gridview;
	private List<Group> users;
	private MyGridviewAdapter adapter;
	private Interface homeInterface;
	private SwipeRefreshLayout swipeLayout;

	private Integer SD_pk_user=null;
	private int EvenNumber;
	private int Size;
	private FooterView footerView;

	private String fileName = getSDPath() + "/" + "saveData";
	private boolean sdcard;
	private boolean refresh;
	private ImageView home_item_head;
	private TextView home_item_name;

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

			Intent intent = getActivity().getIntent();
			sdcard = intent.getBooleanExtra(IConstant.Sdcard, false);
			initUI(inflater);

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

	private void InputSdcard() {
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
	}

	@Override
	public void onStart() {
		if(sdcard)
		{
			InputSdcard();
			Log.e("HomeFragment", "进入了onStart()中的input里了========");
		}
		Log.e("HomeFragment", "进入了onStart()========");
		initNewTeam();
		adapter.notifyDataSetChanged();
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.e("HomeFragment", "进入了onResume()========");
		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		refresh = requestcode_sp.getBoolean(IConstant.Refresh, false);
		if (refresh) {
			initNewTeam();
			adapter.notifyDataSetChanged();
			Log.e("HomeFragment", "进入了onResume()的refresh========"+refresh);
		}
		super.onResume();
	}

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
						// 赋值长度
						EvenNumber = PhoneLoginActivity.list.size() % 2;
						Size = PhoneLoginActivity.list.size();
						Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="+ PhoneLoginActivity.list.toString());
						if (PhoneLoginActivity.list.size() > 0) {
							home_gridview.setAdapter(adapter);
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
		home_gridview = (GridView) mLayout.findViewById(R.id.home_gridview);
		home_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除gridview点击后的背景颜色
		home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (EvenNumber == 0) {
					if (PhoneLoginActivity.list.size() == arg2) {
						Intent intent = new Intent(getActivity(),NewteamActivity.class);
						startActivity(intent);
						Log.e("HomeFragment", "进入了新建小组==========");
					} else if (PhoneLoginActivity.list.size() + 1 == arg2) {
						Log.e("HomeFragment", "点击到了最后一个不能点击的==========");
					} else if (PhoneLoginActivity.list.size() + 2 == arg2) {
						Log.e("HomeFragment", "点击到了邀请码的这个==========");
					} else {
						Log.e("HomeFragment", "点击到了小组的==========");
						Group group = PhoneLoginActivity.list.get(arg2);
						int pk_group = group.getPk_group();
						Intent intent = new Intent(getActivity(),GroupActivity.class);
						intent.putExtra(IConstant.HomePk_group, pk_group);
						startActivity(intent);
					}
				} else {
					if (PhoneLoginActivity.list.size() == arg2) {
						Intent intent = new Intent(getActivity(),NewteamActivity.class);
						startActivity(intent);
					} else {
						Group group = PhoneLoginActivity.list.get(arg2);
						int pk_group = group.getPk_group();
						Intent intent = new Intent(getActivity(),GroupActivity.class);
						intent.putExtra(IConstant.HomePk_group, pk_group);
						startActivity(intent);
					}
				}

			}
		});

		adapter = new MyGridviewAdapter();
	}

	@SuppressWarnings("deprecation")
	private int getDisplayWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	class MyGridviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (EvenNumber == 0) {
				return (Size + 3);
			} else {
				return (Size + 2);
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

			if (EvenNumber == 0) {
				if (position == PhoneLoginActivity.list.size() + 2) {
					if (footerView == null) {
						footerView = new FooterView(parent.getContext());
						GridView.LayoutParams pl = new GridView.LayoutParams(
								getDisplayWidth((getActivity())),
								LayoutParams.WRAP_CONTENT);
						footerView.setLayoutParams(pl);
						footerView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(),RequestCodeActivity.class);
								startActivity(intent);
							}
						});
					}
					return footerView;
				}
			} else {
				if (position == PhoneLoginActivity.list.size() + 1) {
					if (footerView == null) {
						footerView = new FooterView(parent.getContext());
						GridView.LayoutParams pl = new GridView.LayoutParams(
								getDisplayWidth((getActivity())),
								LayoutParams.WRAP_CONTENT);
						footerView.setLayoutParams(pl);
						footerView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(),RequestCodeActivity.class);
								startActivity(intent);
							}
						});
					}
					return footerView;
				}
			}

			View inflater = null;
				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				if(PhoneLoginActivity.list.size()==0)
				{
					inflater = layoutInflater.inflate(R.layout.home_teamadd_item, null);
				}else
				{
					if (position < PhoneLoginActivity.list.size()) {
						inflater = layoutInflater.inflate(R.layout.home_gridview_item, null);
						home_item_head = (ImageView) inflater.findViewById(R.id.home_item_head);
						home_item_name = (TextView) inflater.findViewById(R.id.home_item_name);
						Log.e("HomeFragment", "进入inflater11111111111========");
					} else if (position == PhoneLoginActivity.list.size()) {
						inflater = layoutInflater.inflate(R.layout.home_teamadd_item, null);
						Log.e("HomeFragment", "进入inflater2222222222========");
					} else {
						inflater = layoutInflater.inflate(R.layout.home_teamadd_item_1, null);
						Log.e("HomeFragment", "进入inflater33333333333========");
					}
				}
			if (EvenNumber == 0) {
				if(PhoneLoginActivity.list.size()>0)
				{
					if (position < PhoneLoginActivity.list.size()) {
						Group homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr;
						PreferenceUtils.saveImageCache(getActivity(), completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(getActivity(),
								completeURL, home_item_head);
					} else if (position == PhoneLoginActivity.list.size()) {
					} else {
						
					}
				}
			} else {
				if(PhoneLoginActivity.list.size()>0)
				{
					if (position < PhoneLoginActivity.list.size()) {
						Group homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr;
						PreferenceUtils.saveImageCache(getActivity(), completeURL);
//						homeImageLoaderUtils.getInstance().LoadImage(getActivity(),
//								completeURL, holder.home_item_head);
						AsynImageLoader asynImageLoader = new AsynImageLoader();
						asynImageLoader.showImageAsyn(home_item_head, completeURL, R.drawable.newteam,getActivity());
					} else {
						
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

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		sdcard=false;
		
		SharedPreferences requestcode_sp=getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		Editor editor=requestcode_sp.edit();
		editor.putBoolean(IConstant.Refresh, false);
		editor.commit();
		
		//清除缓存
		homeImageLoaderUtils.clearCache();
		Drawable d = home_item_head.getDrawable();  
		if (d != null) d.setCallback(null);  
		home_item_head.setImageDrawable(null);  
		home_item_head.setBackgroundDrawable(null);
		
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initNewTeam();
				adapter.notifyDataSetChanged();
				swipeLayout.setRefreshing(false);
			}
		}, 3000);
	}
}
