package com.BJ.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.AddFriendsback;
import com.BJ.javabean.CheckFriends;
import com.BJ.javabean.User_User;
import com.biju.Interface;
import com.biju.Interface.addFriendListenner;
import com.biju.Interface.becomeFriendListenner;
import com.biju.R;
import com.biju.function.AddFriends2Activity;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("ResourceAsColor")
public class ContactListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ContactBean> list;
	private HashMap<String, Integer> alphaIndexer; // 字母索引
	private String[] sections; // 存储每个章节
	private Context ctx; // 上下文
	private ArrayList<CheckFriends> contact_list ;// 匹配返回的总的list
	private String phone3;
	private int AddThe_phone=-1;
	private int ByAdd_phone=-1;
	private int AlreadyFriends_phone=-1;
	private int contactFriends_phone=-1;
	
	private String AddThe_path;
	private String ByAdd_path;
	private String AlreadyFriends_path;
	private String contactFriends_path;
	
	private String AddThe_name;
	private String ByAdd_name;
	private String AlreadyFriends_name;
	private String contactFriends_name;
	
	private Integer AddThe_Fk_user_to=-1;
	private Integer ByAdd_Fk_user_to=-1;
	private Integer AlreadyFriends_Fk_user_to=-1;
	private Integer contactFriends_Fk_user=-1;
	
	
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private Integer fk_user_from;
	private Interface contactInterface;
	
	public ContactListAdapter(Context context, List<ContactBean> list,
			QuickAlphabeticBar alpha,ArrayList<CheckFriends> contact_list,Integer fk_user_from) {
		this.ctx = context;
		this.contact_list = contact_list;
		this.fk_user_from = fk_user_from;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.alphaIndexer = new HashMap<String, Integer>();
		this.sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			ContactBean contactBean = list.get(i);
			String phone = contactBean.getPhoneNum();
			char phone_one = phone.charAt(0);
			if ("1".equals(String.valueOf(phone_one))) {
				// 得到字母
				String name = getAlpha(list.get(i).getSortKey());
				if (!alphaIndexer.containsKey(name)) {
					alphaIndexer.put(name, i);
				}
			} else {
				list.remove(contactBean);
			}

		}

		initInterface();
		
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);

		alpha.setAlphaIndexer(alphaIndexer);
	}

	private void initInterface() {
		contactInterface = Interface.getInstance();
		// 被添加者同意添加之后的监听
		contactInterface.setPostListener(new becomeFriendListenner() {

			@Override
			public void success(String A) {
				Log.e("ContactListAdapter", "返回成为好友关系后的结果=====" + A);
			}

			@Override
			public void defail(Object B) {
			}
		});
		
		// 已绑定手机的通讯录中添加好友的监听
		contactInterface.setPostListener(new addFriendListenner() {

			@Override
			public void success(String A) {
				AddFriendsback addFriendsback = GsonUtils.parseJson(A,AddFriendsback.class);
				int status = addFriendsback.getStatusMsg();
				if (status == 1) {
					Log.e("AddFriendsActivity", "返回是否添加成功=======" + A);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item_3, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			//普通布局
			holder.Contact_ordinary_quickContactBadge = (ImageView) convertView.findViewById(R.id.Contact_ordinary_qcb);
			holder.Contact_ordinary_name = (TextView) convertView.findViewById(R.id.Contact_ordinary_name);
			holder.Contact_ordinary_prompt = (TextView) convertView.findViewById(R.id.Contact_ordinary_prompt);
			holder.Contact_ordinary_layout=(RelativeLayout) convertView.findViewById(R.id.Contact_ordinary_layout);
			holder.Contact_ordinary_but=(RelativeLayout) convertView.findViewById(R.id.Contact_ordinary_but);
			//有账号的
			holder.Contact_quickContactBadge = (ImageView) convertView.findViewById(R.id.Contact_qcb);
			holder.Contact_name = (TextView) convertView.findViewById(R.id.Contact_name);
			holder.Contact_contact_name = (TextView) convertView.findViewById(R.id.Contact_contact_name);
			holder.Contact_prompt = (TextView) convertView.findViewById(R.id.Contact_prompt);
			holder.Contact_layout=(RelativeLayout) convertView.findViewById(R.id.Contact_layout);
			holder.Contact_but=(RelativeLayout) convertView.findViewById(R.id.Contact_but);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ContactBean contact = list.get(position);
		final String phone = contact.getPhoneNum();
		char phone_one = phone.charAt(0);
		if ("1".equals(String.valueOf(phone_one))) {
				String phone1 = contact.getPhoneNum();
				if (phone1.length() > 11) {
					String number1 = phone1.substring(0, 3);
					String number2 = phone1.substring(4, 8);
					String number3 = phone1.substring(9, 13);
					phone3 = number1 + number2 + number3;
				} else {
					phone3 = phone1;
				}
				for (int j = 0; j < contact_list.size(); j++) {
					CheckFriends checkFriends = contact_list.get(j);
					String phone2 = checkFriends.getPhone();
//					Log.e("ContactListAdapter", "此时的phone2===========" + phone2);
					if (phone3.equals(phone2)) {
//						Log.e("ContactListAdapter", "此时的phone1==========="+ phone3);
						if ("1".equals(String.valueOf(checkFriends.getRelationship()))) {
							AddThe_phone=position;
							AddThe_path=checkFriends.getAvatar_path();
							AddThe_name=checkFriends.getNickname();
							AddThe_Fk_user_to=checkFriends.getFk_user_to();
						} else if ("2".equals(String.valueOf(checkFriends.getRelationship()))) {
							ByAdd_phone=position;
							ByAdd_path=checkFriends.getAvatar_path();
							ByAdd_name=checkFriends.getNickname();
							ByAdd_Fk_user_to=checkFriends.getFk_user_to();
						} else if ("3".equals(String.valueOf(checkFriends.getRelationship()))) {
							AlreadyFriends_phone=position;
							AlreadyFriends_path=checkFriends.getAvatar_path();
							AlreadyFriends_name=checkFriends.getNickname();
							AlreadyFriends_Fk_user_to=checkFriends.getFk_user_to();
						} else {
							contactFriends_phone=position;
							contactFriends_path=checkFriends.getAvatar_path();
							contactFriends_name=checkFriends.getNickname();
							contactFriends_Fk_user=checkFriends.getPk_user();
						}
					}
			}
				if(position==AddThe_phone)
				{
					Log.e("ContactListAdapter", "此时的position=========="+position);
					Log.e("ContactListAdapter", "此时的AddThe_phone=========="+AddThe_phone);
					holder.Contact_ordinary_layout.setVisibility(View.GONE);
					holder.Contact_layout.setVisibility(View.VISIBLE);
					String name = contact.getDesplayName();
					holder.Contact_name.setText(AddThe_name);
					holder.Contact_contact_name.setText("通讯录名称:"+name);
					holder.Contact_prompt.setText("已发出邀请");
					completeURL = beginStr + AddThe_path + endStr+"mini-avatar";
					ImageLoaderUtils(holder);
					holder.Contact_but.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(ctx, "发出邀请", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(position==ByAdd_phone)
				{
					holder.Contact_ordinary_layout.setVisibility(View.GONE);
					holder.Contact_layout.setVisibility(View.VISIBLE);
					String name = contact.getDesplayName();
					holder.Contact_name.setText(ByAdd_name);
					holder.Contact_contact_name.setText("通讯录名称:"+name);
					holder.Contact_prompt.setText("同意添加");
					completeURL = beginStr + ByAdd_path + endStr+"mini-avatar";
					ImageLoaderUtils(holder);
					holder.Contact_but.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(ctx, "添加成功", Toast.LENGTH_SHORT).show();
							User_User user_User = new User_User();
							user_User.setFk_user_to(ByAdd_Fk_user_to);
							user_User.setFk_user_from(fk_user_from);
							contactInterface.becomeFriend(ctx, user_User);
						}
					});
				}else if(position==AlreadyFriends_phone)
				{
					holder.Contact_ordinary_layout.setVisibility(View.GONE);
					holder.Contact_layout.setVisibility(View.VISIBLE);
					String name = contact.getDesplayName();
					holder.Contact_name.setText(AlreadyFriends_name);
					holder.Contact_contact_name.setText("通讯录名称:"+name);
					holder.Contact_prompt.setText("已为好友");
					completeURL = beginStr + AlreadyFriends_path + endStr+"mini-avatar";
					ImageLoaderUtils(holder);
				}else if(position==contactFriends_phone)
				{
					holder.Contact_ordinary_layout.setVisibility(View.GONE);
					holder.Contact_layout.setVisibility(View.VISIBLE);
					String name = contact.getDesplayName();
					holder.Contact_name.setText(contactFriends_name);
					holder.Contact_contact_name.setText("通讯录名称:"+name);
					holder.Contact_prompt.setText("成为好友");
					completeURL = beginStr + contactFriends_path + endStr+"mini-avatar";
					ImageLoaderUtils(holder);
					holder.Contact_but.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(ctx, "已发送请求", Toast.LENGTH_SHORT).show();
							User_User user_User = new User_User();
							user_User.setFk_user_from(fk_user_from);
							user_User.setFk_user_to(contactFriends_Fk_user);
							user_User.setRelationship(1);
							user_User.setStatus(1);
							contactInterface.addFriend(ctx,user_User);
							holder.Contact_prompt.setText("已发出邀请");
						}
					});
				}else
				{
					holder.Contact_ordinary_layout.setVisibility(View.VISIBLE);
					holder.Contact_layout.setVisibility(View.GONE);
					String name = contact.getDesplayName();
					holder.Contact_ordinary_name.setText(name);
					holder.Contact_ordinary_prompt.setText("邀请加入");
					holder.Contact_ordinary_quickContactBadge.setVisibility(View.INVISIBLE);
					holder.Contact_ordinary_but.setOnClickListener(new OnClickListener() {
						
						private String localphone4;

						@Override
						public void onClick(View v) {
							final String localphone=contact.getPhoneNum();
							if (localphone.length() > 11) {
								String localphone1 = localphone.substring(0, 3);
								String localphone2 = localphone.substring(4, 8);
								String localphone3 = localphone.substring(9, 13);
								localphone4 = localphone1 + localphone2 + localphone3;
							} else {
								localphone4 = localphone;
							}
							//发送短信
							Toast.makeText(ctx, "邀请加入"+localphone4, Toast.LENGTH_SHORT).show();
							Uri smsToUri = Uri.parse("smsto:"+localphone4); 
							Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri ); 
							mIntent.putExtra("sms_body", "亲，快下载必聚软件吧，好用又好玩哦~"); 
							ctx.startActivity( mIntent );
						}
					});
				}
			
			// 当前字母
			String currentStr = getAlpha(contact.getSortKey());
			// 前面的字母
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
					position - 1).getSortKey()) : " ";

			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	private void ImageLoaderUtils(ViewHolder holder) {
		PreferenceUtils.saveImageCache(ctx, completeURL);
		ImageLoaderUtils.getInstance().LoadImageCricular(
				ctx, completeURL,
				holder.Contact_quickContactBadge);
	}

	private static class ViewHolder {
		TextView alpha;
		ImageView Contact_ordinary_quickContactBadge;
		TextView Contact_ordinary_name;
		TextView Contact_ordinary_prompt;
		RelativeLayout Contact_ordinary_layout;
		RelativeLayout Contact_ordinary_but;
		
		ImageView Contact_quickContactBadge;
		TextView Contact_name;
		TextView Contact_contact_name;
		TextView Contact_prompt;
		RelativeLayout Contact_layout;
		RelativeLayout Contact_but;
		
	}

	/**
	 * 获取首字母
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式匹配
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 将小写字母转换为大写
		} else {
			return "#";
		}
	}
}
