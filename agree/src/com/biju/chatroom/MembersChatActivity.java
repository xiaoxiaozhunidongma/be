package com.biju.chatroom;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

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

public class MembersChatActivity extends Activity implements OnClickListener,OnItemClickListener{

	private ListView mMembersChatListview;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String completeURL;
	private ArrayList<User> membersChatList=new ArrayList<User>();
	private MyMemberChatAdapter adapter;
	private Integer sd_pk_user;
	private boolean source;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_members_chat);
		sd_pk_user = SdPkUser.getsD_pk_user();
//<<<<<<< HEAD
//		
//		List<String> members = ChatActivityLean.conversation.getMembers();
//		members.remove(String.valueOf(sd_pk_user));//�Ƴ���ǰmenber
//		membersChatList.clear();
//		for (int i = 0; i < members.size(); i++) {
//			String string = members.get(i);
//			User user = FriendsFragment.AllFriendsMap.get(Integer.valueOf(string));
//			membersChatList.add(user);
//		}
//		membersChatList.add(HomeFragment.readuser);
//		initUI();
//		
////		membersChatList = SdPkUser.getUser();
////		if(membersChatList.size()>0){
////			mMembersChatListview.setAdapter(adapter);
////		}
//=======
		Intent intent = getIntent();
		source = intent.getBooleanExtra("PersonalData", false);
		initUI();
		membersChatList = SdPkUser.getUser();
	}

	private void initUI() {
		findViewById(R.id.MembersChatShowLayout).setOnClickListener(this);
		findViewById(R.id.MembersChatExitGroupChatLayout).setOnClickListener(this);//�˳�Ⱥ��
		findViewById(R.id.MembersChatAddMembersLayout).setOnClickListener(this);//��ӳ�Ա
		findViewById(R.id.MembersChatChangeNameLayout).setOnClickListener(this);//����
		findViewById(R.id.MembersChatNoShowLayout).setOnClickListener(this);
		findViewById(R.id.MembersChatOKLayout).setOnClickListener(this);//�ر�
		findViewById(R.id.MembersChatBackLayout).setOnClickListener(this);//����
		mMembersChatListview = (ListView) findViewById(R.id.MembersChatListview);
		adapter = new MyMemberChatAdapter();
		mMembersChatListview.setAdapter(adapter);
		mMembersChatListview.setOnItemClickListener(this);
	}

	class ViewHolder{
		ImageView MemberChat_head;
		TextView MemberChat_name;
		TextView MemberChat_role;
		TextView MemberChat_line_1;
		TextView MemberChat_line_2;
		TextView MemberChat_delete;
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
				holder.MemberChat_delete=(TextView) inflater.findViewById(R.id.MemberChat_delete);
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
					holder.MemberChat_role.setText("�鳤");
				} else {
					holder.MemberChat_role.setText("��Ա");
				}

				if(sd_pk_user.equals(Integer.valueOf(CreatorId))){
					holder.MemberChat_delete.setVisibility(View.VISIBLE);
				}
				
				String useravatar_path = user.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr+"mini-avatar";
				PreferenceUtils.saveImageCache(MembersChatActivity.this,completeURL);// ��SP
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
						//ɾ���������еĳ�Ա.....δ��
						final SweetAlertDialog sd = new SweetAlertDialog(MembersChatActivity.this);
						sd.setTitleText("��ʾ");
						sd.setContentText("���Ҫɾ���ó�Ա��");
						sd.setCancelText("��������");
						sd.setConfirmText("�ǵ�");
						sd.showCancelButton(true);
						sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sd.cancel();
							}
						}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sd.cancel();
								//ɾ��
							}
						}).show();
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
			MembersChatChangeNameLayout();
			break;
		case R.id.MembersChatAddMembersLayout:
			MembersChatAddMembers();
			break;
		case R.id.MembersChatExitGroupChatLayout:
			MembersChatExitGroup();
			break;
		default:
			break;
		}
	}

	//�˳�Ⱥ��
	private void MembersChatExitGroup() {
		final SweetAlertDialog sd = new SweetAlertDialog(MembersChatActivity.this);
		sd.setTitleText("��ʾ");
		sd.setContentText("���Ҫ�˳����Ⱥ����?");
		sd.setCancelText("��������");
		sd.setConfirmText("�ǵ�");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				ExitChatRoom();
			}
		}).show();

		
	}

	private void ExitChatRoom() {
		AVIMClient tom = AVIMClient.getInstance(String.valueOf(sd_pk_user));
		tom.open(new AVIMClientCallback(){

			@Override
			public void done(AVIMClient client, AVIMException e) {
			      if(e==null){
				      //��¼�ɹ�
				        final AVIMConversation conv = client.getConversation(ChatActivityLean.conversation.getConversationId());
				        conv.join(new AVIMConversationCallback(){
				            @Override
				            public void done(AVIMException e){
				              if(e==null){
				              //����ɹ�
				              conv.quit(new AVIMConversationCallback(){
				                @Override
				                public void done(AVIMException e){
				                  if(e==null){
				                  //�˳��ɹ�
				                	//�˳�Ⱥ��
										Log.e("MembersCha", sd_pk_user+"�˳�Ⱥ�ĳɹ�~");
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

	//��ӳ�Ա
	private void MembersChatAddMembers() {
		Intent intent=new Intent(MembersChatActivity.this, AddMembersActivity.class);
		startActivity(intent);
	}

	//��������������
	private void MembersChatChangeNameLayout() {
		Intent intent=new Intent(MembersChatActivity.this, ChangeChatNameActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void MembersChatBackLayout() {
		finish();
		Intent intent = new Intent(MembersChatActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	//�رճ�Ա����
	private void MembersChatOKLayout() {
		ChatActivityLean.memberChat.MemberChat();
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
		if(source){
			PersonalDataActivity.getClose.Close();
		}else {
			ChatActivityLean.getClose.Close();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			MembersChatOKLayout();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SdPkUser.setGetSource(4);
		User user = membersChatList.get(position);
		boolean isOpen=SdPkUser.GetChatRoomOpen;
		if(isOpen){
			PersonalDataActivity.getClose.Close();
			PersonalDataActivity.chatRoomRefreshData.ChatRoomRefreshData(user);
		}else {
			ChatActivityLean.memberChat.MemberChat();//�򿪺�����Ϣ����ʱ�ı���
			SdPkUser.setChatRoomUser(user);
			Intent intent=new Intent(MembersChatActivity.this, PersonalDataActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.leftin_item, R.anim.leftout_item);
		}
		finish();
	}
}
