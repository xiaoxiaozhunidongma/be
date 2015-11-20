package com.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.ChatManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.FooterView;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.Interface.readUserListenner;
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
@TargetApi(19)
public class HomeFragment extends Fragment implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {

	private View mLayout;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private GridView home_gridview;
	private List<Group> users;
	private MyGridviewAdapter adapter;
	private Interface homeInterface;
	private SwipeRefreshLayout swipeLayout;
	private SwipeRefreshLayout swipe_refresh_1;

	private Integer SD_pk_user = null;
	private int EvenNumber;
	private int Size;
	private FooterView footerView;

	private String fileName = getSDPath() + "/" + "saveData";
	private boolean sdcard;
	private boolean refresh;
	private ImageView home_item_head;
	private TextView home_item_name;
	private RelativeLayout mHome_NoTeam_prompt_layout;
	private int columnWidth;
	private String currUserUrl;
	private HashMap<Integer, String> FromAvaUrlMap=new HashMap<Integer, String>();
	private int pk_group;
	private String group_name;
	private Group group;
	public static  User readuser;
	private boolean changeName;
	private boolean isSwipe;

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
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
			
			DisplayMetrics();// 获取屏幕的高度和宽度
			
			Intent intent = getActivity().getIntent();
			sdcard = intent.getBooleanExtra(IConstant.Sdcard, false);
			initUI(inflater);

