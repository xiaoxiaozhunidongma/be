package com.biju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.ChatManager;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.biju.Interface.MyAllfriendsListenner;
import com.github.volley_examples.utils.GsonUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddFriends3Activity extends Activity implements OnClickListener, OnItemClickListener {

	private ListView listView;
	private MyAdapter myAdapter;
	private List<User> userList=new ArrayList<User>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	 final ArrayList<String> members=new ArrayList<String>();
		private HashMap<Integer, String> HasKnowFromAvaUrlMap=new HashMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends3);
		findViewById(R.id.tv_ok).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listView1);
		readAllfriends();
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
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View inflater = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(
						R.layout.readuserallfriends_item, null);
				holder.ReadUserAllFriends_head = (ImageView) inflater
						.findViewById(R.id.ReadUserAllFriends_head);
				holder.ReadUserAllFriends_name = (TextView) inflater
						.findViewById(R.id.ReadUserAllFriends_name);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			
			User user = userList.get(position);
			String nickname = user.getNickname();       
			String avatar_path = user.getAvatar_path();
			holder.ReadUserAllFriends_name.setText(nickname);
			String completeURL = beginStr + avatar_path + endStr+"mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(AddFriends3Activity.this,
					completeURL, holder.ReadUserAllFriends_head);
			
			return inflater;
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
		case R.id.tv_ok:
			createConv_ok();
			break;

		default:
			break;
		}
	}

	private void createConv_ok() {
		    // Tom ���Լ���������ΪclientId����ȡAVIMClient����ʵ��
			Integer SD_pk_user = SdPkUser.getsD_pk_user();
			members.add(String.valueOf(SD_pk_user));//��ӵ�ǰ�û�
			Log.e("AddFriend3Activity", "members===="+members+members.size());
		    AVIMClient curuser = AVIMClient.getInstance(String.valueOf(SD_pk_user));
		    // �����������
		    curuser.open(new AVIMClientCallback() {
		      @Override
		      public void done(AVIMClient client, AVIMException e) {
		        if (e == null) {
		          // ������ Jerry��Bob,Harry,William ֮��ĻỰ
		            HashMap<String,Object> attr = new HashMap<String,Object>();
		            
		            attr.put("type",3);//1��Ⱥ�� ��3��������

					String convName="";
					for (int i = 0; i < members.size(); i++) {
						if(i!=members.size()-1){
							convName=convName+members.get(i)+",";
						}
						if(i==members.size()-1){
							convName=convName+members.get(i)+"�ĶԻ�";
						}
					}
		            
		          client.createConversation(members, convName, attr,
		              new AVIMConversationCreatedCallback() {

		                @Override
		                public void done(AVIMConversation conversation, AVIMException e) {
		                  if (e == null) {
		                	  Log.e("AddFriend3Activity", "�����ҶԻ������ɹ���");
		          			final ChatManager chatManager = ChatManager.getInstance();
								 chatManager.registerConversation(conversation);//ע��Ի�
								 members.clear();//������
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
		User user = userList.get(position);
		members.add(String.valueOf(user.getPk_user()));
	}

}
