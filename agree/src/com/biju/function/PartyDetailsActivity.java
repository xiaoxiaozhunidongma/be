package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.ImageText;
import com.BJ.javabean.ImageTextBack;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.Party3;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.User;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Weeks;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.Interface.ReadGraphicListenner;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readPartyJoinMsgListenner;
import com.biju.Interface.updateUserJoinMsgListenner;
import com.biju.R;
import com.biju.pay.GraphicPreviewActivity;
import com.biju.pay.PayBaseActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

public class PartyDetailsActivity extends Activity implements OnClickListener {
	private ArrayList<Group_ReadAllUser> PartyDetailsList = new ArrayList<Group_ReadAllUser>();
	private ArrayList<Relation> partakeNumList = new ArrayList<Relation>();
	private ArrayList<Relation> OtherList = new ArrayList<Relation>();

	private Interface readpartyInterface;
	private Party2 oneParty;
	private Integer pk_party_user;
	private UserAllParty allParty;
	private boolean userAll;
	private Integer sD_pk_user;

	private TextView mPartyDetailsPartyAddress;
	private TextView mPartyDetailsPartyName;
	private TextView mPartyDetailsPartyStartTime;
	private TextView mPartyDetailsPayWayText;
	private TextView mPartyDetailsPartakeNumber;
	private TextView mPartyDetailsNotSayNumber;
	private TextView mPartyDetailsPartyOrganizersName;
	private TextView mPartyDetails_apply;
	private String pk_party;
	private Integer fk_group;
	private int not_sayNum;
	private Integer current_relationship;
	private RelativeLayout mPartyDetails_apply_layout;
	private Integer mPay_type;
	private float mPay_amount;
	private String mPayName;
	public static GetWeChatPay getWeChatPay;
	public static GetAliPay getAliPay;
	private TextView mPartyDetailsPartyGraphicNumber;
	private ImageView mPartyDetailsPartyOrganizersHead;

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private List<ImageText> GraphicNumberList = new ArrayList<ImageText>();
	private RelativeLayout mPartyDetailsPartyAddressLayout;
	private RelativeLayout mPartyDetailsPartyGraphicLayout;
	private ImageView mPartyDetailsPartyAddressImage;
	private ImageView mPartyDetailsPartyGraphicImage;
	private RelativeLayout mPartyDetailsBackground;
	public static GetPartyDetailsBackground partyDetailsBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_details);
		// 加入list中
		RefreshActivity.activList_1.add(PartyDetailsActivity.this);
		// 获取sd卡中的sD_pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		initUI();
		initInterface();
		initOneParty();
		returndata();
		initGraphic();
		initGetWeChatPay();// 微信支付
		initAliPay();// 支付宝支付
		initPartyDetailsBackground();//覆盖色回调
	}

	private void initPartyDetailsBackground() {
		GetPartyDetailsBackground partyDetailsBackground=new GetPartyDetailsBackground(){

			@Override
			public void PartyDetailsBackground() {
				mPartyDetailsBackground.setVisibility(View.GONE);
				Animation animation=new AlphaAnimation(1.0f,0.0f);
				animation.setDuration(500);
				mPartyDetailsBackground.startAnimation(animation);
			}
			
		};
		this.partyDetailsBackground=partyDetailsBackground;
	}

	private void initGraphic() {
		Party party = new Party();
		party.setPk_party(pk_party);
		readpartyInterface.ReadGraphic(PartyDetailsActivity.this, party);
	}

	private void initAliPay() {
		GetAliPay getAliPay = new GetAliPay() {

			@Override
			public void AliPay() {
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Toast();
			}
		};
		this.getAliPay = getAliPay;
	}

	@SuppressWarnings("static-access")
	private void initGetWeChatPay() {
		GetWeChatPay getWeChatPay = new GetWeChatPay() {

			@Override
			public void WeChatPay() {
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Toast();
			}
		};
		this.getWeChatPay = getWeChatPay;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initInterface();
		if (userAll) {
			SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.Partyfragmnet, 0);
			pk_party_user = PartyDetails_sp.getInt(IConstant.Partyfragmnet_Pk_party_user, 0);
			pk_party = PartyDetails_sp.getString(IConstant.Partyfragmnet_Pk_party, "");
			fk_group = PartyDetails_sp.getInt(IConstant.Partyfragmnet_fk_group,0);
		} else {
			SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.Schedule, 0);
			pk_party_user = PartyDetails_sp.getInt(IConstant.Pk_party_user, 0);
			pk_party = PartyDetails_sp.getString(IConstant.Pk_party, "");
			fk_group = PartyDetails_sp.getInt(IConstant.fk_group, 0);
		}
	}

	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(fk_group);
		readpartyInterface.readAllPerRelation(PartyDetailsActivity.this,readAllPerRelation_group);
	}

	// 读取聚会详情
	private void initReadParty() {
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this,readparty);
	}

	private void initInterface() {
		readpartyInterface = Interface.getInstance();
		readpartyInterface.setPostListener(new updateUserJoinMsgListenner() {

			@Override
			public void success(String A) {
				returndata();
				Log.e("PartyDetailsActivity", "返回的是否更新成功" + A);
			}

			@Override
			public void defail(Object B) {

			}
		});

		readpartyInterface.setPostListener(new readPartyJoinMsgListenner() {

			@Override
			public void success(String A) {
				partakeNumList.clear();
				OtherList.clear();
				Log.e("PartyDetailsActivity", "返回的用户参与信息" + A);
				java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
				}.getType();
				ReadPartyback partyback = GsonUtils.parseJsonArray(A, type);
				ReturnData returnData = partyback.getReturnData();
				Log.e("PartyDetailsActivity","当前returnData:" + returnData.toString());
				List<Relation> relationList = returnData.getRelation();
				if (relationList.size() > 0) {
					for (int i = 0; i < relationList.size(); i++) {
						Relation relation = relationList.get(i);
						// 判断参与、拒绝数
						Integer relationship = relation.getRelationship();
						if (4 == relationship) {
							partakeNumList.add(relation);
						} else if (0 == relationship) {
							OtherList.add(relation);
						} else if (3 == relationship) {
							OtherList.add(relation);
						}
						// 查找当前用户的参与信息
						Integer pk_user = relation.getPk_user();
						if (String.valueOf(pk_user).equals(String.valueOf(sD_pk_user))) {
							current_relationship = relationList.get(i).getRelationship();
							if (current_relationship == 4) {
								mPartyDetails_apply.setText("已参与");
								mPartyDetails_apply_layout.setBackgroundResource(R.drawable.PartyDetails_noapply_layout_color);// 已报名背景为淡灰色
							} else {
								if (1 == mPay_type) {
									mPartyDetails_apply.setText("报名");
								} else if (3 == mPay_type) {
									mPartyDetails_apply.setText("报名:" + " ¥"+ mPay_amount);
								}
								mPartyDetails_apply_layout.setBackgroundResource(R.drawable.PartyDetails_apply_layout_color);// 未报名背景为绿色
							}
						}
					}
					Log.e("PartyDetailsActivity", "当前partakeNum的数量"+ partakeNumList.size());
					Log.e("PartyDetailsActivity", "当前not_sayNum的数量"+ not_sayNum);
					mPartyDetailsPartakeNumber.setText(String.valueOf(partakeNumList.size()));// 显示参与数量
					mPartyDetailsNotSayNumber.setText(String.valueOf(OtherList.size()));// 显示未表态数量

					List<Party3> partylist = returnData.getParty();
					if (partylist.size() > 0) {
						Party3 readparty = partylist.get(0);
						Integer pk_user = readparty.getFk_user();

						// 查找聚会创建者
						List<String> list = new ArrayList<String>();
						list.add(String.valueOf(pk_user));
						readpartyInterface.findMultiUsers(PartyDetailsActivity.this, list);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		// 查找创建者监听
		readpartyInterface.setPostListener(new FindMultiUserListenner() {

			@Override
			public void success(String A) {
				Log.e("PartyDetailsActivity", "返回的创建者信息" + A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					// 取第一个Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						mPartyDetailsPartyOrganizersName.setText(user.getNickname() + "  创建的活动");

						String useravatar_path = user.getAvatar_path();
						String completeURL = beginStr + useravatar_path+ endStr + "mini-avatar";
						PreferenceUtils.saveImageCache(PartyDetailsActivity.this, completeURL);// 存SP
						ImageLoaderUtils.getInstance().LoadImageCricular(PartyDetailsActivity.this, completeURL,
								mPartyDetailsPartyOrganizersHead);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		readpartyInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				PartyDetailsList.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("PartyDetailsActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							PartyDetailsList.add(readAllUser);
						}
						initReadParty();// 读取聚会详情
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// 获取图文信息的监听
		readpartyInterface.setPostListener(new ReadGraphicListenner() {

			@Override
			public void success(String A) {
				GraphicNumberList.clear();
				Log.e("PartyDetailsActivity", "获取回来的图文信息=========" + A);
				ImageTextBack imageTextBack = GsonUtils.parseJson(A,ImageTextBack.class);
				Integer StatusMsg = imageTextBack.getStatusMsg();
				if (1 == StatusMsg) {
					List<ImageText> imageTextslist = imageTextBack.getReturnData();
					if (imageTextslist.size() > 0) {
						for (int i = 0; i < imageTextslist.size(); i++) {
							ImageText imageText = imageTextslist.get(i);
							GraphicNumberList.add(imageText);
						}
						if (GraphicNumberList.size() > 0) {
							mPartyDetailsPartyGraphicLayout.setEnabled(true);
							mPartyDetailsPartyGraphicNumber.setText(GraphicNumberList.size()+ "  个图文信息");
						}
					}
				} else {
					mPartyDetailsPartyGraphicNumber.setText("无更多详情");
					mPartyDetailsPartyGraphicLayout.setEnabled(false);
					mPartyDetailsPartyGraphicImage.setVisibility(View.GONE);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	// 首次进来时传值
	private void initOneParty() {
		Intent intent = getIntent();
		userAll = intent.getBooleanExtra(IConstant.UserAll, false);
		if (userAll) {
			allParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllParty);
			pk_party_user = allParty.getPk_party_user();
			pk_party = allParty.getPk_party();
			fk_group = allParty.getFk_group();
			String Begin_time = allParty.getBegin_time();
			String years = Begin_time.substring(0, 4);
			String months = Begin_time.substring(5, 7);
			String days = Begin_time.substring(8, 10);
			String hour = Begin_time.substring(11, 13);
			String minute = Begin_time.substring(14, 16);
			Integer y = Integer.valueOf(years);
			Integer m = Integer.valueOf(months);
			Integer d = Integer.valueOf(days);
			// 调用计算星期几的方法
			Weeks.CaculateWeekDay(y, m, d);
			String week = Weeks.getweek();
			mPartyDetailsPartyName.setText(allParty.getName());// 显示聚会名称
			mPartyDetailsPartyStartTime.setText(years + "年" + months + "月"
					+ days + "日" + "  " + week + "  " + hour + ":" + minute);// 显示聚会时间
			if (allParty.getLocation() == null) {
				mPartyDetailsPartyAddressLayout.setEnabled(false);
				mPartyDetailsPartyAddress.setText("无地址信息");// 显示聚会地点
				mPartyDetailsPartyAddressImage.setVisibility(View.GONE);//没有地址则隐藏箭头
			} else {
				mPartyDetailsPartyAddressLayout.setEnabled(true);
				mPartyDetailsPartyAddress.setText(allParty.getLocation());// 显示聚会地点
			}
			mPay_type = allParty.getPay_type();// 支付类型
			mPay_amount = allParty.getPay_amount();// 支付金额
			mPayName = allParty.getName();// 聚会名称
			if (1 == mPay_type) {
				mPartyDetailsPayWayText.setText("该活动为免费活动");
			} else {
				mPartyDetailsPayWayText.setText("该活动为预付款活动");
			}
		} else {
			oneParty = (Party2) intent.getSerializableExtra(IConstant.OneParty);
			SharedPreferences partydetails_sp = getSharedPreferences(IConstant.Schedule, 0);
			pk_party_user = partydetails_sp.getInt(IConstant.Pk_party_user, 0);
			Log.e("PartyDetailsActivity", "得到的第二个getPk_party_user========="+ pk_party_user);
			pk_party = oneParty.getPk_party();
			fk_group = oneParty.getFk_group();
			String Begin_time = oneParty.getBegin_time();
			String years = Begin_time.substring(0, 4);
			String months = Begin_time.substring(5, 7);
			String days = Begin_time.substring(8, 10);
			String hour = Begin_time.substring(11, 13);
			String minute = Begin_time.substring(14, 16);
			Integer y = Integer.valueOf(years);
			Integer m = Integer.valueOf(months);
			Integer d = Integer.valueOf(days);
			// 调用计算星期几的方法
			Weeks.CaculateWeekDay(y, m, d);
			String week = Weeks.getweek();
			mPartyDetailsPartyName.setText(oneParty.getName());// 显示聚会名称
			mPartyDetailsPartyStartTime.setText(years + "年" + months + "月"+ days + "日" + "  " + week + "  " + hour + ":" + minute);// 显示聚会时间
			Log.e("PartyDetailsActivity","得到的第二个oneParty.getLocation()========="+ oneParty.getLocation());
			if (oneParty.getLocation() == null) {
				mPartyDetailsPartyAddressLayout.setEnabled(false);
				mPartyDetailsPartyAddress.setText("无地址信息");// 显示聚会地点
				mPartyDetailsPartyAddressImage.setVisibility(View.GONE);//没有地址则隐藏箭头
			} else {
				mPartyDetailsPartyAddressLayout.setEnabled(true);
				mPartyDetailsPartyAddress.setText(oneParty.getLocation());// 显示聚会地点
			}
			mPay_type = oneParty.getPay_type();// 支付类型
			mPay_amount = oneParty.getPay_amount();// 支付金额
			mPayName = oneParty.getName();// 聚会名称
			if (1 == mPay_type) {
				mPartyDetailsPayWayText.setText("该活动为免费活动");
			} else {
				mPartyDetailsPayWayText.setText("该活动为预付款活动");
			}
		}
	}

	private void initUI() {
		mPartyDetailsBackground = (RelativeLayout) findViewById(R.id.PartyDetailsBackground);//覆盖色布局
		
		mPartyDetailsPartyGraphicImage = (ImageView) findViewById(R.id.PartyDetailsPartyGraphicImage);//有图文信息传时候才出现的箭头
		mPartyDetailsPartyAddressImage = (ImageView) findViewById(R.id.PartyDetailsPartyAddressImage);//有地址信息才出现的箭头
		mPartyDetailsPayWayText = (TextView) findViewById(R.id.PartyDetailsPayWayText);// 付款方式
		mPartyDetailsPartyName = (TextView) findViewById(R.id.PartyDetailsPartyName);// 聚会名称
		mPartyDetailsPartyStartTime = (TextView) findViewById(R.id.PartyDetailsPartyStartTime);// 聚会时间
		mPartyDetailsPartyAddress = (TextView) findViewById(R.id.PartyDetailsPartyAddress);// 聚会地址
		mPartyDetailsPartyAddressLayout = (RelativeLayout) findViewById(R.id.PartyDetailsPartyAddressLayout);
		mPartyDetailsPartyAddressLayout.setOnClickListener(this);// 跳转至大地图导航界面
		mPartyDetailsPartyGraphicLayout = (RelativeLayout) findViewById(R.id.PartyDetailsPartyGraphicLayout);
		mPartyDetailsPartyGraphicLayout.setOnClickListener(this);// 跳转至图文详情列表
		mPartyDetailsPartyGraphicNumber = (TextView) findViewById(R.id.PartyDetailsPartyGraphicNumber);// 显示图文个数
		findViewById(R.id.PartyDetailsPartakeLayout).setOnClickListener(this);// 参与
		mPartyDetailsPartakeNumber = (TextView) findViewById(R.id.PartyDetailsPartakeNumber);// 显示参与数量
		findViewById(R.id.PartyDetailsNotSayLayout).setOnClickListener(this);// 未表态
		mPartyDetailsNotSayNumber = (TextView) findViewById(R.id.PartyDetailsNotSayNumber);// 显示未表态数量
		mPartyDetailsPartyOrganizersName = (TextView) findViewById(R.id.PartyDetailsPartyOrganizersName);// 组织者
		mPartyDetailsPartyOrganizersHead = (ImageView) findViewById(R.id.PartyDetailsPartyOrganizersHead);// 组织者头像

		mPartyDetails_apply_layout = (RelativeLayout) findViewById(R.id.PartyDetails_apply_layout);
		mPartyDetails_apply_layout.setOnClickListener(this);// 报名
		mPartyDetails_apply = (TextView) findViewById(R.id.PartyDetails_apply);// 报名支付金额

		findViewById(R.id.PartyDetails_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.PartyDetails_back).setOnClickListener(this);
		findViewById(R.id.PartyDetails_more_layout).setOnClickListener(this);// 设置
		findViewById(R.id.PartyDetails_more).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PartyDetails_back_layout:
		case R.id.PartyDetails_back:
			PartyDetails_back();
			break;
		case R.id.PartyDetails_more_layout:
		case R.id.PartyDetails_more:
			PartyDetails_more();
			break;
		case R.id.PartyDetailsPartakeLayout:
			PartyDetailsPartakeLayout();
			break;
		case R.id.PartyDetailsNotSayLayout:
			PartyDetailsNotSayLayout();
			break;
		case R.id.PartyDetails_apply_layout:
		case R.id.PartyDetails_apply:
			PartyDetails_apply_layout();
			break;
		case R.id.PartyDetailsPartyGraphicLayout:
			PartyDetailsPartyGraphicLayout();
			break;
		case R.id.PartyDetailsPartyAddressLayout:
			PartyDetailsPartyAddressLayout();
			break;
		default:
			break;
		}
	}

	// 跳转至大地图进行导航
	private void PartyDetailsPartyAddressLayout() {
		if (userAll) {
			Double mLat = allParty.getLatitude();
			Log.e("PartyDetailsActivity", "当前的mLat======" + mLat);
			Double mLng = allParty.getLongitude();
			Log.e("PartyDetailsActivity", "当前的mLng======" + mLng);
			if (mLat != null && mLng != null) {
				String address = allParty.getLocation();
				if (!("".equals(address))) {
					Intent intent = new Intent(PartyDetailsActivity.this,BigMapActivity.class);
					intent.putExtra("BigMap", true);
					intent.putExtra("AllBigMap", allParty);
					startActivity(intent);
					overridePendingTransition(R.anim.in_item, R.anim.out_item);
				}
			}
		} else {
			Double mLat = oneParty.getLatitude();
			Log.e("PartyDetailsActivity", "当前的mLat111111======" + mLat);
			Double mLng = oneParty.getLongitude();
			Log.e("PartyDetailsActivity", "当前的mLng111111======" + mLng);
			if (mLat != null && mLng != null) {
				String address = oneParty.getLocation();
				if (!("".equals(address))) {
					Intent intent = new Intent(PartyDetailsActivity.this,BigMapActivity.class);
					intent.putExtra("BigMap", false);
					intent.putExtra("OneBigMap", oneParty);
					startActivity(intent);
					overridePendingTransition(R.anim.in_item, R.anim.out_item);
				}
			}
		}
	}

	// 跳转图文信息界面
	private void PartyDetailsPartyGraphicLayout() {
		Intent intent = new Intent(PartyDetailsActivity.this,GraphicPreviewActivity.class);
		intent.putExtra("Pk_party", pk_party);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	// 未表态
	private void PartyDetailsNotSayLayout() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 3);
		intent.putExtra(IConstant.Not_Say, pk_party);
		intent.putExtra(IConstant.All_fk_group, fk_group);
		intent.putExtra("Pay_amount", mPay_amount);
		startActivity(intent);
	}

	// 参与
	private void PartyDetailsPartakeLayout() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 4);
		intent.putExtra(IConstant.ParTake, pk_party);
		intent.putExtra(IConstant.All_fk_group, fk_group);
		intent.putExtra("Pay_amount", mPay_amount);
		startActivity(intent);

	}

	private void PartyDetails_apply_layout() {
		if (current_relationship == 4) {
			if (1 == mPay_type) {
				final SweetAlertDialog sd = new SweetAlertDialog(PartyDetailsActivity.this,SweetAlertDialog.WARNING_TYPE);
				sd.setTitleText("警告");
				sd.setContentText("你确定要取消报名？");
				sd.setCancelText("我再想想");
				sd.setConfirmText("是的");
				sd.showCancelButton(true);
				sd.setCancelClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sd.cancel();
							}
						})
						.setConfirmClickListener(
								new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										sd.cancel();
										Party_User party_user = new Party_User();
										party_user.setPk_party_user(pk_party_user);
										party_user.setRelationship(0);
										party_user.setStatus(1);
										party_user.setFk_party(pk_party);
										party_user.setFk_user(sD_pk_user);
										readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
									}
								}).show();
			}
		} else {
			if (1 == mPay_type) {
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Toast();
			} else if (3 == mPay_type) {
				//覆盖色渐变
				mPartyDetailsBackground.setVisibility(View.VISIBLE);
				Animation animation=new AlphaAnimation(0.0f,1.0f);
				animation.setDuration(500);
				mPartyDetailsBackground.startAnimation(animation);
				
				Intent intent = new Intent(PartyDetailsActivity.this,PayBaseActivity.class);
				intent.putExtra(IConstant.Paymount, mPay_amount);
				intent.putExtra(IConstant.Payname, mPayName);
				if (userAll) {
					intent.putExtra(IConstant.UserAllUoreParty, allParty);
					intent.putExtra(IConstant.UserAll, true);
				} else {
					intent.putExtra(IConstant.MoreParty, oneParty);
				}
				startActivity(intent);
				overridePendingTransition(R.anim.top_item, R.anim.bottom_item);//从下往上动画
			}
		}
	}

	private void Toast() {
		// 自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("报名成功");
		toast.show();
	}

	private void PartyDetails_more() {
		//覆盖色渐变
		mPartyDetailsBackground.setVisibility(View.VISIBLE);
		Animation animation=new AlphaAnimation(0.0f,1.0f);
		animation.setDuration(500);
		mPartyDetailsBackground.startAnimation(animation);
		
		Intent intent = new Intent(PartyDetailsActivity.this,MoreActivity.class);
		if (userAll) {
			intent.putExtra(IConstant.UserAllUoreParty, allParty);
			intent.putExtra(IConstant.UserAll, true);
		} else {
			intent.putExtra(IConstant.MoreParty, oneParty);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.top_item, R.anim.bottom_item);//从下往上动画
	}

	private void PartyDetails_back() {
		if (userAll) {
			finish();
		} else {
			finish();
			SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
			Editor editor = PartyDetails_sp.edit();
			editor.putBoolean(IConstant.PartyDetails, true);
			editor.commit();
		}
	}

	// 微信支付报名
	public interface GetWeChatPay {
		void WeChatPay();
	}

	// 支付宝支付
	public interface GetAliPay {
		void AliPay();
	}
	
	//覆盖色接口
	public interface GetPartyDetailsBackground{
		void PartyDetailsBackground();
	}
	
}
