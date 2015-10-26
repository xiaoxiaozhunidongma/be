package com.biju.chatroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.ChatManager;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.biju.Interface;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.R;
import com.example.testleabcloud.ChatActivityLean;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("UseSparseArrays")
public class AddMembersActivity extends Activity implements OnClickListener {

	private ListView listView;
	private MyAdapter myAdapter;
	private List<User> userList=new ArrayList<User>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	final ArrayList<String> members=new ArrayList<String>();
	@SuppressWarnings("unused")
	private HashMap<Integer, String> HasKnowFromAvaUrlMap=new HashMap<Integer, String>();
	private boolean isChoose;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends3);
		initUI();
		readAllfriends();
	}
	
	private void initUI() {
		findViewById(R.id.AddFriends3OKLayout).setOnClickListener(this);
		findViewById(R.id.AddFriends3OK).setOnClickListener(this);
		findViewById(R.id.AddFriends3BackLayout).setOnClickListener(this);
		findViewById(R.id.AddFriends3Back).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setDividerHeight(0);
		myAdapter = new MyAdapter();
		listView.setAdapter(myAdapter);
	}

	private void readAllfriends() {
		Interface instance = Interface.getInstance();
		User user=new User();
		Integer getsD_pk_user = SdPkUser.getsD_pk_user();
		user.setPk_user(getsD_pk_user);
		instance.readMyAllfriend(this, user);
		instance.setPostListener(new MyAllfriendsListenner() {
			

			@Override
			public void success(String A) {
				Log.e("AddFriends3Activity", "返回结果"+A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				userList = loginbackread.getReturnData();
				myAdapter.notifyDataSetChanged();//刷新
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	class ViewHolder {
		ImageView ReadUserAllFriends_head;
		TextView ReadUserAllFriends_name;
		TextView ReadUserAllFriendsLine1;
		TextView ReadUserAllFriendsLine2;
		ImageView ReadUserAllFriends_choose;
		RelativeLayout ReadUserAllFriendsLayout;
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return userList.size();
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
				inflater = layoutInflater.inflate(R.layout.readuserallfriends_item, null);
				holder.ReadUserAllFriends_head = (ImageView) inflater.findViewById(R.id.ReadUserAllFriends_head);
				holder.ReadUserAllFriends_name = (TextView) inflater.findViewById(R.id.ReadUserAllFriends_name);
				holder.ReadUserAllFriendsLine1=(TextView) inflater.findViewById(R.id.ReadUserAllFriendsLine1);
				holder.ReadUserAllFriendsLine2=(TextView) inflater.findViewById(R.id.ReadUserAllFriendsLine2);
				holder.ReadUserAllFriends_choose=(ImageView) inflater.findViewById(R.id.ReadUserAllFriends_choose);
				holder.ReadUserAllFriendsLayout=(RelativeLayout) inflater.findViewById(R.id.ReadUserAllFriendsLayout);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			
			User user = userList.get(position);
			Integer pk_user=user.getPk_user();
			String nickname = user.getNickname();       
			String avatar_path = user.getAvatar_path();
			holder.ReadUserAllFriends_name.setText(nickname);
			String completeURL = beginStr + avatar_path + endStr+"mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(AddMembersActivity.this,
					completeURL, holder.ReadUserAllFriends_head);
			if(position==userList.size()-1){
				holder.ReadUserAllFriendsLine1.setVisibility(View.VISIBLE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.GONE);
			}else {
				holder.ReadUserAllFriendsLine1.setVisibility(View.GONE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.VISIBLE);
			}
			Choose(holder,position,pk_user);
			return inflater;
		}

		private void Choose(final ViewHolder holder, final int position, final Integer pk_user) {
			holder.ReadUserAllFriendsLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isChoose=!isChoose;
					if(isChoose){
						holder.ReadUserAllFriends_choose.setVisibility(View.VISIBLE);
						User user1 = userList.get(position);
						members.add(String.valueOf(user1.getPk_user()));
					}else {
						holder.ReadUserAllFriends_choose.setVisibility(View.GONE);
					}
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_friends3, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.AddFriends3OK:
		case R.id.AddFriends3OKLayout:
			AddFriends3OK();
			break;
		case R.id.AddFriends3BackLayout:
		case R.id.AddFriends3Back:
			AddFriends3Back();
			break;
		default:
			break;
		}
	}

	private void AddFriends3Back() {
		finish();
	}

	private void AddFriends3OK() {
		    // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
			Integer SD_pk_user = SdPkUser.getsD_pk_user();
			
			 AVIMClient currUser = AVIMClient.getInstance(String.valueOf(SD_pk_user));
			 currUser.open(new AVIMClientCallback() {

			      @Override
			      public void done(AVIMClient client, AVIMException e) {
			        if (e == null) {
			          //登录成功
			          final AVIMConversation conv = client.getConversation(ChatActivityLean.conversation.getConversationId());
			          conv.join(new AVIMConversationCallback() {
			            @Override
			            public void done(AVIMException e) {
			              if (e == null) {
			                //加入成功
			                conv.addMembers(members, new AVIMConversationCallback() {
			                  @Override
			                  public void done(AVIMException e) {
			                	  Log.e("AddMembersActivity", "添加成员成功："+members.toString());
			                	  
			                	  List<String> list = ChatActivityLean.conversation.getMembers();
			  					String convName="";
								for (int i = 0; i < list.size(); i++) {
									if(i!=list.size()-1){
										convName=convName+list.get(i)+",";
									}
									if(i==list.size()-1){
										convName=convName+list.get(i)+"的对话";
									}
								}
								ChatActivityLean.conversation.setName(convName);
								
			                	  finish();
			                  }
			                });
			              }
			            }
			          });
			        }
			      }
			    });

		  }
}
