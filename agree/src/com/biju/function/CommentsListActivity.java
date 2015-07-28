package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

import com.BJ.javabean.Party;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readPartyJoinMsgListenner;
import com.biju.login.RegisteredActivity;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.tencent.upload.UploadManager;

public class CommentsListActivity extends Activity implements OnClickListener {

	private RelativeLayout mComments_list_partake_layout;
	private RelativeLayout mComments_list_refuse_layout;
	private RelativeLayout mComments_list_not_say_layout;
	private TextView mComments_list_partake_count;
	private TextView mComments_list_refuse_count;
	private TextView mComments_list_not_say_count;
	private ListView mComments_list_listview;
	private String pk_party;
	private Interface mCommentInterface;
	private ArrayList<Relation> partackList=new ArrayList<Relation>();
	private ArrayList<Relation> not_sayList=new ArrayList<Relation>();
	private ArrayList<Relation> refuseList=new ArrayList<Relation>();
	
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String useravatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private int commentsList_msg;
	private MyAdapter adapter;
	private int partakeNum;
	private int refuseNum;
	private int not_sayNum;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments_list);
		initUI();
		initInterface();
		initData();
	}
	

	private void initInterface() {
		mCommentInterface = Interface.getInstance();
		mCommentInterface.setPostListener(new readPartyJoinMsgListenner() {
			@Override
			public void success(String A) {
				partackList.clear();
				not_sayList.clear();
				refuseList.clear();
				Log.e("CommentsListActivity", "返回的用户参与信息" + A);
				java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
				}.getType();
				ReadPartyback partyback = GsonUtils.parseJsonArray(A, type);
				ReturnData returnData = partyback.getReturnData();
				Log.e("CommentsListActivity", "当前returnData:" + returnData.toString());
				List<Relation> relationList = returnData.getRelation();
				if (relationList.size() > 0) {
					for (int i = 0; i < relationList.size(); i++) {
						Relation relation = relationList.get(i);
						// 判断参与、拒绝数
						Integer relationship = relation.getRelationship();
						switch (relationship) {
						case 0:
							not_sayNum++;
							Log.e("CommentsListActivity", "当前not_sayNum的数量" + not_sayNum+"======="+i);
							not_sayList.add(relationList.get(i));
							break;
						case 1:
							partakeNum++;
							Log.e("CommentsListActivity", "当前partakeNum的数量" + partakeNum+"======="+i);
							partackList.add(relationList.get(i));
							break;
						case 2:
							refuseNum++;
							Log.e("CommentsListActivity", "当前refuseNum的数量" + refuseNum+"======="+i);
							refuseList.add(relationList.get(i));
							break;
						default:
							break;
						}
					}
					mComments_list_partake_count.setText(String
							.valueOf(partakeNum));
					mComments_list_refuse_count.setText(String
							.valueOf(refuseNum));
					mComments_list_not_say_count.setText(String
							.valueOf(not_sayNum));
					partakeNum = 0;
					refuseNum = 0;
					not_sayNum = 0;
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initData() {
		Intent intent = getIntent();
		commentsList_msg = intent.getIntExtra("CommentsList", 0);
		switch (commentsList_msg) {
		case 0:
			mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_1);
			mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_3);
			mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_3);
			pk_party = intent.getStringExtra("not_say");
			break;
		case 1:
			mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_1);
			mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_3);
			mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_3);
			pk_party = intent.getStringExtra("partake");
			break;
		case 2:
			mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_1);
			mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_3);
			mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_3);
			pk_party = intent.getStringExtra("refuse");
			break;
		default:
			break;
		}
		initReadParty(pk_party);
	}

	private void initReadParty(String pk_party) {
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		mCommentInterface
				.readPartyJoinMsg(CommentsListActivity.this, readparty);
	}

	private void initUI() {
		findViewById(R.id.Comments_list_back_layout).setOnClickListener(this);
		findViewById(R.id.Comments_list_back).setOnClickListener(this);// 返回
		mComments_list_partake_layout = (RelativeLayout) findViewById(R.id.Comments_list_partake_layout);
		mComments_list_partake_layout.setOnClickListener(this);// 参与
		mComments_list_refuse_layout = (RelativeLayout) findViewById(R.id.Comments_list_refuse_layout);
		mComments_list_refuse_layout.setOnClickListener(this);// 拒绝
		mComments_list_not_say_layout = (RelativeLayout) findViewById(R.id.Comments_list_not_say_layout);
		mComments_list_not_say_layout.setOnClickListener(this);// 未表态
		mComments_list_partake_count = (TextView) findViewById(R.id.Comments_list_partake_count);// 参与数量
		mComments_list_refuse_count = (TextView) findViewById(R.id.Comments_list_refuse_count);// 拒绝数量
		mComments_list_not_say_count = (TextView) findViewById(R.id.Comments_list_not_say_count);// 未表态数量
		mComments_list_listview = (ListView) findViewById(R.id.Comments_list_listview);// listview列表
		adapter = new MyAdapter();
		mComments_list_listview.setAdapter(adapter);
	}

	class ViewHolder
	{
		ImageView commentslist_item_head;
		TextView commentslist_item_nickname;
		TextView commentslist_item_status;
	}
	
	
	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			if(commentsList_msg==0)
			{
				return not_sayList.size();
			}else if(commentsList_msg==1)
			{
				return partackList.size();
			}else
			{
				return refuseList.size();
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
			View inflater=null;
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater=layoutInflater.inflate(R.layout.commentslist_item, null);
				holder.commentslist_item_head=(ImageView) inflater.findViewById(R.id.commentslist_item_head);
				holder.commentslist_item_nickname=(TextView) inflater.findViewById(R.id.commentslist_item_nickname);
				holder.commentslist_item_status=(TextView) inflater.findViewById(R.id.commentslist_item_status);
				inflater.setTag(holder);
			}else
			{
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			if(commentsList_msg==0)
			{
				Relation relation=not_sayList.get(position);
				holder.commentslist_item_status.setText("未表态");
				String useravatar_path=relation.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this, completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImage(CommentsListActivity.this,completeURL, holder.commentslist_item_head);
				holder.commentslist_item_nickname.setText(relation.getNickname());
			}else if(commentsList_msg==1)
			{
				Relation relation=partackList.get(position);
				holder.commentslist_item_status.setText("参与");
				String useravatar_path1=relation.getAvatar_path();
				completeURL = beginStr + useravatar_path1 + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this, completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImage(CommentsListActivity.this,completeURL, holder.commentslist_item_head);
				holder.commentslist_item_nickname.setText(relation.getNickname());
			}else
			{
				Relation relation=refuseList.get(position);
				holder.commentslist_item_status.setText("拒绝");
				String useravatar_path1=relation.getAvatar_path();
				completeURL = beginStr + useravatar_path1 + endStr;
				PreferenceUtils.saveImageCache(CommentsListActivity.this, completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImage(CommentsListActivity.this,completeURL, holder.commentslist_item_head);
				holder.commentslist_item_nickname.setText(relation.getNickname());
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
		case R.id.Comments_list_partake_layout:
			Comments_list_partake_layout();
			break;
		case R.id.Comments_list_refuse_layout:
			Comments_list_refuse_layout();
			break;
		case R.id.Comments_list_not_say_layout:
			Comments_list_not_say_layout();
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

	private void Comments_list_not_say_layout() {
		commentsList_msg=0;
		mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_1);
		mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_3);
		mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_3);
		initReadParty(pk_party);
		adapter.notifyDataSetChanged();
	}

	private void Comments_list_refuse_layout() {
		commentsList_msg=2;
		mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_3);
		mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_3);
		mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_1);
		initReadParty(pk_party);
		adapter.notifyDataSetChanged();
	}

	private void Comments_list_partake_layout() {
		commentsList_msg=1;
		mComments_list_not_say_layout.setBackgroundResource(R.drawable.ok_3);
		mComments_list_partake_layout.setBackgroundResource(R.drawable.ok_1);
		mComments_list_refuse_layout.setBackgroundResource(R.drawable.ok_3);
		initReadParty(pk_party);
		adapter.notifyDataSetChanged();
	}

}
