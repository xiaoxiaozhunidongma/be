package com.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.AVIMTypedMessagesArrayCallback;
import leanchatlib.controller.ChatManager;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.activeandroid.query.Select;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery.CachePolicy;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.biju.Interface;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.Interface.readUserListenner;
import com.biju.R;
import com.biju.chatroom.AddChatsActivity;
import com.example.testleabcloud.ChatActivityLean;
import com.github.volley_examples.utils.GsonUtils;

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
	// private ArrayList<ReadUserAllFriends> AllFriends_List = new
	// ArrayList<ReadUserAllFriends>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private static MyAdapter adapter;
	private Integer fk_user_from;

	private Integer SD_pk_user;
	private String CurrUserUrl;
	private List<AVIMConversation> convs = new ArrayList<AVIMConversation>();
	private Interface instance;
	private HashMap<Integer, String> FromAvaUrlMap = new HashMap<Integer, String>();
	public static HashMap<Integer, User> AllFriendsMap = new HashMap<Integer, User>();

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
			// LoginHuanXin();
			// initInterface();
			initFindUserlistener();// 这个改为数据库的比较好！！！！！！！！！！
			ReadUserAllFriends();// 暂时没用
			readAllfriends();// 读取我的所有好友
			mFriends_swipe_refresh = (SwipeRefreshLayout) mLayout
					.findViewById(R.id.friends_swipe_refresh);
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

	private void readAllfriends() {
		User user = new User();
		Integer getsD_pk_user = SdPkUser.getsD_pk_user();
		user.setPk_user(getsD_pk_user);
		instance.readMyAllfriend(getActivity(), user);
		instance.setPostListener(new MyAllfriendsListenner() {

			@Override
			public void success(String A) {
				Log.e("AddFriends3Activity", "返回结果" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				Integer status222 = loginbackread.getStatusMsg();
				if (1 == status222) {
					List<User> userList = loginbackread.getReturnData();
					// 查表所有数据
					List<User> DBuserList = new Select().from(User.class)
							.execute();
					for (int i = 0; i < userList.size(); i++) {
						boolean isInsert = true;// 默认为true
						User user = userList.get(i);
						AllFriendsMap.put(user.getPk_user(), user);

						Integer pk_user = user.getPk_user();
						String nickname = user.getNickname();
						String avatar_path = user.getAvatar_path();
						String phone = user.getPhone();
						String password = user.getPassword();
						String setup_time = user.getSetup_time();
						String last_login_time = user.getLast_login_time();
						String jpush_id = user.getJpush_id();
						Integer sex = user.getSex();
						String device_id = user.getDevice_id();
						Integer status = user.getStatus();
						float amount = user.getAmount();
						String wechat_id = user.getWechat_id();
						String real_name = user.getReal_name();

						for (int j = 0; j < DBuserList.size(); j++) {
							User Curuser = DBuserList.get(j);
							Integer pk_user2 = Curuser.getPk_user();
							if (String.valueOf(pk_user).equals(
									String.valueOf(pk_user2))) {
								// 先查后改
								User executeSingle = new Select().from(User.class).where("pk_user=?", pk_user).executeSingle();

								executeSingle.setPk_user(pk_user);
								executeSingle.setNickname(nickname);
								executeSingle.setAvatar_path(avatar_path);
								executeSingle.setPhone(phone);
								executeSingle.setPassword(password);
								executeSingle.setSetup_time(setup_time);
								executeSingle.setLast_login_time(last_login_time);
								executeSingle.setJpush_id(jpush_id);
								executeSingle.setSex(sex);
								executeSingle.setDevice_id(device_id);
								executeSingle.setStatus(status);
								executeSingle.setAmount(amount);
								executeSingle.setWechat_id(wechat_id);
								executeSingle.setReal_name(real_name);

								executeSingle.save();

								isInsert = false;
							}
						}

						if (isInsert) {
							User user2 = new User(pk_user, nickname,
									avatar_path, phone, password, setup_time,
									last_login_time, jpush_id, sex, device_id,
									status, amount, wechat_id, real_name);
							user2.save();
						}
						isInsert = true;// 默认可以插入数据

						adapter.notifyDataSetChanged();

					}

				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initFindUserlistener() {

		instance = Interface.getInstance();

		instance.setPostListener(new FindMultiUserListenner() {

			@Override
			public void success(String A) {
				Log.e("FriendsFragment", "查询多个用户返回成功！！！====" + A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					List<User> Users = findfriends_statusmsg.getReturnData();
					for (int i = 0; i < Users.size(); i++) {
						User user = Users.get(i);
						String avatar_path = beginStr + user.getAvatar_path()
								+ endStr + "mini-avatar";
						FromAvaUrlMap.put(user.getPk_user(), avatar_path);
					}
					SdPkUser.setUser((ArrayList<User>) Users);// 把容器传到成员列表界面
					SdPkUser.setGetSource(2);// 传个true说明是聊天室的
					// 查询完毕，异步更新头像
					ChatActivityLean.fromAvaUrlMapInter
							.AvaSuccess(FromAvaUrlMap);
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
						CurrUserUrl = beginStr + mAvatar_path + endStr
								+ "mini-avatar";
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
	// private void LoginHuanXin() {
	// Log.e("FriendsFragment~~~~~", "调用了LoginHuanXin（）");
	// String str_pkuser = String.valueOf(SD_pk_user);
	//
	// Log.e("FriendsFragment~~~~~", "Integer.valueOf(pk_user)" + SD_pk_user);
	//
	// if (!"".equals(str_pkuser)) {
	//
	// EMChatManager.getInstance().login(str_pkuser, "paopian",
	// new EMCallBack() {// 回调
	// @Override
	// public void onSuccess() {
	// EMGroupManager.getInstance().loadAllGroups();
	// EMChatManager.getInstance().loadAllConversations();
	// Log.e("FriendsFragment~~~~~", "登陆聊天服务器成功！~~~~");
	// }
	//
	// @Override
	// public void onProgress(int progress, String status) {
	// }
	//
	// @Override
	// public void onError(int code, String message) {
	// Log.d("FriendsFragment~~~~~~", "登陆聊天服务器失败！~~~~");
	// }
	// });
	// }
	// }

	@Override
	public void onStart() {
		// ReadUserAllFriends();//???????????????????????????????????????????
		QueryAllConv();// 查询对话
		super.onStart();
	}

	private void QueryAllConv() {
		final ArrayList<String> members = new ArrayList<String>();
		members.add(String.valueOf(SD_pk_user));
		AVIMClient tom = AVIMClient.getInstance(String.valueOf(SD_pk_user));
		tom.open(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient client, AVIMException e) {
				if (e == null) {
					// 登录成功
					AVIMConversationQuery query = client.getQuery();

					// 查询对话成员有 Bob 和 Jerry的Conversation
					// query.withMembers(members);
					query.containsMembers(members);
					// query.whereContains("m", String.valueOf(SD_pk_user));
					query.whereEqualTo("attr.type", 3);
					query.setQueryPolicy(CachePolicy.IGNORE_CACHE); // 设置缓存过期！！
																	// ！！！

					// query.limit(50);

					query.findInBackground(new AVIMConversationQueryCallback() {
						@Override
						public void done(List<AVIMConversation> convs,
								AVIMException e) {
							if (e == null) {
								Log.e("FriendsFragment", "进入这里111111=======");
								if (convs != null && !convs.isEmpty()) {
									Log.e("FriendsFragment", "进入这里33333=======");
									// 获取符合查询条件的Conversation列表
									Log.e("FriendsFragment", "查询所有对成功！！！！数量："
											+ convs.size());
									FriendsFragment.this.convs = convs;// 赋值
									mFriends_add_tishi_layout
											.setVisibility(View.GONE);
									mFriends_listview
											.setVisibility(View.VISIBLE);
									adapter.notifyDataSetChanged();
								} else {
									Log.e("FriendsFragment", "进入这里44444=======");
									mFriends_add_tishi_layout
											.setVisibility(View.VISIBLE);
									mFriends_listview.setVisibility(View.GONE);
								}
							} else {
								Log.e("FriendsFragment", "进入这里22222=======");
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
		// addFriends_interface.readFriend(getActivity(), user);
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_friends_addbuddy_layout)
				.setOnClickListener(this);
		mLayout.findViewById(R.id.tab_friends_addbuddy)
				.setOnClickListener(this);// 添加好友
		mFriends_add_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_layout);// 有好友的时候的布局
		mFriends_add_tishi_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_tishi_layout);// 没有好友的时候的提示布局
		TextView friends_add_Text = (TextView) mLayout
				.findViewById(R.id.friends_add_Text);
		friends_add_Text.setText("暂无对话信息  点击" + "\"添加\"" + "增加" + "\n"
				+ "新的对话信息");

		mFriends_listview = (ListView) mLayout
				.findViewById(R.id.friends_listview);// listview布局
		mFriends_listview.setDividerHeight(0);// 设置listview的item直接的间隙为0
		adapter = new MyAdapter();
		mFriends_listview.setOnItemClickListener(this);
		mFriends_listview.setAdapter(adapter);
	}

	public static void notifyList() {
		adapter.notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView PartyReadUserAllFriends_head;
		TextView PartyReadUserAllFriends_name;
		TextView PartyReadUserAllFriendsLine1;
		TextView PartyReadUserAllFriendsLine2;
		public TextView tv_menmberNum;
		public TextView tv_lastmsg;
	}

	class MyAdapter extends BaseAdapter {

		private String completeURL = "路径为空";

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
						R.layout.partyreaduserallfriends_item, null);
				holder.PartyReadUserAllFriends_head = (ImageView) inflater
						.findViewById(R.id.PartyReadUserAllFriends_head);
				holder.PartyReadUserAllFriends_name = (TextView) inflater
						.findViewById(R.id.PartyReadUserAllFriends_name);
				holder.PartyReadUserAllFriendsLine1 = (TextView) inflater
						.findViewById(R.id.PartyReadUserAllFriendsLine1);
				holder.PartyReadUserAllFriendsLine2 = (TextView) inflater
						.findViewById(R.id.PartyReadUserAllFriendsLine2);
				holder.tv_menmberNum = (TextView) inflater
						.findViewById(R.id.tv_menmberNum);
				holder.tv_lastmsg = (TextView) inflater
						.findViewById(R.id.tv_lastmsg);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			AVIMConversation avimConversation = convs.get(position);
			readMsg(avimConversation, holder.tv_lastmsg);
			String convName = "";
			List<String> members = avimConversation.getMembers();

			Object attribute = avimConversation.getAttribute("isupdate");
			log.e("isupdate", "isupdate现在的值==" + attribute);
			if (attribute != null) {
				if ("true".equals(String.valueOf(attribute))) {
					// 有更新过对话名字
					convName = avimConversation.getName();
				} else {
					// 没更新过
					convName = "";// 先清空
					for (int i = 0; i < members.size(); i++) {

						String string = members.get(i);
						if (members.size() != 2) {
							// 除了当前名字拼成的对话名
							if (!String.valueOf(SD_pk_user).equals(string)) {
								User user = AllFriendsMap.get(Integer
										.valueOf(string));
								if (user != null) {
									if (i != members.size() - 1) {
										convName = convName
												+ user.getNickname() + ",";
									}
									if (i == members.size() - 1) {
										convName = convName
												+ user.getNickname() + "的对话";
									}
								}
							}
						} else {
							if (!String.valueOf(SD_pk_user).equals(string)) {
								User user = AllFriendsMap.get(Integer
										.valueOf(string));
								if (user != null) {
									convName = user.getNickname();

								}
							}
						}
					}

				}
			}

			holder.PartyReadUserAllFriends_name.setText(convName);

			if (members.size() > 2) {
				holder.tv_menmberNum.setVisibility(View.VISIBLE);
				holder.tv_menmberNum.setText(String.valueOf(members.size()));
				holder.PartyReadUserAllFriends_head
						.setImageResource(R.drawable.chatroomhead);
			} else {
				holder.tv_menmberNum.setVisibility(View.GONE);
				for (int i = 0; i < members.size(); i++) {
					String string = members.get(i);
					if (!String.valueOf(SD_pk_user).equals(string)) {
						User user = AllFriendsMap.get(Integer.valueOf(string));
						Log.e("聊天记录头像",
								"YourFriendsMap.size==" + AllFriendsMap.size());
						if (user != null) {
							String avatar_path = user.getAvatar_path();
							Log.e("聊天记录头像", "avatar_path==" + avatar_path);
							completeURL = beginStr + avatar_path + endStr
									+ "mini-avatar";

						}
						Log.e("聊天记录头像", "completeURL==" + completeURL);
						ImageLoaderUtils.getInstance().LoadImageCricular(
								getActivity(), completeURL,
								holder.PartyReadUserAllFriends_head);

					}
				}
			}

			if (position == convs.size() - 1) {
				holder.PartyReadUserAllFriendsLine1.setVisibility(View.VISIBLE);
				holder.PartyReadUserAllFriendsLine2.setVisibility(View.GONE);
			} else {
				holder.PartyReadUserAllFriendsLine1.setVisibility(View.GONE);
				holder.PartyReadUserAllFriendsLine2.setVisibility(View.VISIBLE);
			}

			// holder.ReadUserAllFriends_head
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent(getActivity(),
			// FriendsDataActivity.class);
			// intent.putExtra(IConstant.ReadUserAllFriends,
			// allFriends);
			// intent.putExtra(IConstant.Fk_user_from,
			// fk_user_from);
			// startActivity(intent);
			// }
			// });

			return inflater;
		}

		private void readMsg(AVIMConversation avimConversation,
				final TextView tv_lastmsg) {
			ChatManager.getInstance().queryMessages(avimConversation, null,
					System.currentTimeMillis(), 1,
					new AVIMTypedMessagesArrayCallback() {

						@Override
						public void done(List<AVIMTypedMessage> typedMessages,
								AVException e) {
							if (typedMessages.size() <= 0) {
								tv_lastmsg.setText("无消息");
							} else {

								AVIMTypedMessage avimTypedMessage = typedMessages
										.get(0);
								AVIMReservedMessageType type = AVIMReservedMessageType
										.getAVIMReservedMessageType(avimTypedMessage
												.getMessageType());
								switch (type) {
								case TextMessageType:
									AVIMTextMessage textMsg = (AVIMTextMessage) avimTypedMessage;
									tv_lastmsg.setText(textMsg.getText());
									break;
								case ImageMessageType:
									tv_lastmsg.setText("[" + "图片" + "]");
									break;

								default:
									break;
								}

							}
						}
					});
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
		// Integer size = AllFriends_List.size();
		Intent intent = new Intent(getActivity(), AddChatsActivity.class);
		// intent.putExtra(IConstant.Size, size);
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
		if (convs.size() != 0) {
			AVIMConversation avimConversation = convs.get(position);
			checkConversation2(avimConversation);
		}
	}

	private void checkConversation2(final AVIMConversation avimConversation) {

		List<String> members = avimConversation.getMembers();
		String convName = avimConversation.getName();
		// for (int i = 0; i < members.size(); i++) {
		// //查找用户
		// User user = new User();
		// user.setPhone(members.get(i));
		// instance.findUser(getActivity(),user);
		// }
		instance.findMultiUsers(getActivity(), members);

		String creator = avimConversation.getCreator();
		Log.e("FriendsFragment", "创建者的ID=======" + creator);
		SdPkUser.setCreator(creator);// 传创建者ID给成员界面

		final ChatManager chatManager = ChatManager.getInstance();
		chatManager.registerConversation(avimConversation);// 注册对话

		Intent intent = new Intent(getActivity(), ChatActivityLean.class);
		intent.putExtra("conName", convName);
		intent.putExtra("FromAvaUrlMap", FromAvaUrlMap);// 数组//???此处异步还没返回之前容器为空
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
