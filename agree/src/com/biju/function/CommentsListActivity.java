package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Party;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.activeandroid.query.Select;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readPartyJoinMsgListenner;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

@SuppressLint("ResourceAsColor")
public class CommentsListActivity extends Activity implements OnClickListener {

	private ListView mComments_list_listview;
	private String pk_party;
	private Interface mCommentInterface;
	private ArrayList<Relation> partackList = new ArrayList<Relation>();
	private ArrayList<Relation> not_sayList = new ArrayList<Relation>();

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!avatar";
	private String completeURL;
	private String TestcompleteURL = beginStr+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private int commentsList_msg = -1;
	private MyPartakeAdapter partakeadapter;
	private RelativeLayout mCommentslist_partake_list_layout;
	private TextView mCommentslist_partake_list_number;
	private RelativeLayout mCommentslist_not_say_layout;
	private TextView mCommentslist_not_say_number;
	private TextView mCommentslist_partake_list_prompt;
	private TextView mCommentslist_not_say_prompt;
	private ArrayList<Group_ReadAllUser> commentslist = new ArrayList<Group_ReadAllUser>();
	private ListView mCommentslist_not_say_listview;
	private boolean isNotsay;
	private MyNotsayAdapter notsayAdapter;
	private Integer fk_group;
	private float mPay_amount;
	
