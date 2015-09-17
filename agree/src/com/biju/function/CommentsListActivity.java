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
import com.BJ.javabean.Party;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readPartyJoinMsgListenner;
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

	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String completeURL;
	private String TestcompleteURL = beginStr+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private int commentsList_msg = -1;
	private MyPartakeAdapter partakeadapter;
	private int partakeNum;
	private RelativeLayout mCommentslist_partake_list_layout;
	private TextView mCommentslist_partake_list_number;
	private RelativeLayout mCommentslist_not_say_layout;
	private TextView mCommentslist_not_say_number;
	private TextView mCommentslist_partake_list_prompt;
	private TextView mCommentslist_not_say_prompt;
	private ArrayList<Group_ReadAllUser> commentslist = new ArrayList<Group_ReadAllUser>();
	private int not_sayNum;
	private ListView mCommentslist_not_say_listview;
	private boolean isNotsay;
	private MyNotsayAdapter notsayAdapter;
	private Integer fk_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_list);
		initUI();
		initInterface();
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
						switch (relationship) {
						case 0:
							not_sayList.add(relationList.get(i));
							break;
						case 1:
							partakeNum++;
							Log.e("CommentsListActivity", "当前partakeNum的数量"+ partakeNum + "=======" + i);
							partackList.add(relationList.get(i));
							break;
						default:
							break;
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
										commentslist.remove(allUser);
									}
								}
							}
						}
					}

					mCommentslist_partake_list_number.setText(String.valueOf(partakeNum));
					partakeNum = 0;
					if (isNotsay) {
						mComments_list_listview.setVisibility(View.GONE);
						mCommentslist_not_say_listview.setVisibility(View.VISIBLE);
						not_sayNum = commentslist.size();
						mCommentslist_not_say_listview.setAdapter(notsayAdapter);
						notsayAdapter.notifyDataSetChanged();
					} else {
						mComments_list_listview.setVisibility(View.VISIBLE);
						mCommentslist_not_say_listview.setVisibility(View.GONE);
						not_sayNum = commentslist.size() - partackList.size();
					}
					mCommentslist_not_say_number.setText(String.valueOf(not_sayNum));
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

	}

	private void initData() {
		Intent intent = getIntent();
		commentsList_msg = intent.getIntExtra(IConstant.CommentsList, 0);
		fk_group = intent.getIntExtra(IConstant.All_fk_group, 0);
		switch (commentsList_msg) {
		case 0:
			isNotsay = true;
			mComments_list_listview.setVisibility(View.GONE);
			mCommentslist_not_say_listview.setVisibility(View.VISIBLE);

			mCommentslist_partake_list_layout.setBackgroundResource(R.color.white);
			mCommentslist_partake_list_prompt.setTextColor(Color.GRAY);
			mCommentslist_partake_list_number.setTextColor(Color.GREEN);

			mCommentslist_not_say_layout.setBackgroundResource(R.color.green_2);
			mCommentslist_not_say_prompt.setTextColor(Color.WHITE);
			mCommentslist_not_say_number.setTextColor(Color.WHITE);
			pk_party = intent.getStringExtra(IConstant.Not_Say);
			break;
		case 1:
			isNotsay = false;
			mComments_list_listview.setVisibility(View.VISIBLE);
			mCommentslist_not_say_listview.setVisibility(View.GONE);

			mCommentslist_partake_list_layout.setBackgroundResource(R.color.green_2);
			mCommentslist_partake_list_prompt.setTextColor(Color.WHITE);
			mCommentslist_partake_list_number.setTextColor(Color.WHITE);

			mCommentslist_not_say_layout.setBackgroundResource(R.color.white);
			mCommentslist_not_say_prompt.setTextColor(Color.GRAY);
			mCommentslist_not_say_number.setTextColor(Color.GREEN);
			pk_party = intent.getStringExtra(IConstant.ParTake);
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
		partakeadapter = new MyPartakeAdapter();
		mComments_list_listview.setAdapter(partakeadapter);

		mCommentslist_not_say_listview = (ListView) findViewById(R.id.Commentslist_not_say_listview);// 未表态列表
		notsayAdapter = new MyNotsayAdapter();
		// mCommentslist_not_say_listview.setAdapter(notsayAdapter);
	}

	class ViewHolder {
		ImageView commentslist_item_head;
		TextView commentslist_item_nickname;
		TextView commentslist_item_status;
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
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			Log.e("CommentsListActivity","这时候的commentsList_msg2222222222222============"+ commentsList_msg);
			Relation relation = partackList.get(position);
			holder.commentslist_item_status.setText("参与");
			String useravatar_path1 = relation.getAvatar_path();
			completeURL = beginStr + useravatar_path1 + endStr;
			PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
			ImageLoaderUtils.getInstance().LoadImage(CommentsListActivity.this,
					completeURL, holder.commentslist_item_head);
			holder.commentslist_item_nickname.setText(relation.getNickname());
			return inflater;
		}

	}

	class MyNotsayAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			Log.e("CommentsListActivity", "这时候的commentslist.size()============"+ commentslist.size());
			return commentslist.size();
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
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			if (commentslist.size() > 0) {
				Group_ReadAllUser allUser = commentslist.get(position);
				Integer allUser_pk_user = allUser.getFk_user();
				Log.e("CommentsListActivity", "这时候的allUser_pk_user============"+ allUser_pk_user);
				Log.e("CommentsListActivity","这时候的not_sayList.size()============"+ not_sayList.size());
				if (not_sayList.size() > 0) {
					for (int i = 0; i < not_sayList.size(); i++) {
						Relation relation = not_sayList.get(i);
						Integer relation_pk_user = relation.getPk_user();
						Log.e("CommentsListActivity", "这时候的有进入============");
						Log.e("CommentsListActivity","这时候的relation_pk_user============"+ relation_pk_user);
						if (Integer.valueOf(allUser_pk_user).equals(Integer.valueOf(relation_pk_user))) {
							Log.e("CommentsListActivity", "进入已看的============");
							Integer relationship = relation.getRelationship();
							if(relationship==0)
							{
								holder.commentslist_item_status.setText("未表态 - 已看");
								String useravatar_path = allUser.getAvatar_path();
								completeURL = beginStr + useravatar_path + endStr;
								PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
								ImageLoaderUtils.getInstance().LoadImage(
										CommentsListActivity.this, completeURL,
										holder.commentslist_item_head);
								holder.commentslist_item_nickname.setText(allUser.getNickname());
							}
						} else {
							Log.e("CommentsListActivity", "进入了未看的============");
							holder.commentslist_item_status.setText("未表态 - 未看");
							String useravatar_path = allUser.getAvatar_path();
							completeURL = beginStr + useravatar_path + endStr;
							PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
							ImageLoaderUtils.getInstance().LoadImage(
									CommentsListActivity.this, completeURL,
									holder.commentslist_item_head);
							holder.commentslist_item_nickname.setText(allUser.getNickname());
						}

					}
				} else {
					holder.commentslist_item_status.setText("未表态 - 未看");
					String useravatar_path = allUser.getAvatar_path();
					completeURL = beginStr + useravatar_path + endStr;
					PreferenceUtils.saveImageCache(CommentsListActivity.this,completeURL);// 存SP
					ImageLoaderUtils.getInstance().LoadImage(
							CommentsListActivity.this, completeURL,
							holder.commentslist_item_head);
					holder.commentslist_item_nickname.setText(allUser.getNickname());
				}
				
			}
			return inflater;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments_list, menu);
		return true;
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
		mCommentslist_partake_list_prompt.setTextColor(Color.GRAY);
		mCommentslist_partake_list_number.setTextColor(Color.GREEN);

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
		mCommentslist_not_say_prompt.setTextColor(Color.GRAY);
		mCommentslist_not_say_number.setTextColor(Color.GREEN);
		returndata();
		partakeadapter.notifyDataSetChanged();
	}

}
