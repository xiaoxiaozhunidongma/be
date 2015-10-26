package com.biju.chatroom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.biju.MainActivity;
import com.biju.R;
import com.example.testleabcloud.ChatActivityLean;

public class MembersChatActivity extends Activity implements OnClickListener{

	private ListView mMembersChatListview;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private List<User> membersChatList;
	private MyMemberChatAdapter adapter;
	private Integer sd_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_members_chat);
		sd_pk_user = SdPkUser.getsD_pk_user();
		initUI();
		membersChatList = SdPkUser.getUser();
		if(membersChatList.size()>0){
			mMembersChatListview.setAdapter(adapter);
		}
	}

	private void initUI() {
		findViewById(R.id.MembersChatExitGroup).setOnClickListener(this);//退出群聊
		findViewById(R.id.MembersChatExitGroupChatLayout).setOnClickListener(this);
		findViewById(R.id.MembersChatAddMembers).setOnClickListener(this);//添加成员
		findViewById(R.id.MembersChatAddMembersLayout).setOnClickListener(this);
		findViewById(R.id.MembersChatChangeNameLayout).setOnClickListener(this);//改名
		findViewById(R.id.MembersChatChangeNameBut).setOnClickListener(this);
		findViewById(R.id.MembersChatNoShowLayout).setOnClickListener(this);
		findViewById(R.id.MembersChatOKLayout).setOnClickListener(this);//关闭
		findViewById(R.id.MembersChatBackLayout).setOnClickListener(this);//返回
		mMembersChatListview = (ListView) findViewById(R.id.MembersChatListview);
		adapter = new MyMemberChatAdapter();
	}

	class ViewHolder{
		ImageView MemberChat_head;
		TextView MemberChat_name;
		TextView MemberChat_role;
		TextView MemberChat_line_1;
		TextView MemberChat_line_2;
		Button MemberChat_delete;
	}

	class MyMemberChatAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return membersChatList.size();
		}

		@Override
		public Object getItem(int arg0) {
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
				inflater = layoutInflater.inflate(R.layout.memberchat_item, null);
				holder.MemberChat_head = (ImageView) inflater.findViewById(R.id.MemberChat_head);
				holder.MemberChat_name = (TextView) inflater.findViewById(R.id.MemberChat_name);
				holder.MemberChat_role = (TextView) inflater.findViewById(R.id.MemberChat_role);
				holder.MemberChat_line_1 = (TextView) inflater.findViewById(R.id.MemberChat_line_1);
				holder.MemberChat_line_2 = (TextView) inflater.findViewById(R.id.MemberChat_line_2);
				holder.MemberChat_delete=(Button) inflater.findViewById(R.id.MemberChat_delete);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			if (membersChatList.size() > 0) {
				User user = membersChatList.get(position);
				holder.MemberChat_name.setText(user.getNickname());
				Integer pk_user=user.getPk_user();
				String CreatorId=SdPkUser.getCreator();
				if (pk_user.equals(Integer.valueOf(CreatorId))) {
					holder.MemberChat_role.setText("组长");
				} else {
					holder.MemberChat_role.setText("组员");
				}

				if(sd_pk_user.equals(Integer.valueOf(CreatorId))){
					holder.MemberChat_delete.setVisibility(View.VISIBLE);
				}
				
				String useravatar_path = user.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr+"mini-avatar";
				PreferenceUtils.saveImageCache(MembersChatActivity.this,completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImageCricular(MembersChatActivity.this, completeURL, holder.MemberChat_head);
				if (position < membersChatList.size() - 1) {
					holder.MemberChat_line_1.setVisibility(View.GONE);
					holder.MemberChat_line_2.setVisibility(View.VISIBLE);
				} else {
					holder.MemberChat_line_1.setVisibility(View.VISIBLE);
					holder.MemberChat_line_2.setVisibility(View.GONE);
				}
				
				holder.MemberChat_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//删除聊天室中的成员.....未做
					}
				});
			}
			return inflater;
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.MembersChatOKLayout:
			MembersChatOKLayout();
			break;
		case R.id.MembersChatNoShowLayout:
			MembersChatOKLayout();
			break;
		case R.id.MembersChatBackLayout:
			MembersChatBackLayout();
			break;
		case R.id.MembersChatChangeNameLayout:
		case R.id.MembersChatChangeNameBut:
			MembersChatChangeNameLayout();
			break;
		case R.id.MembersChatAddMembersLayout:
		case R.id.MembersChatAddMembers:
			MembersChatAddMembers();
			break;
		case R.id.MembersChatExitGroupChatLayout:
		case R.id.MembersChatExitGroup:
			MembersChatExitGroup();
			break;
		default:
			break;
		}
	}

	//退出群聊。。。。未做
	private void MembersChatExitGroup() {
		AVIMClient tom = AVIMClient.getInstance(String.valueOf(sd_pk_user));
		tom.open(new AVIMClientCallback(){

			@Override
			public void done(AVIMClient client, AVIMException e) {
			      if(e==null){
				      //登录成功
				        final AVIMConversation conv = client.getConversation(ChatActivityLean.conversation.getConversationId());
				        conv.join(new AVIMConversationCallback(){
				            @Override
				            public void done(AVIMException e){
				              if(e==null){
				              //加入成功
				              conv.quit(new AVIMConversationCallback(){
				                @Override
				                public void done(AVIMException e){
				                  if(e==null){
				                  //退出成功
				                	  Log.e("MembersCha", sd_pk_user+"退出群聊成功~");
				                	  Intent intent=new Intent(MembersChatActivity.this, MainActivity.class);
									startActivity(intent);
				                  }
				                } 
				              });
				              }
				            }
				        });
				      }
				    }
		});

		
	}

	//添加成员。。。。。未做
	private void MembersChatAddMembers() {
		Intent intent=new Intent(MembersChatActivity.this, AddMembersActivity.class);
		startActivity(intent);
	}

	//更改聊天室名称
	private void MembersChatChangeNameLayout() {
		Intent intent=new Intent(MembersChatActivity.this, ChangeChatNameActivity.class);
		startActivity(intent);
	}

	private void MembersChatBackLayout() {
		finish();
		Intent intent = new Intent(MembersChatActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	//关闭成员界面
	private void MembersChatOKLayout() {
		ChatActivityLean.memberChat.MemberChat();
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			MembersChatBackLayout();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
