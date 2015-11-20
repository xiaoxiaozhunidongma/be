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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.biju.Interface;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("UseSparseArrays")
public class AddChatsActivity extends Activity implements OnClickListener, OnItemClickListener {

	private ListView listView;
	private MyAdapter myAdapter;
	private List<User> userList=new ArrayList<User>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	final ArrayList<String> members=new ArrayList<String>();
	final ArrayList<String> NicNameList=new ArrayList<String>();
	@SuppressWarnings("unused")
	private HashMap<Integer, String> HasKnowFromAvaUrlMap=new HashMap<Integer, String>();
	private HashMap<Integer, Boolean> isSelectMap=new HashMap<Integer, Boolean>();
	private boolean isSelected;
	
	
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
		listView.setOnItemClickListener(this);
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
				Log.e("AddFriends3Activity", "���ؽ��"+A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				userList = loginbackread.getReturnData();
				myAdapter.notifyDataSetChanged();//ˢ��
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
			ImageLoaderUtils.getInstance().LoadImageCricular(AddChatsActivity.this,
					completeURL, holder.ReadUserAllFriends_head);
			if(position==userList.size()-1){
				holder.ReadUserAllFriendsLine1.setVisibility(View.VISIBLE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.GONE);
			}else {
				holder.ReadUserAllFriendsLine1.setVisibility(View.GONE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.VISIBLE);
			}
			
			isSelectMap.put(position, false);//��������Ĭ��Ϊfalse ��δѡ��
			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.AddFriends3OK:
		case R.id.AddFriends3OKLayout:
			
			if(members.size()!=0){
				AddFriends3OK();
			}else{
				Toast.makeText(AddChatsActivity.this, "����ӶԻ���Ա", Toast.LENGTH_SHORT).show();
			}
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
		    // Tom ���Լ���������ΪclientId����ȡAVIMClient����ʵ��
			final Integer SD_pk_user = SdPkUser.getsD_pk_user();
			members.add(String.valueOf(SD_pk_user));//��ӵ�ǰ�û�
			Log.e("AddChatsActivity", "members===="+members+members.size());
		    AVIMClient curuser = AVIMClient.getInstance(String.valueOf(SD_pk_user));
		    // �����������
		    curuser.open(new AVIMClientCallback() {
		      @Override
		      public void done(AVIMClient client, AVIMException e) {
		        if (e == null) {
		          // ������ Jerry��Bob,Harry,William ֮��ĻỰ
		            HashMap<String,Object> attr = new HashMap<String,Object>();
		            
		            attr.put("type",3);//1��Ⱥ�� ��3��������
		            attr.put("isupdate", false);
					String convName="";
					for (int i = 0; i < NicNameList.size(); i++) {
							
						if(NicNameList.size()!=1){
							if(i!=NicNameList.size()-1){
								convName=convName+NicNameList.get(i)+",";
							}
							if(i==NicNameList.size()-1){
								convName=convName+NicNameList.get(i)+"�ĶԻ�";
							}
						}else{
							convName=NicNameList.get(i);
						}
					}
		            
		          client.createConversation(members, convName, attr,
		              new AVIMConversationCreatedCallback() {

		                @Override
		                public void done(AVIMConversation conversation, AVIMException e) {
		                  if (e == null) {
		                	  Log.e("AddChatsActivity", "�����ҶԻ������ɹ���");
		          			final ChatManager chatManager = ChatManager.getInstance();
								 chatManager.registerConversation(conversation);//ע��Ի�
								 members.clear();//������
								 NicNameList.clear();
								 finish();//����
		              		//�ϴ�OSS
//		              		OSSupload(ossData, bitmap2Bytes,uUid);
//		                    AVIMTextMessage msg = new AVIMTextMessage();
//		                    msg.setText("�������Ķ���");
//		                    // ������Ϣ
//		                    conversation.sendMessage(msg, new AVIMConversationCallback() {
//
//		                      @Override
//		                      public void done(AVIMException e) {
//		                        if (e == null) {
//		                          Log.d("Tom & Jerry", "���ͳɹ���");
//		                        }
//		                      }
//		                    });
		                  }
		                }
		              });
		        }
		      }
		    });
		    
		  }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		isSelected = isSelectMap.get(position);
		isSelected=!isSelected;
		isSelectMap.put(position, isSelected);
		User user1 = userList.get(position);
		if(isSelected){
			view.findViewById(R.id.ReadUserAllFriends_choose).setVisibility(View.VISIBLE);
//			holder.ReadUserAllFriends_choose.setVisibility(View.VISIBLE);
				members.add(String.valueOf(user1.getPk_user()));
			NicNameList.add(user1.getNickname());
		}else {
			view.findViewById(R.id.ReadUserAllFriends_choose).setVisibility(View.GONE);
//			holder.ReadUserAllFriends_choose.setVisibility(View.GONE);
			members.remove(String.valueOf(user1.getPk_user()));
			NicNameList.remove(user1.getNickname());
		}
	
	}

}