	private List<String>  WeChatList = new ArrayList<String>();
	private List<User>  List = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_list);
		initInterface();
		initUI();
		initData();
	}
	

	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(fk_group);
		mCommentInterface.readAllPerRelation(CommentsListActivity.this,readAllPerRelation_group);
	}

	private void initInterface() {
		mCommentInterface = Interface.getInstance();
		mCommentInterface.setPostListener(new readPartyJoinMsgListenner() {

			@Override
			public void success(String A) {
				partackList.clear();
				not_sayList.clear();
				Log.e("CommentsListActivity", "返回的用户参与信息" + A);
				java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
				}.getType();
				ReadPartyback partyback = GsonUtils.parseJsonArray(A, type);
				ReturnData returnData = partyback.getReturnData();
				Log.e("CommentsListActivity","当前returnData:" + returnData.toString());
				List<Relation> relationList = returnData.getRelation();
				if (relationList.size() > 0) {
					for (int i = 0; i < relationList.size(); i++) {
						Relation relation = relationList.get(i);
						// 判断参与、拒绝数
						Integer relationship = relation.getRelationship();
						if(4==relationship){
							partackList.add(relation);
						}else if(0==relationship){
							not_sayList.add(relation);
						}else if(3==relationship){
							not_sayList.add(relation);
						}
					}
					if (isNotsay) {
						if (partackList.size() > 0) {
							for (int i = 0; i < partackList.size(); i++) {
								Relation relation = relationList.get(i);
								Integer partake_fk_user = relation.getPk_user();
								for (int j = 0; j < commentslist.size(); j++) {
									Group_ReadAllUser allUser = commentslist.get(j);
									Integer all_fk_user = allUser.getFk_user();
									if (String.valueOf(partake_fk_user).equals(String.valueOf(all_fk_user))) {
										Log.e("CommentsListActivity","进入循环的次数i==== " + i+ "    j=====" + j);
										Log.e("CommentsListActivity","所删除的all_fk_user" + all_fk_user);
										commentslist.remove(allUser);
									}
								}
							}
						}
					}

					mCommentslist_partake_list_number.setText(partackList.size()+"");//参与数量
					if (isNotsay) {
						mComments_list_listview.setVisibility(View.GONE);
						mCommentslist_not_say_listview.setVisibility(View.VISIBLE);
						mCommentslist_not_say_listview.setAdapter(notsayAdapter);
						notsayAdapter.notifyDataSetChanged();
					} else {
						mComments_list_listview.setVisibility(View.VISIBLE);
						mCommentslist_not_say_listview.setVisibility(View.GONE);
					}
					mCommentslist_not_say_number.setText(not_sayList.size()+"");//未表态数量
					
					if(partackList.size()>0){
						for (int i = 0; i < partackList.size(); i++) {
							Integer pk_user=partackList.get(i).getPk_user();
							WeChatList.add(String.valueOf(pk_user));//加入容器中，
						}
						if(WeChatList.size()>0){
							mCommentInterface.findMultiUsers(CommentsListActivity.this, WeChatList);
						}
					}
					
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		mCommentInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				commentslist.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("CommentsListActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							commentslist.add(readAllUser);
						}
						initReadParty(pk_party);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		mCommentInterface.setPostListener(new FindMultiUserListenner() {
			
			@Override
			public void success(String A) {
				Log.e("CommentsListActivity", "来自微信的用户在小组中的信息========" + A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					// 取第一个Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if(Users.size()>0){
						for (int i = 0; i < Users.size(); i++) {
							User user=Users.get(i);
							List.add(user);
						}
						partakeadapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});

	}

	private void initData() {
		Intent intent = getIntent();
		commentsList_msg = intent.getIntExtra(IConstant.CommentsList, 0);
		fk_group = intent.getIntExtra(IConstant.All_fk_group, 0);
		switch (commentsList_msg) {
		case 3:
			isNotsay = true;
			mComments_list_listview.setVisibility(View.GONE);
			mCommentslist_not_say_listview.setVisibility(View.VISIBLE);

			mCommentslist_partake_list_layout.setBackgroundResource(R.color.white);
			mCommentslist_partake_list_prompt.setTextColor(mCommentslist_partake_list_prompt.getResources().getColor(R.drawable.Common_text_color_gray));
			mCommentslist_partake_list_number.setTextColor(mCommentslist_partake_list_number.getResources().getColor(R.drawable.Common_text_color_green));

			mCommentslist_not_say_layout.setBackgroundResource(R.color.green_2);
			mCommentslist_not_say_prompt.setTextColor(Color.WHITE);
			mCommentslist_not_say_number.setTextColor(Color.WHITE);
			pk_party = intent.getStringExtra(IConstant.Not_Say);
			mPay_amount = intent.getFloatExtra("Pay_amount", 0);
			Log.e("CommentsListActivity", "读取出小组中的pk_party11111111========" + pk_party);
			break;
		case 4:
			isNotsay = false;
			mComments_list_listview.setVisibility(View.VISIBLE);
			mCommentslist_not_say_listview.setVisibility(View.GONE);

			mCommentslist_partake_list_layout.setBackgroundResource(R.color.green_2);
			mCommentslist_partake_list_prompt.setTextColor(Color.WHITE);
			mCommentslist_partake_list_number.setTextColor(Color.WHITE);

			mCommentslist_not_say_layout.setBackgroundResource(R.color.white);
			mCommentslist_not_say_prompt.setTextColor(mCommentslist_partake_list_prompt.getResources().getColor(R.drawable.Common_text_color_gray));
			mCommentslist_not_say_number.setTextColor(mCommentslist_partake_list_number.getResources().getColor(R.drawable.Common_text_color_green));
			pk_party = intent.getStringExtra(IConstant.ParTake);
			mPay_amount = intent.getFloatExtra("Pay_amount", 0);
			Log.e("CommentsListActivity", "读取出小组中的pk_party222222========" + pk_party);
			break;
		default:
			break;
		}
		returndata();
	}

	private void initReadParty(String pk_party) {
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		mCommentInterface.readPartyJoinMsg(CommentsListActivity.this, readparty);
	}

	private void initUI() {
		findViewById(R.id.Comments_list_back_layout).setOnClickListener(this);
		findViewById(R.id.Comments_list_back).setOnClickListener(this);// 返回
		mCommentslist_partake_list_layout = (RelativeLayout) findViewById(R.id.Commentslist_partake_list_layout);
		mCommentslist_partake_list_layout.setOnClickListener(this);// 参与列表
		mCommentslist_partake_list_prompt = (TextView) findViewById(R.id.Commentslist_partake_list_prompt);
		mCommentslist_partake_list_prompt.setOnClickListener(this);// 参与提示
		mCommentslist_partake_list_number = (TextView) findViewById(R.id.Commentslist_partake_list_number);
		mCommentslist_partake_list_number.setOnClickListener(this);// 参与数量
		mCommentslist_not_say_layout = (RelativeLayout) findViewById(R.id.Commentslist_not_say_layout);
		mCommentslist_not_say_layout.setOnClickListener(this);// 未表态列表
		mCommentslist_not_say_prompt = (TextView) findViewById(R.id.Commentslist_not_say_prompt);
		mCommentslist_not_say_prompt.setOnClickListener(this);// 未表态提示
		mCommentslist_not_say_number = (TextView) findViewById(R.id.Commentslist_not_say_number);
		mCommentslist_not_say_number.setOnClickListener(this);// 未表态数量

		mComments_list_listview = (ListView) findViewById(R.id.Commentslist_partake_list_listview);// 参与listview列表
		mComments_list_listview.setDividerHeight(0);//设置listview的item直接的间隙为0
		partakeadapter = new MyPartakeAdapter();
		mComments_list_listview.setAdapter(partakeadapter);

		mCommentslist_not_say_listview = (ListView) findViewById(R.id.Commentslist_not_say_listview);// 未表态列表
		mCommentslist_not_say_listview.setDividerHeight(0);//设置listview的item直接的间隙为0
		notsayAdapter = new MyNotsayAdapter();
	}

	class ViewHolder {
		ImageView commentslist_item_head;
		TextView commentslist_item_nickname;
		TextView commentslist_item_status;
		TextView commentslist_item_prompt_1;
		TextView commentslist_item_prompt_2;
	}

	class MyPartakeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return partackList.size();
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.commentslist_item,null);
				holder.commentslist_item_head = (ImageView) inflater.findViewById(R.id.commentslist_item_head);
				holder.commentslist_item_nickname = (TextView) inflater.findViewById(R.id.commentslist_item_nickname);
				holder.commentslist_item_status = (TextView) inflater.findViewById(R.id.commentslist_item_status);
				holder.commentslist_item_prompt_1=(TextView) inflater.findViewById(R.id.commentslist_item_prompt_1);
				holder.commentslist_item_prompt_2=(TextView) inflater.findViewById(R.id.commentslist_item_prompt_2);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			Log.e("CommentsListActivity","这时候的commentsList_msg2222222222222============"+ commentsList_msg);
			Relation relation = partackList.get(position);
			
			String status=relation.getStatus();
			if("3".equals(status)){
				if(mPay_amount==0){
					holder.commentslist_item_status.setText("微信用户 - 已经报名");
				}else {
					holder.commentslist_item_status.setText("微信用户 - 已报名 -"+"已支付"+mPay_amount+"元");
				}
				
				String WeChatPath=relation.getAvatar_path();
				PreferenceUtils.saveImageCache(CommentsListActivity.this,WeChatPath);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(CommentsListActivity.this,
						WeChatPath, holder.commentslist_item_head);
				
				Integer pk_user=relation.getPk_user();
				Log.e("CommentsListActivity", "已经报名的用户======"+pk_user);
				for (int i = 0; i < List.size(); i++) {
					User user=List.get(i);
					Integer WeChatUser=user.getPk_user();
					Log.e("CommentsListActivity", "已经报名的微信用户======"+WeChatUser);
					if(String.valueOf(pk_user).equals(String.valueOf(WeChatUser))){
						String name=user.getReal_name();
						holder.commentslist_item_nickname.setText(name+"("+relation.getNickname()+")");
					}
				}
				
			}else {
				if(mPay_amount==0){
					holder.commentslist_item_status.setText("已经报名");
				}else {
					holder.commentslist_item_status.setText("已报名 -"+"已支付"+mPay_amount+"元");
				}
				
				String useravatar_path1 = relation.getAvatar_path();
				completeURL = beginStr + useravatar_path1 + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(CommentsListActivity.this,
						completeURL, holder.commentslist_item_head);
				
				holder.commentslist_item_nickname.setText(relation.getNickname());
			}
			
			if(position==partackList.size()-1){
				holder.commentslist_item_prompt_2.setVisibility(View.VISIBLE);
				holder.commentslist_item_prompt_1.setVisibility(View.GONE);
			}else {
				holder.commentslist_item_prompt_2.setVisibility(View.GONE);
				holder.commentslist_item_prompt_1.setVisibility(View.VISIBLE);
			}
			return inflater;
		}

	}

	class MyNotsayAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return not_sayList.size();
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.commentslist_item,null);
				holder.commentslist_item_head = (ImageView) inflater.findViewById(R.id.commentslist_item_head);
				holder.commentslist_item_nickname = (TextView) inflater.findViewById(R.id.commentslist_item_nickname);
				holder.commentslist_item_status = (TextView) inflater.findViewById(R.id.commentslist_item_status);
				holder.commentslist_item_prompt_1=(TextView) inflater.findViewById(R.id.commentslist_item_prompt_1);
				holder.commentslist_item_prompt_2=(TextView) inflater.findViewById(R.id.commentslist_item_prompt_2);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			Relation relation = not_sayList.get(position);
			Integer relationship = relation.getRelationship();
			if(relationship==3){
				holder.commentslist_item_status.setText("未表态 - 已看");
				String useravatar_path = relation.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(
						CommentsListActivity.this, completeURL,
						holder.commentslist_item_head);
				holder.commentslist_item_nickname.setText(relation.getNickname());
			}else {
				holder.commentslist_item_status.setText("未表态 - 未看");
				String useravatar_path = relation.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(
						CommentsListActivity.this, completeURL,
						holder.commentslist_item_head);
				holder.commentslist_item_nickname.setText(relation.getNickname());
			}
			if(position==not_sayList.size()-1){
				holder.commentslist_item_prompt_2.setVisibility(View.VISIBLE);
				holder.commentslist_item_prompt_1.setVisibility(View.GONE);
			}else {
				holder.commentslist_item_prompt_2.setVisibility(View.GONE);
				holder.commentslist_item_prompt_1.setVisibility(View.VISIBLE);
			}
			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Commentslist_partake_list_layout:
		case R.id.Commentslist_partake_list_number:
		case R.id.Commentslist_partake_list_prompt:
			Commentslist_partake_list_layout();
			break;
		case R.id.Commentslist_not_say_layout:
		case R.id.Commentslist_not_say_number:
		case R.id.Commentslist_not_say_prompt:
			Commentslist_not_say_layout();
			break;
		case R.id.Comments_list_back_layout:
		case R.id.Comments_list_back:
			Comments_list_back_layout();
			break;
		default:
			break;
		}
	}

	private void Comments_list_back_layout() {
		finish();
	}

	// 点击未表态
	private void Commentslist_not_say_layout() {
		isNotsay = true;
		mCommentslist_partake_list_layout.setBackgroundResource(R.color.white);
		mCommentslist_partake_list_prompt.setTextColor(mCommentslist_partake_list_prompt.getResources().getColor(R.drawable.Common_text_color_gray));
		mCommentslist_partake_list_number.setTextColor(mCommentslist_partake_list_number.getResources().getColor(R.drawable.Common_text_color_green));

		mCommentslist_not_say_layout.setBackgroundResource(R.color.green_2);
		mCommentslist_not_say_prompt.setTextColor(Color.WHITE);
		mCommentslist_not_say_number.setTextColor(Color.WHITE);
		returndata();
		notsayAdapter.notifyDataSetChanged();
	}

	// 点击参与
	private void Commentslist_partake_list_layout() {
		isNotsay = false;
		mCommentslist_partake_list_layout.setBackgroundResource(R.color.green_2);
		mCommentslist_partake_list_prompt.setTextColor(Color.WHITE);
		mCommentslist_partake_list_number.setTextColor(Color.WHITE);

		mCommentslist_not_say_layout.setBackgroundResource(R.color.white);
		mCommentslist_not_say_prompt.setTextColor(mCommentslist_partake_list_prompt.getResources().getColor(R.drawable.Common_text_color_gray));
		mCommentslist_not_say_number.setTextColor(mCommentslist_partake_list_number.getResources().getColor(R.drawable.Common_text_color_green));
		returndata();
		partakeadapter.notifyDataSetChanged();
	}

}
