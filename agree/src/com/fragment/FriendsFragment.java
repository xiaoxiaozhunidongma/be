package com.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.ChatManager;
import leanchatlib.model.ConversationType;
import leanchatlib.utils.LogUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.javabean.ReadUserAllFriendsback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.AVQuery.CachePolicy;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.biju.AddFriends3Activity;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.Interface.readFriendListenner;
import com.biju.R;
import com.biju.function.AddFriends2Activity;
import com.biju.function.FriendsDataActivity;
import com.biju.function.GroupActivity;
import com.biju.function.PartyDetailsActivity;
import com.example.huanxin.ChatActivity;
import com.example.testleabcloud.ChatActivityLean;
import com.github.volley_examples.utils.GsonUtils;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.findUserListenner;;

;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FriendsFragment extends Fragment implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	private View mLayout;
	private SwipeRefreshLayout mFriends_swipe_refresh;
	private RelativeLayout mFriends_add_layout;
	private RelativeLayout mFriends_add_tishi_layout;
	private ListView mFriends_listview;
	private Interface addFriends_interface;
//	private ArrayList<ReadUserAllFriends> AllFriends_List = new ArrayList<ReadUserAllFriends>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private MyAdapter adapter;
	private Integer fk_user_from;

	private Integer SD_pk_user;
	private String CurrUserUrl;
	private List<AVIMConversation> convs=new ArrayList<AVIMConversation>();
	private Interface instance;
	private HashMap<Integer, String> FromAvaUrlMap=new HashMap<Integer, String>();

	public FriendsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_friends, container,
					false);

			// 获取SD卡中的pk_user
			SD_pk_user = SdPkUser.getsD_pk_user();
			ReadUser();
			Log.e("HomeFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);

			initUI();
//			LoginHuanXin();
//			initInterface();
			initFindUserlistener();
			ReadUserAllFriends();
			mFriends_swipe_refresh = (SwipeRefreshLayout) mLayout.findViewById(R.id.friends_swipe_refresh);
			mFriends_swipe_refresh.setOnRefreshListener(this);

			// 顶部刷新的样式
			mFriends_swipe_refresh.setColorSchemeResources(
					android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);
		}
		return mLayout;
	}

	private void initFindUserlistener() {
		instance = Interface.getInstance();
//		instance.setPostListener(new findUserListenner() {
//			
//			@Override
//			public void success(String A) {
//				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
//						Loginback.class);
//				int statusmsg = findfriends_statusmsg.getStatusMsg();
//				if (statusmsg == 1) {
//					// 取第一个Users[0]
//					List<User> Users = findfriends_statusmsg.getReturnData();
//					if (Users.size() >= 1) {
//						User user = Users.get(0);
//						String avatar_path = beginStr+user.getAvatar_path()+endStr+"mini-avatar";
//						FromAvaUrlMap.put(user.getPk_user(), avatar_path);
//					}
//				} 
//
//			}
//			
//			@Override
//			public void defail(Object B) {
//				
//			}
//		});
		instance.setPostListener(new FindMultiUserListenner() {
			
			@Override
			public void success(String A) {
				Log.e("FriendsFragment", "查询多个用户返回成功！！！===="+ A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
				List<User> Users = findfriends_statusmsg.getReturnData();
					for (int i = 0; i < Users.size(); i++) {
					User user = Users.get(i);
					String avatar_path = beginStr+user.getAvatar_path()+endStr+"mini-avatar";
					FromAvaUrlMap.put(user.getPk_user(), avatar_path);
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void ReadUser() {
		Interface instance = Interface.getInstance();
		instance.setPostListener(new readUserListenner() {

	//需要异步？？？？？？？？？？？？？？？？？？？？？？？？？？？
//	private void LoginHuanXin() {
//		Log.e("FriendsFragment~~~~~", "调用了LoginHuanXin（）");
//		String str_pkuser = String.valueOf(SD_pk_user);
//		
//		Log.e("FriendsFragment~~~~~", "Integer.valueOf(pk_user)"+SD_pk_user);
//		
//		if(!"".equals(str_pkuser)){
//			
//			EMChatManager.getInstance().login(str_pkuser,
//					"paopian",new EMCallBack() {//回调
//				@Override
//				public void onSuccess() {
//					EMGroupManager.getInstance().loadAllGroups();
//					EMChatManager.getInstance().loadAllConversations();
//					Log.e("FriendsFragment~~~~~", "登陆聊天服务器成功！~~~~");		
//				}
//				
//				@Override
//				public void onProgress(int progress, String status) {
//				}
//				
//				@Override
//				public void onError(int code, String message) {
//					Log.d("FriendsFragment~~~~~~", "登陆聊天服务器失败！~~~~");
//				}
//			});
//		}
//	}

			@Override
			public void success(String A) {
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// 取第一个Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						String mAvatar_path = readuser.getAvatar_path();
						CurrUserUrl = beginStr + mAvatar_path + endStr+"mini-avatar";
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		User user = new User();
		user.setPk_user(SD_pk_user);
		instance.readUser(getActivity(), user);
	}


	// 需要异步？？？？？？？？？？？？？？？？？？？？？？？？？？？
//	private void LoginHuanXin() {
//		Log.e("FriendsFragment~~~~~", "调用了LoginHuanXin（）");
//		String str_pkuser = String.valueOf(SD_pk_user);
//
//		Log.e("FriendsFragment~~~~~", "Integer.valueOf(pk_user)" + SD_pk_user);
//
//		if (!"".equals(str_pkuser)) {
//
//			EMChatManager.getInstance().login(str_pkuser, "paopian",
//					new EMCallBack() {// 回调
//						@Override
//						public void onSuccess() {
//							EMGroupManager.getInstance().loadAllGroups();
//							EMChatManager.getInstance().loadAllConversations();
//							Log.e("FriendsFragment~~~~~", "登陆聊天服务器成功！~~~~");
//						}
//
//						@Override
//						public void onProgress(int progress, String status) {
//						}
//
//						@Override
//						public void onError(int code, String message) {
//							Log.d("FriendsFragment~~~~~~", "登陆聊天服务器失败！~~~~");
//						}
//					});
//		}
//	}

	@Override
	public void onStart() {
//		ReadUserAllFriends();//???????????????????????????????????????????
		QueryAllConv();//查询对话
		super.onStart();
	}

	private void QueryAllConv() {
		final ArrayList<String> members=new ArrayList<String>();
		members.add(String.valueOf(SD_pk_user));
		AVIMClient tom = AVIMClient.getInstance(String.valueOf(SD_pk_user));
		tom.open(new AVIMClientCallback(){

		    @Override
		    public void done(AVIMClient client,AVIMException e){
		      if(e==null){
		      //登录成功
		      AVIMConversationQuery query = client.getQuery();

		      //查询对话成员有 Bob 和 Jerry的Conversation
//		      query.withMembers(members);
		      query.containsMembers(members);
//		      query.whereContains("m", String.valueOf(SD_pk_user));
		      query.whereEqualTo("attr.type", 3);
		      query.setQueryPolicy(CachePolicy.IGNORE_CACHE);        //设置缓存过期！！ ！！！
		      
//		      query.limit(50);

		      query.findInBackground(new AVIMConversationQueryCallback(){
		        @Override
		        public void done(List<AVIMConversation> convs,AVIMException e){
		          if(e==null){
		              if(convs!=null && !convs.isEmpty()){
		                //获取符合查询条件的Conversation列表
		            	  Log.e("FriendsFragment", "查询所有对成功！！！！数量："+convs.size());
		            	  FriendsFragment.this.convs=convs;//赋值
		            	  adapter.notifyDataSetChanged();
		            	  
		              }
		          }
		        }
		      });
		      }
		    }
		});

	}

	private void ReadUserAllFriends() {
		User user = new User();
		user.setPk_user(SD_pk_user);
		fk_user_from = SD_pk_user;
//		addFriends_interface.readFriend(getActivity(), user);
	}

//	private void initInterface() {
//		addFriends_interface = Interface.getInstance();
//		addFriends_interface.setPostListener(new readFriendListenner() {
//
//			@Override
//			public void success(String A) {
//				AllFriends_List.clear();
//				ReadUserAllFriendsback readUserAllFriendsback = GsonUtils
//						.parseJson(A, ReadUserAllFriendsback.class);
//				int status = readUserAllFriendsback.getStatusMsg();
//				if (status == 1) {
//					Log.e("FriendsFragment", "返回用户的所有已添加的好友========" + A);
//					List<ReadUserAllFriends> readUserAllFriendslist = readUserAllFriendsback
//							.getReturnData();
//					if (readUserAllFriendslist.size() > 0) {
//						for (int i = 0; i < readUserAllFriendslist.size(); i++) {
//							ReadUserAllFriends userAllFriends = readUserAllFriendslist
//									.get(i);
//							AllFriends_List.add(userAllFriends);
//						}
//					}
//					if (AllFriends_List.size() > 0) {
//						mFriends_add_layout.setVisibility(View.VISIBLE);
//						mFriends_add_tishi_layout.setVisibility(View.GONE);
//						mFriends_listview.setAdapter(adapter);
//					} else {
//						mFriends_add_layout.setVisibility(View.GONE);
//						mFriends_add_tishi_layout.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//
//			@Override
//			public void defail(Object B) {
//			}
//		});
//	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_friends_addbuddy_layout)
				.setOnClickListener(this);
		mLayout.findViewById(R.id.tab_friends_addbuddy)
				.setOnClickListener(this);// 添加好友
		mFriends_add_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_layout);// 有好友的时候的布局
		mFriends_add_tishi_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_tishi_layout);// 没有好友的时候的提示布局
		mFriends_listview = (ListView) mLayout
				.findViewById(R.id.friends_listview);// listview布局
		mFriends_listview.setDividerHeight(0);// 设置listview的item直接的间隙为0
		adapter = new MyAdapter();
		mFriends_listview.setOnItemClickListener(this);
		mFriends_listview.setAdapter(adapter);
	}

	class ViewHolder {
		ImageView ReadUserAllFriends_head;
		TextView ReadUserAllFriends_name;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return convs.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflater = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getActivity()
						.getLayoutInflater();
				inflater = layoutInflater.inflate(
						R.layout.readuserallfriends_item, null);
				holder.ReadUserAllFriends_head = (ImageView) inflater
						.findViewById(R.id.ReadUserAllFriends_head);
				holder.ReadUserAllFriends_name = (TextView) inflater
						.findViewById(R.id.ReadUserAllFriends_name);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			
			AVIMConversation avimConversation = convs.get(position);
			String convName = avimConversation.getName();
			holder.ReadUserAllFriends_name.setText(convName);
			
			String avatar_path = "http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu";
			String completeURL = beginStr + avatar_path + endStr+"mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(getActivity(),
					"http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu", holder.ReadUserAllFriends_head);

//			holder.ReadUserAllFriends_head
//					.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							Intent intent = new Intent(getActivity(),
//									FriendsDataActivity.class);
//							intent.putExtra(IConstant.ReadUserAllFriends,
//									allFriends);
//							intent.putExtra(IConstant.Fk_user_from,
//									fk_user_from);
//							startActivity(intent);
//						}
//					});

			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_friends_addbuddy_layout:
		case R.id.tab_friends_addbuddy:
			tab_friends_addbuddy();
			break;

		default:
			break;
		}
	}

	private void tab_friends_addbuddy() {
//		Integer size = AllFriends_List.size();
		Intent intent = new Intent(getActivity(), AddFriends3Activity.class);
//		intent.putExtra(IConstant.Size, size);
		getActivity().overridePendingTransition(R.anim.tab_left_in_item,
				R.anim.tab_left_out_item);
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ReadUserAllFriends();
				mFriends_swipe_refresh.setRefreshing(false);
				adapter.notifyDataSetChanged();
			}
		}, 3000);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(convs.size()!=0){
			AVIMConversation avimConversation = convs.get(position);
			checkConversation2(avimConversation);
		}
	}

	private void checkConversation2(final AVIMConversation avimConversation) {
		
		List<String> members = avimConversation.getMembers();
			String convName=avimConversation.getName();
//			for (int i = 0; i < members.size(); i++) {
//				//查找用户
//				User user = new User();
//				user.setPhone(members.get(i));
//				instance.findUser(getActivity(),user);
//			}
			instance.findMultiUsers(getActivity(), members);
			
  			final ChatManager chatManager = ChatManager.getInstance();
				 chatManager.registerConversation(avimConversation);//注册对话
			
			Intent intent = new Intent(getActivity(),ChatActivityLean.class);
			intent.putExtra("conName", convName);
			intent.putExtra("FromAvaUrlMap", FromAvaUrlMap);//数组//???此处异步还没返回之前容器为空
			intent.putExtra("convid", avimConversation.getConversationId());
			intent.putExtra("CurrUserUrl", CurrUserUrl);
			startActivity(intent);
	}
	

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

}
