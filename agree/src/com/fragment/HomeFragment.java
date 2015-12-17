package com.fragment;

import java.util.ArrayList;
import java.util.List;

import leanchatlib.controller.ChatManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.BJ.javabean.GroupHome;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.JPush;
import com.BJ.javabean.JPushNoSee;
import com.BJ.javabean.JPushTabNumber;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.ExampleUtil;
import com.BJ.utils.FooterView;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.InitPkUser;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.ToastUtils;
import com.BJ.utils.homeImageLoaderUtils;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
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
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private GridView home_gridview;
	private List<GroupHome> users;
	private MyGridviewAdapter adapter;
	private Interface homeInterface;
	private SwipeRefreshLayout swipeLayout;
	private SwipeRefreshLayout swipe_refresh_1;

	private Integer init_pk_user = null;
	private int EvenNumber;
	private int Size;
	private FooterView footerView;

	private boolean sdcard;
	private boolean refresh;
	private ImageView home_item_head;
	private TextView home_item_name;
	private RelativeLayout mHome_NoTeam_prompt_layout;
	private int columnWidth;
	private String currUserUrl;
	private int pk_group;
	private String group_name;
	private GroupHome group;
	public static User readuser;
	private boolean changeName;
	private RelativeLayout mHomeNoWIFI;

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static boolean isForeground = false;
	private ImageView mHomePromptImage;

	private List<Integer> JpushList = new ArrayList<Integer>();
	private List<JPush> JpushList1 = new ArrayList<JPush>();
	private List<JPushTabNumber> JPushTabNumberList = new ArrayList<JPushTabNumber>();
	private List<JPushNoSee> JPushNoSeeList = new ArrayList<JPushNoSee>();
	private Integer party_update_number = 0;

	public HomeFragment() {
	}

	@SuppressLint("InlinedApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_home, container, false);
			DisplayMetrics();// ��ȡ��Ļ�ĸ߶ȺͿ��

			Intent intent = getActivity().getIntent();
			sdcard = intent.getBooleanExtra(IConstant.Sdcard, false);
			initUI(inflater);

			swipeLayout = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh);
			swipeLayout.setOnRefreshListener(this);
			swipe_refresh_1 = (SwipeRefreshLayout) mLayout.findViewById(R.id.swipe_refresh_1);
			swipe_refresh_1.setOnRefreshListener(this);

			// ����ˢ�µ���ʽ
			swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);
			swipe_refresh_1.setColorSchemeResources(
					android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);

			if (sdcard) {
				init_pk_user = InitPkUser.InitPkUser();
				Log.e("HomeFragment", "�з�װ����������pk_user=====" + init_pk_user);
				// ReadTeamInterface(init_pk_user);
			}
			registerMessageReceiver();
			Log.e("HomeFragment", "onCreateView()=======");
		}
		return mLayout;
	}

	// ��ȡ��Ļ���
	private void DisplayMetrics() {
		com.BJ.utils.DisplayMetrics.DisplayMetrics(getActivity());
		int width = com.BJ.utils.DisplayMetrics.Width();
		columnWidth = (width - 30) / 2;
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String extras = intent.getStringExtra(KEY_EXTRAS);
				setCostomMsg(extras);
			}
		}
	}

	private void setCostomMsg(String extras) {
		Log.e("HomeFragment", "�õ���msg=====" + extras);
		JpushList1.clear();
		JpushList.clear();
		if (!ExampleUtil.isEmpty(extras)) {
			try {
				JSONObject extraJson = new JSONObject(extras);
				if (null != extraJson && extraJson.length() > 0) {
					JPush jPush = GsonUtils.parseJson(extras, JPush.class);
					Integer jPush_pk_group = jPush.getPk_group();
					Integer jPush_type_tag = jPush.getType_tag();
					if (2 == Integer.valueOf(jPush_type_tag)) {
						// �ٲ��±�,Ȼ��ɾ�������
						JPushTabNumberList = new Select().from(JPushTabNumber.class).execute();
						for (int i = 0; i < JPushTabNumberList.size(); i++) {
							JPushTabNumber jPushTabNumber = JPushTabNumberList.get(i);
							Integer jPushTab_pk_group = jPushTabNumber.getPk_group();
							if (String.valueOf(jPushTab_pk_group).equals(String.valueOf(jPush_pk_group))) {
								new Delete().from(JPushTabNumber.class).where("pk_group=?", jPushTab_pk_group).execute();
							}
						}

						ReadTeamInterface(init_pk_user);//���¶���ȡ����

						adapter.notifyDataSetChanged();
					}
				}
			} catch (JSONException e) {

			}
		}

	}

	private void SendBroadcastReceiver(boolean one, Integer party_update) {
		Intent intent = new Intent();
		intent.setAction("JPush");
		intent.putExtra("JPushTabNumber", one);
		if (one) {
			intent.putExtra("OneNumber", party_update);
		}
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	public void onStart() {
		Log.e("HomeFragment", "onStart()=======");
		WIFIChange();// �ж�WIFI�ĸı���в�����ʾ�ĸı�
		boolean exitteam = SdPkUser.RefreshTeam;
		if (exitteam) {
			ReadTeamInterface(init_pk_user);
		}
		ReadTeamInterface(init_pk_user);
		super.onStart();
	}

	private void WIFIChange() {
		boolean isWIFI = Ifwifi.getNetworkConnected(getActivity());// �ж��Ƿ�������
		// ������ʱ
		if (isWIFI) {
			mHomeNoWIFI.setVisibility(View.GONE);
			home_gridview.setVisibility(View.VISIBLE);
		} else {
			mHomeNoWIFI.setVisibility(View.VISIBLE);
			home_gridview.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		isForeground = true;
		WIFIChange();// �ж�WIFI�ĸı���в�����ʾ�ĸı�

		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		refresh = requestcode_sp.getBoolean(IConstant.Refresh, false);
		if (refresh) {
			// ��ȡpk_user
			init_pk_user = InitPkUser.InitPkUser();
			ReadTeamInterface(init_pk_user);
		}

		SharedPreferences ChangeName_sp = getActivity().getSharedPreferences("ChangeGroupName", 0);
		changeName = ChangeName_sp.getBoolean("ChangeName", false);
		if (changeName) {
			// ��ȡpk_user
			init_pk_user = InitPkUser.InitPkUser();
			ReadTeamInterface(init_pk_user);
		}

		readCurUser();// ��ȡ��ǰ�û�
		readAlluserOfgroup();
		super.onResume();
	}

	private void ReadTeamInterface(int pk_user) {
		homeInterface = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new readUserGroupMsgListenner() {

			private GroupHome readhomeuser_1;

			@Override
			public void success(String A) {
				PhoneLoginActivity.list.clear();
				JpushList1.clear();
				JpushList.clear();
				new Delete().from(JPush.class).execute();// ÿ�ζ�֮ǰ�Ȱ����ݿ�ɾ��

				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("HomeFragment", "��ȡ�û�С����Ϣ222222222===" + A);
					users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							readhomeuser_1 = users.get(i);
							PhoneLoginActivity.list.add(readhomeuser_1);
							Integer jPush_pk_group = readhomeuser_1.getPk_group();
							Integer party_update = readhomeuser_1.getParty_update();
							// ������ݿ�
							JPush jPush2 = new JPush(jPush_pk_group, 2,party_update);
							jPush2.save();
						}

						// ��ֵ����
						EvenNumber = PhoneLoginActivity.list.size() % 2;
						Size = PhoneLoginActivity.list.size();
						Log.e("HomeFragment", "��ȡ�û�С����Ϣ����List�������==="+ PhoneLoginActivity.list.toString());
						if (PhoneLoginActivity.list.size() > 0) {
							swipeLayout.setVisibility(View.VISIBLE);
							swipe_refresh_1.setVisibility(View.GONE);
							mHome_NoTeam_prompt_layout.setVisibility(View.GONE);
							home_gridview.setVisibility(View.VISIBLE);
						}
					}
					// ���
					JpushList1 = new Select().from(JPush.class).execute();
					Log.e("HomeFragment","JpushList1�ĳ���=======" + JpushList1.size());
					for (int i = 0; i < JpushList1.size(); i++) {
						JPush jPush3 = JpushList1.get(i);
						Integer jpush_pk_group = jPush3.getPk_group();
						JpushList.add(jpush_pk_group);

						Integer party_update = jPush3.getParty_update();
						party_update_number = party_update_number+ party_update;
						Log.e("HomeFragment", "����õ���party_update_number======="+ party_update_number + "========" + i);
						Log.e("HomeFragment", "ÿ�ε�party_update_number======="+ jPush3.getParty_update() + "========" + i);
					}
					SendBroadcastReceiver(true, party_update_number);// ���͹㲥��mainactivity,
					party_update_number = 0;//���¸�ֵΪ0
					Log.e("HomeFragment", "���͹�ȥ��party_update_number======="+ party_update_number);

					adapter.notifyDataSetChanged();
				} else {
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
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("HomeFragment", "��ȡ��С���е������û�========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							String avatar_path = beginStr+ readAllUser.getAvatar_path() + endStr+ "mini-avatar";
						}
					}
					SdPkUser.setHomeClickUser(allUsers);// ������������Ա�б����
					SdPkUser.setGetSource(1);// ����true˵����Ⱥ�ĵ�

					Intent intent = new Intent(getActivity(),GroupActivity.class);
					intent.putExtra(IConstant.HomePk_group, pk_group);
					intent.putExtra(IConstant.HomeGroupName, group_name);
					intent.putExtra("conName", "group_name");
					// intent.putExtra("FromAvaUrlMap", FromAvaUrlMap);//����
					intent.putExtra("convid", group.getEm_id());
					intent.putExtra("CurrUserUrl", currUserUrl);
					startActivity(intent);
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
				Loginback loginbackread = GsonUtils.parseJson(A,Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// ȡ��һ��Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						readuser = Users.get(0);
						String mAvatar_path = readuser.getAvatar_path();
						currUserUrl = beginStr + mAvatar_path + endStr+ "mini-avatar";
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		User user = new User();
		user.setPk_user(init_pk_user);
		homeInterface.readUser(getActivity(), user);
	}

	private void initUI(LayoutInflater inflater) {
		mHomeNoWIFI = (RelativeLayout) mLayout.findViewById(R.id.HomeNoWIFI);// û������ʱ������
		mLayout.findViewById(R.id.HomeRequestCodeLayout).setOnClickListener(this);
		mHome_NoTeam_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.Home_NoTeam_prompt_layout);// û��С��ʱ��ʾ
		mLayout.findViewById(R.id.tab_home_new_layout).setOnClickListener(this);// �½�С��
		mLayout.findViewById(R.id.tab_home_new).setOnClickListener(this);// �½�С��
		home_gridview = (GridView) mLayout.findViewById(R.id.home_gridview);
		home_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// ȥ��gridview�����ı�����ɫ

		WIFIChange();// �ж�WIFI�ĸı���в�����ʾ�ĸı�

		// �Ƿ񻬶�ʱ����ͣ����
		home_gridview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		home_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				boolean isWIFI = Ifwifi.getNetworkConnected(getActivity());// �ж��Ƿ�������
				if (isWIFI) {
					HomeItemClick(arg2);
				} else {
					// �Զ���Toast
					View toastRoot = getActivity().getLayoutInflater().inflate(R.layout.my_prompt_toast, null);
					ToastUtils.ShowMsgCENTER(getActivity().getApplicationContext(), "���粻����", 0, toastRoot, 0);
				}

			}

			private void HomeItemClick(int arg2) {
				if (EvenNumber == 0) {
					if (!(PhoneLoginActivity.list.size() == arg2)) {
						Log.e("HomeFragment", "�������С���==========");
						group = PhoneLoginActivity.list.get(arg2);

						joinConv(group);// ���뵽�Ի�
						pk_group = group.getPk_group();

						Group readAllPerRelation_group = new Group();
						readAllPerRelation_group.setPk_group(pk_group);
						homeInterface.readAllPerRelation(getActivity(),readAllPerRelation_group);

						group_name = group.getName();

						// ÿ�ε���ѵ�ǰ��pk_group�������ݿ�
						JPushTabNumber jPushTabNumber = new JPushTabNumber(pk_group, 2, 0);
						jPushTabNumber.save();

						// �����ĳ�JPushNoSee���ݱ���ɾ��
						new Delete().from(JPushNoSee.class).where("pk_group=?", pk_group).execute();
						Log.e("HomeFragment", "ɾ����======" + pk_group);

					}
				} else {
					if (!(PhoneLoginActivity.list.size() == arg2)) {
						group = PhoneLoginActivity.list.get(arg2);
						joinConv(group);// ���뵽�Ի�
						pk_group = group.getPk_group();

						Group readAllPerRelation_group = new Group();
						readAllPerRelation_group.setPk_group(pk_group);
						homeInterface.readAllPerRelation(getActivity(),readAllPerRelation_group);

						group_name = group.getName();

						// ÿ�ε���ѵ�ǰ��pk_group�������ݿ�
						JPushTabNumber jPushTabNumber = new JPushTabNumber(pk_group, 2, 0);
						jPushTabNumber.save();

						// �����ĳ�JPushNoSee���ݱ���ɾ��
						new Delete().from(JPushNoSee.class).where("pk_group=?", pk_group).execute();
						Log.e("HomeFragment", "ɾ����======" + pk_group);
					}
				}
			}

			private void joinConv(final GroupHome group) {
				final Integer init_pk_user = InitPkUser.InitPkUser();
				AVIMClient curUser = AVIMClient.getInstance(String.valueOf(init_pk_user));
				curUser.open(new AVIMClientCallback() {
					@Override
					public void done(AVIMClient client, AVIMException e) {
						if (e == null) {
							// ��¼�ɹ�
							String em_id = group.getEm_id();
							AVIMConversation conversation = client.getConversation(em_id);
							// ע��Ի�
							final ChatManager chatManager = ChatManager.getInstance();
							chatManager.registerConversation(conversation);// ע��Ի�
							conversation.join(new AVIMConversationCallback() {
								@Override
								public void done(AVIMException e) {
									if (e == null) {
										// ����ɹ�
										Log.e("HomeFragment", "��ǰ��"+ init_pk_user + "����С��ɹ�");
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
						GridView.LayoutParams pl = new GridView.LayoutParams(getDisplayWidth((getActivity())),
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
			mHomePromptImage = (ImageView) inflater.findViewById(R.id.HomePromptImage);

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

			// ����ͼƬ��λ��
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
			layoutParams9.height = columnWidth;// ����ͼƬ�ĸ߶�
			layoutParams9.width = columnWidth; // ����ͼƬ�Ŀ��
			home_item_head.setLayoutParams(layoutParams9);

			if (EvenNumber == 0) {
				if (PhoneLoginActivity.list.size() > 0) {
					if (position < PhoneLoginActivity.list.size()) {
						GroupHome homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr+ "group-front-cover";
						PreferenceUtils.saveImageCache(getActivity(),completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(getActivity(), completeURL, home_item_head);

						Integer party_update = homeuser_gridview.getParty_update();
						Integer pk_group = homeuser_gridview.getPk_group();
						if (party_update > 0) {

							// ����֮ǰ��ɾ���������е�
							new Delete().from(JPushNoSee.class).where("pk_group=?", pk_group).execute();
							// ����δ���������ݿ���
							JPushNoSee jPushNoSee = new JPushNoSee(pk_group, 2,0);
							jPushNoSee.save();

							JPushNoSeeList.clear();
							JPushNoSeeList = new Select().from(JPushNoSee.class).execute();
							Log.e("HomeFragment","JPushNoSeeList�ĳ���11111======"+ JPushNoSeeList.size());
							if (JPushNoSeeList.size() > 0) {
								for (int i = 0; i < JPushNoSeeList.size(); i++) {
									JPushNoSee jPushNoSee1 = JPushNoSeeList.get(i);
									Integer jPushNoSee_pk_group = jPushNoSee1.getPk_group();
									Log.e("HomeFragment", "�������ID======"+ jPushNoSee_pk_group);
								}
							}

							// �ٲ��±�,
							JPushTabNumberList.clear();
							JPushTabNumberList = new Select().from(JPushTabNumber.class).execute();
							if (JPushTabNumberList.size() > 0) {
								for (int i = 0; i < JPushTabNumberList.size(); i++) {
									JPushTabNumber jPushTabNumber = JPushTabNumberList.get(i);
									Integer jpush_pk_group = jPushTabNumber.getPk_group();
									if (String.valueOf(pk_group).equals(String.valueOf(jpush_pk_group))) {
										mHomePromptImage.setVisibility(View.GONE);
									} else {
										mHomePromptImage.setVisibility(View.VISIBLE);
									}
								}
							} else {
								mHomePromptImage.setVisibility(View.VISIBLE);
							}
						} else {
							JPushNoSeeList.clear();
							JPushNoSeeList = new Select().from(JPushNoSee.class).execute();
							Log.e("HomeFragment", "JPushNoSeeList�ĳ���======"+ JPushNoSeeList.size());
							if (JPushNoSeeList.size() > 0) {
								for (int i = 0; i < JPushNoSeeList.size(); i++) {
									JPushNoSee jPushNoSee = JPushNoSeeList.get(i);
									Integer jPushNoSee_pk_group = jPushNoSee.getPk_group();
									if (String.valueOf(jPushNoSee_pk_group).equals(String.valueOf(pk_group))) {
										mHomePromptImage.setVisibility(View.VISIBLE);
									}
								}
							} else {
								mHomePromptImage.setVisibility(View.GONE);
							}
						}

					}
				}
			} else {
				if (PhoneLoginActivity.list.size() > 0) {
					if (position < PhoneLoginActivity.list.size()) {
						GroupHome homeuser_gridview = PhoneLoginActivity.list.get(position);
						String homeAvatar_path = homeuser_gridview.getAvatar_path();
						String homenickname = homeuser_gridview.getName();
						home_item_name.setText(homenickname);
						completeURL = beginStr + homeAvatar_path + endStr+ "group-front-cover";
						PreferenceUtils.saveImageCache(getActivity(),completeURL);
						homeImageLoaderUtils.getInstance().LoadImage(getActivity(), completeURL, home_item_head);

						Integer party_update = homeuser_gridview.getParty_update();
						Integer pk_group = homeuser_gridview.getPk_group();
						if (party_update > 0) {

							// ����֮ǰ��ɾ���������е�
							new Delete().from(JPushNoSee.class).where("pk_group=?", pk_group).execute();
							// ����δ���������ݿ���
							JPushNoSee jPushNoSee = new JPushNoSee(pk_group, 2,0);
							jPushNoSee.save();

							JPushNoSeeList.clear();
							JPushNoSeeList = new Select().from(JPushNoSee.class).execute();
							Log.e("HomeFragment","JPushNoSeeList�ĳ���11111======"+ JPushNoSeeList.size());
							if (JPushNoSeeList.size() > 0) {
								for (int i = 0; i < JPushNoSeeList.size(); i++) {
									JPushNoSee jPushNoSee1 = JPushNoSeeList.get(i);
									Integer jPushNoSee_pk_group = jPushNoSee1.getPk_group();
									Log.e("HomeFragment", "�������ID======"+ jPushNoSee_pk_group);
								}
							}

							// �ٲ��±�,
							JPushTabNumberList.clear();
							JPushTabNumberList = new Select().from(JPushTabNumber.class).execute();
							if (JPushTabNumberList.size() > 0) {
								for (int i = 0; i < JPushTabNumberList.size(); i++) {
									JPushTabNumber jPushTabNumber = JPushTabNumberList.get(i);
									Integer jpush_pk_group = jPushTabNumber.getPk_group();
									if (String.valueOf(pk_group).equals(String.valueOf(jpush_pk_group))) {
										mHomePromptImage.setVisibility(View.GONE);
									} else {
										mHomePromptImage.setVisibility(View.VISIBLE);
									}
								}
							} else {
								mHomePromptImage.setVisibility(View.VISIBLE);
							}
						} else {
							JPushNoSeeList.clear();
							JPushNoSeeList = new Select().from(JPushNoSee.class).execute();
							Log.e("HomeFragment", "JPushNoSeeList�ĳ���======"+ JPushNoSeeList.size());
							if (JPushNoSeeList.size() > 0) {
								for (int i = 0; i < JPushNoSeeList.size(); i++) {
									JPushNoSee jPushNoSee = JPushNoSeeList.get(i);
									Integer jPushNoSee_pk_group = jPushNoSee.getPk_group();
									if (String.valueOf(jPushNoSee_pk_group).equals(String.valueOf(pk_group))) {
										mHomePromptImage.setVisibility(View.VISIBLE);
									}
								}
							} else {
								mHomePromptImage.setVisibility(View.GONE);
							}
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
		case R.id.HomeRequestCodeLayout:
			HomeRequestCodeLayout();
			break;
		default:
			break;
		}
	}

	private void HomeRequestCodeLayout() {
		Intent intent = new Intent(getActivity(), RequestCodeActivity.class);
		startActivity(intent);
	}

	private void tab_home_new_layout() {
		SharedPreferences TeamFriends_sp = getActivity().getSharedPreferences("TeamFriends", 0);
		Editor editor = TeamFriends_sp.edit();
		editor.putBoolean("AddTeamFriends", false);
		editor.commit();
		Intent intent = new Intent(getActivity(), NewteamActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		SdPkUser.setRefreshTeam(false);// �ж�С������޸Ĺ���false
		sdcard = false;
		SharedPreferences requestcode_sp = getActivity().getSharedPreferences(IConstant.RequestCode, 0);
		Editor editor = requestcode_sp.edit();
		editor.putBoolean(IConstant.Refresh, false);
		editor.commit();

		SharedPreferences ChangeName_sp = getActivity().getSharedPreferences("ChangeGroupName", 0);
		Editor ChangeName_editor = ChangeName_sp.edit();
		ChangeName_editor.putBoolean("ChangeName", false);
		ChangeName_editor.commit();

		// �������
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
				ReadTeamInterface(init_pk_user);
				swipeLayout.setRefreshing(false);
				swipe_refresh_1.setRefreshing(false);
			}
		}, 3000);
	}
}