			swipeLayout = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh);
			swipeLayout.setOnRefreshListener(this);
			swipe_refresh_1 = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh_1);
			swipe_refresh_1.setOnRefreshListener(this);

			// 顶部刷新的样式
			swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);
			swipe_refresh_1.setColorSchemeResources(android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);

			Log.e("HomeFragment", "进入了onCreateView()=========" + sdcard);
			if (sdcard) {
				InputSdcard();
				Log.e("HomeFragment", "进入了onStart()中的input里了========"+ SD_pk_user);
				ReadTeamInterface(SD_pk_user);
			}

		}
		return mLayout;
	}

	private void DisplayMetrics() {
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		columnWidth = (width - 30) / 2;
		int height = metric.heightPixels; // 屏幕高度（像素）
		Log.e("HomeFragment", "屏幕宽度（像素）width=======" + width);
		Log.e("HomeFragment", "屏幕高度（像素）height=======" + height);
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
		boolean exitteam = SdPkUser.RefreshTeam;
		if (exitteam) {
			InputSdcard();
			ReadTeamInterface(SD_pk_user);
		}
		super.onStart();
	}

	
	@Override
	public void onResume() {
		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		refresh = requestcode_sp.getBoolean(IConstant.Refresh, false);
		if (refresh) {
			// 获取SD卡中的pk_user
			SD_pk_user = SdPkUser.getsD_pk_user();
			ReadTeamInterface(SD_pk_user);
		}
		
		SharedPreferences ChangeName_sp=getActivity().getSharedPreferences("ChangeGroupName", 0);
		changeName = ChangeName_sp.getBoolean("ChangeName", false);
		Log.e("HomeFragment", "得到的changeName====="+changeName);
		if(changeName){
			// 获取SD卡中的pk_user
			SD_pk_user = SdPkUser.getsD_pk_user();
			ReadTeamInterface(SD_pk_user);
		}
		
		
		readCurUser();//读取当前用户
		readAlluserOfgroup();
		super.onResume();
	}

	private void ReadTeamInterface(int pk_user) {
		homeInterface = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new readUserGroupMsgListenner() {

			private Group readhomeuser_1;

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
							readhomeuser_1 = users.get(i);
							Log.e("HomeFragment", "readhomeuser==="+ readhomeuser_1.getPk_group());
							PhoneLoginActivity.list.add(readhomeuser_1);
						}

						// 赋值长度
						EvenNumber = PhoneLoginActivity.list.size() % 2;
						Size = PhoneLoginActivity.list.size();
						Log.e("HomeFragment", "读取用户小组信息加入List后的内容==="+ PhoneLoginActivity.list.toString());
						if (PhoneLoginActivity.list.size() > 0) {
							isSwipe=true;
							swipeLayout.setVisibility(View.VISIBLE);
							swipe_refresh_1.setVisibility(View.GONE);
							mHome_NoTeam_prompt_layout.setVisibility(View.GONE);
							home_gridview.setVisibility(View.VISIBLE);
						}
					}
					adapter.notifyDataSetChanged();
				} else {
					isSwipe=false;
					swipeLayout.setVisibility(View.GONE);
					swipe_refresh_1.setVisibility(View.VISIBLE);
					mHome_NoTeam_prompt_layout.setVisibility(View.VISIBLE);
					home_gridview.setVisibility(View.GONE);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void readAlluserOfgroup() {
		homeInterface = Interface.getInstance();
		homeInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				FromAvaUrlMap.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("HomeFragment", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							String avatar_path = beginStr+readAllUser.getAvatar_path()+endStr+"mini-avatar";
							FromAvaUrlMap.put(readAllUser.getFk_user(), avatar_path);
						}
					}
					SdPkUser.setHomeClickUser(allUsers);//把容器传到成员列表界面
					SdPkUser.setGetSource(1);//传个true说明是群聊的
					
					Intent intent = new Intent(getActivity(),GroupActivity.class);
					intent.putExtra(IConstant.HomePk_group, pk_group);
					intent.putExtra(IConstant.HomeGroupName, group_name);
					intent.putExtra("conName", "group_name");
					intent.putExtra("FromAvaUrlMap", FromAvaUrlMap);//数组
					intent.putExtra("convid", group.getEm_id());
					intent.putExtra("CurrUserUrl", currUserUrl);
					startActivity(intent);
					Log.e("CommentsListActivity", "有点击到==========");
				}
				
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void readCurUser() {
		homeInterface.setPostListener(new readUserListenner() {
			


			@Override
			public void success(String A) {
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// 取第一个Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						readuser = Users.get(0);
						String mAvatar_path = readuser.getAvatar_path();
						currUserUrl = beginStr + mAvatar_path + endStr+"mini-avatar";
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		
		User user = new User();
		user.setPk_user(SD_pk_user);
		homeInterface.readUser(getActivity(), user);
	}

	private void initUI(LayoutInflater inflater) {
		mLayout.findViewById(R.id.HomeRequestCodeLayout).setOnClickListener(this);
		mHome_NoTeam_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.Home_NoTeam_prompt_layout);//没有小组时显示
		mLayout.findViewById(R.id.tab_home_new_layout).setOnClickListener(this);// 新建小组
		mLayout.findViewById(R.id.tab_home_new).setOnClickListener(this);// 新建小组
		home_gridview = (GridView) mLayout.findViewById(R.id.home_gridview);
		home_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除gridview点击后的背景颜色

		// 是否滑动时候暂停加载
		home_gridview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		home_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (EvenNumber == 0) {
					if (!(PhoneLoginActivity.list.size() == arg2)) {
						Log.e("HomeFragment", "点击到了小组的==========");
						group = PhoneLoginActivity.list.get(arg2);
						
						joinConv(group);//加入到对话
						pk_group = group.getPk_group();
						
						Group readAllPerRelation_group = new Group();
						readAllPerRelation_group.setPk_group(pk_group);
						homeInterface.readAllPerRelation(getActivity(),readAllPerRelation_group);
						
						group_name = group.getName();
//						Intent intent = new Intent(getActivity(),GroupActivity.class);
//						intent.putExtra(IConstant.HomePk_group, pk_group);
//						intent.putExtra(IConstant.HomeGroupName, group_name);
//						intent.putExtra("conName", "group_name");
//						intent.putExtra("FromAvaUrlList", FromAvaUrlList);//数组
//						intent.putExtra("convid", group.getEm_id());
//						intent.putExtra("CurrUserUrl", currUserUrl);
//						startActivity(intent);
					}
				} else {
					if (!(PhoneLoginActivity.list.size() == arg2)) {
						group = PhoneLoginActivity.list.get(arg2);
						joinConv(group);//加入到对话
						pk_group = group.getPk_group();
						
						Group readAllPerRelation_group = new Group();
						readAllPerRelation_group.setPk_group(pk_group);
						homeInterface.readAllPerRelation(getActivity(),readAllPerRelation_group);
						
						group_name = group.getName();
//						Intent intent = new Intent(getActivity(),GroupActivity.class);
//						intent.putExtra(IConstant.HomePk_group, pk_group);
//						intent.putExtra(IConstant.HomeGroupName, group_name);
//						intent.putExtra("conName", "group_name");
//						intent.putExtra("FromAvaUrlList", FromAvaUrlList);//数组
//						intent.putExtra("convid", group.getEm_id());
//						intent.putExtra("CurrUserUrl", currUserUrl);
//						startActivity(intent);
					}
				}

			}

			private void joinConv(final Group group) {
				final Integer SD_pk_user = SdPkUser.getsD_pk_user();
				AVIMClient curUser = AVIMClient.getInstance(String.valueOf(SD_pk_user));
				curUser.open(new AVIMClientCallback(){
					@Override
					public void done(AVIMClient client, AVIMException e) {
					      if(e==null){
						        //登录成功
					    	  String em_id = group.getEm_id();
						        AVIMConversation conversation = client.getConversation(em_id);
						        //注册对话
								final ChatManager chatManager = ChatManager.getInstance();
								 chatManager.registerConversation(conversation);//注册对话
								 conversation.join(new AVIMConversationCallback(){
						            @Override
						            public void done(AVIMException e){
						              if(e==null){
						              //加入成功
						            	  Log.e("HomeFragment", "当前："+SD_pk_user+"加入小组成功");
						              }
						            }
						        });
						      }
						    }
				});

			}
		});
		adapter = new MyGridviewAdapter();
		home_gridview.setAdapter(adapter);
	}

	@SuppressWarnings("deprecation")
	private int getDisplayWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	class MyGridviewAdapter extends BaseAdapter {

		private RelativeLayout home_item_name_layout;

		@Override
		public int getCount() {
			if (EvenNumber == 0) {
				return (Size + 1);
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
				if (position == PhoneLoginActivity.list.size()) {
					if (footerView == null) {
						footerView = new FooterView(parent.getContext());
						GridView.LayoutParams pl = new GridView.LayoutParams(getDisplayWidth((getActivity())),LayoutParams.WRAP_CONTENT);
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
						GridView.LayoutParams pl = new GridView.LayoutParams(getDisplayWidth((getActivity())),LayoutParams.WRAP_CONTENT);
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
			inflater = layoutInflater.inflate(R.layout.home_gridview_item, null);
			home_item_head = (ImageView) inflater.findViewById(R.id.home_item_head);
			home_item_name = (TextView) inflater.findViewById(R.id.home_item_name);
			home_item_name_layout = (RelativeLayout) inflater.findViewById(R.id.home_item_name_layout);
			if (position == PhoneLoginActivity.list.size()) {
				home_item_head.setVisibility(View.INVISIBLE);
				home_item_name.setVisibility(View.INVISIBLE);
				home_item_name_layout.setVisibility(View.INVISIBLE);
			}
			int px2dip_left_1 = DensityUtil.dip2px(getActivity(), 10);
			int px2dip_left_2 = DensityUtil.dip2px(getActivity(), 5);
			int px2dip_top_1 = DensityUtil.dip2px(getActivity(), 10);
			int px2dip_top_2 = DensityUtil.dip2px(getActivity(), 10);
			int px2dip_right_1 = DensityUtil.dip2px(getActivity(), 5);
			int px2dip_right_2 = DensityUtil.dip2px(getActivity(), 10);

			// 设置图片的位置
			MarginLayoutParams margin9 = new MarginLayoutParams(home_item_head.getLayoutParams());
			int item_number = position % 2;
			switch (item_number) {
			case 0:
				margin9.setMargins(px2dip_left_1, px2dip_top_1, px2dip_right_1,0);
				break;
			case 1:
				margin9.setMargins(px2dip_left_2, px2dip_top_2, px2dip_right_2,0);
				break;
			default:
				break;
			}
			RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
			layoutParams9.height = columnWidth;// 设置图片的高度
			layoutParams9.width = columnWidth; // 设置图片的宽度
			home_item_head.setLayoutParams(layoutParams9);

			if (EvenNumber == 0) {
				if (PhoneLoginActivity.list.size() > 0) {
					if (position < PhoneLoginActivity.list.size()) {
						Group homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr+ "group-front-cover";
						PreferenceUtils.saveImageCache(getActivity(),completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(getActivity(), completeURL, home_item_head);
					}
				}
			} else {
				if (PhoneLoginActivity.list.size() > 0) {
					if (position < PhoneLoginActivity.list.size()) {
						Group homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr+ "group-front-cover";
						PreferenceUtils.saveImageCache(getActivity(),completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(getActivity(), completeURL, home_item_head);
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
		case R.id.HomeRequestCodeLayout:
			HomeRequestCodeLayout();
			break;
		default:
			break;
		}
	}

	private void HomeRequestCodeLayout() {
		Intent intent = new Intent(getActivity(),RequestCodeActivity.class);
		startActivity(intent);
	}

	private void tab_home_new_layout() {
		SharedPreferences TeamFriends_sp=getActivity().getSharedPreferences("TeamFriends", 0);
		Editor editor=TeamFriends_sp.edit();
		editor.putBoolean("AddTeamFriends", false);
		editor.commit();
		Intent intent = new Intent(getActivity(), NewteamActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SdPkUser.setRefreshTeam(false);// 有对小组进行修改过后传false
		sdcard = false;
		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		Editor editor = requestcode_sp.edit();
		editor.putBoolean(IConstant.Refresh, false);
		editor.commit();

		SharedPreferences ChangeName_sp=getActivity().getSharedPreferences("ChangeGroupName", 0);
		Editor ChangeName_editor=ChangeName_sp.edit();
		ChangeName_editor.putBoolean("ChangeName", false);
		ChangeName_editor.commit();
		
		// 清除缓存
		if (PhoneLoginActivity.list.size() > 0) {
			Drawable d = home_item_head.getDrawable();
			if (d != null)
				d.setCallback(null);
			home_item_head.setImageDrawable(null);
		}

		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ReadTeamInterface(SD_pk_user);
				if(isSwipe){
					swipeLayout.setRefreshing(false);
				}else {
					swipe_refresh_1.setRefreshing(false);
				}
			}
		}, 3000);
	}
}
