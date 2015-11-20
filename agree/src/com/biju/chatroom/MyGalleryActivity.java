package com.biju.chatroom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.Photo;
import com.BJ.javabean.User;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.avos.avoscloud.LogUtil.log;
import com.biju.HackyViewPager;
import com.biju.Interface;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.utils.DensityUtil;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.biju.HackyViewPager;
import com.biju.R;


public class MyGalleryActivity extends Activity implements OnClickListener {

	private HackyViewPager mViewPager;
	private static final String ISLOCKED_ARG = "isLocked";
	public static ArrayList<String> netFullpath=new ArrayList<String>();
	private static int curposition;
	private ImageView iv_jianhao;
	private int iv_jianhao_height;
	private RelativeLayout rela_slideup;
	private RelativeLayout rela_translucent;
	private int windowHeight;
	private static String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private ImageView userhead;
	private TextView username;
	private TextView creattime;
	public static ArrayList<Photo> listphotos;
	private Interface instance;
	private SamplePagerAdapter samplePagerAdapter;
//	public HashMap<Integer, User> UsersMap=new HashMap<Integer, User>();
	private List<User> users=new ArrayList<User>();
private EditText et_sendmessage;
private Button btn_send;
private ListView review_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		Intent intent = getIntent();
		curposition = intent.getIntExtra("position", -1);
		listphotos = (ArrayList<Photo>) intent.getSerializableExtra("listphotos");
		initReadCurUser();
		
		initUI();

			samplePagerAdapter = new SamplePagerAdapter();
			mViewPager.setAdapter(samplePagerAdapter);
			if (savedInstanceState != null) {
				boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
				((HackyViewPager) mViewPager).setLocked(isLocked);
			}
			
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
//					Log.e("onPageSelected", "第一次进来是否调用~");//第一次不调用
					Photo photo = listphotos.get(position); 
					String create_time = photo.getCreate_time();
					Integer fk_user = photo.getFk_user();
					// 此处user已经被加载好了，如果没加载好要做处理
					if(users.size()>0){
						for (int i = 0; i < users.size(); i++) {
							User user = users.get(i);
							Integer pk_user = user.getPk_user();
							if(String.valueOf(pk_user).equals(String.valueOf(fk_user))){
								log.e("前者"+String.valueOf(pk_user), "后者"+String.valueOf(fk_user));
								String avatar_path = user.getAvatar_path();
								String avaurl =beginStr+avatar_path;
								String nickname = user.getNickname();
								
								//更新UI
								ImageLoaderUtils.getInstance().LoadImageCricular(MyGalleryActivity.this, avaurl, userhead);
								Log.e("MyGalleryActivity", "url=="+avaurl);
								username.setText(nickname);
								creattime.setText(create_time);
							}
						}
						
					}
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int position) {
					// TODO Auto-generated method stub
//					Log.e("onPageSelected", "第一次进来是否调用~position");//第一次调用，并且预加载
					
				}
			});
			mViewPager.setCurrentItem(curposition);
	}
	
	private void initFirstdata() {
		log.e("", "第一次是否调用~");
		Photo photo = listphotos.get(curposition); 
		String create_time = photo.getCreate_time();
		Integer fk_user = photo.getFk_user();
		if(users.size()>0){
			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				Integer pk_user = user.getPk_user();
				if(String.valueOf(pk_user).equals(String.valueOf(fk_user))){
					String avatar_path = user.getAvatar_path();
					String avaurl =beginStr+avatar_path;
					String nickname = user.getNickname();
					
					//更新UI
					ImageLoaderUtils.getInstance().LoadImageCricular(MyGalleryActivity.this, avaurl, userhead);
					Log.e("MyGalleryActivity", "url=="+avaurl);
					username.setText(nickname);
					creattime.setText(create_time);
				}
			}
		}
		
	}

	private void initReadCurUser() {
		instance = Interface.getInstance();
		instance.setPostListener(new FindMultiUserListenner() {
			






			@Override
			public void success(String A) {
				Log.e("MyGalleryActivity", "查询多个用户返回" + A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					users = findfriends_statusmsg.getReturnData();
				}
				initFirstdata();
				samplePagerAdapter.notifyDataSetChanged();
			}
			
			
			
			@Override
			public void defail(Object B) {
				
			}
		});
		
		List<String> list=new ArrayList<String>();
		list.clear();
		for (int i = 0; i < listphotos.size(); i++) {
			Photo photo = listphotos.get(i);
			Integer fk_user = photo.getFk_user();
			list.add(String.valueOf(fk_user));
		}
		instance.findMultiUsers(this, list);
	}

	private void initUI() {
		review_listview = (ListView) findViewById(R.id.review_listview);
		MyAdapter myAdapter = new MyAdapter();
		review_listview.setAdapter(myAdapter);
		
		btn_send = (Button) findViewById(R.id.send);
		btn_send.setOnClickListener(this);
		et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
		et_sendmessage.setOnClickListener(this);
		userhead = (ImageView) findViewById(R.id.comment_head);
		username = (TextView) findViewById(R.id.comment_username);
		creattime = (TextView) findViewById(R.id.comment_creattime);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		iv_jianhao = (ImageView) findViewById(R.id.iv_jianhao);
		iv_jianhao.setOnClickListener(this);
		iv_jianhao_height = iv_jianhao.getHeight();
		rela_slideup = (RelativeLayout) findViewById(R.id.rela_slideup);
		findViewById(R.id.comment).setOnClickListener(this);//弹出评论
		rela_translucent = (RelativeLayout) findViewById(R.id.rela_translucent);//大布局
		rela_translucent.setOnClickListener(this);
		
		et_sendmessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if("".equals(s.toString())){
					btn_send.setVisibility(View.GONE);
				}else{
					btn_send.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			return null;
		}
		
	}

	 class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return listphotos.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			Photo photo = listphotos.get(position); 
//			String create_time = photo.getCreate_time();
			String pk_photo = photo.getPk_photo();
			String bigurl =beginStr+pk_photo;
//			Integer fk_user = photo.getFk_user();
//			// 此处user已经被加载好了，如果没加载好要做处理
//			if(UsersMap.size()>0){
//				User user = UsersMap.get(fk_user);//根据fk_user从容器中获取user对象
//				String avatar_path = user.getAvatar_path();
//				String avaurl =beginStr+avatar_path;
//				String nickname = user.getNickname();
//				
//				//更新UI
//				ImageLoaderUtils.getInstance().LoadImageCricular(MyGalleryActivity.this, avaurl, userhead);
//				Log.e("MyGalleryActivity", "url=="+avaurl);
//				username.setText(nickname);
//				creattime.setText(create_time);
//			}
			
//			String url = netFullpath.get(position);
			
//			Log.e("MyGalleryActivity", "netFullpath.get(curposition)=="+netFullpath.get(curposition));
//			if(position==curposition){//asdfadsfgsdgsdfjhkabhfigsdruirg
//				ImageLoaderUtils4Photos.getInstance().LoadImage2(netFullpath.get(0), photoView);
//			}else{
				ImageLoaderUtils4Photos.getInstance().LoadImage2(bigurl, photoView);
//			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewParent parent = container.getParent();
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_jianhao:
//			ALWAYShideSoftInputView();
			hideSoftInputView();
			hide();
			break;
		case R.id.comment:
			popupComment();
			break;
		case R.id.rela_translucent:
//			ALWAYShideSoftInputView();
			hideSoftInputView();
			hide();
			break;
		case R.id.et_sendmessage:
			
			break;
		case R.id.send:
			hideSoftInputView();
			sendReview();
			break;

		default:
			break;
		}
	}

	private void sendReview() {
		
	}

	private void popupComment() {
		rela_translucent.setVisibility(View.VISIBLE);
		Animation animation=new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		iv_jianhao.startAnimation(animation);
		
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		windowHeight = metric.heightPixels;
		
		//从下往上滑动
		Animation animation2=new TranslateAnimation(0, 0, windowHeight, 0);
		animation2.setDuration(500);
		rela_slideup.startAnimation(animation2); 
	}

	private void hide() {
		Animation animation=new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(500);
//		iv_jianhao.setAnimation(animation);
		iv_jianhao.startAnimation(animation);
		
		int dip2px = DensityUtil.dip2px(this, 35);
		int popdown=windowHeight-dip2px-iv_jianhao_height;
		//从上往下滑动
		Animation animation2=new TranslateAnimation(0, 0, 0, popdown);
		animation2.setDuration(500);
		rela_slideup.startAnimation(animation2); 
		
		rela_slideup.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				rela_translucent.setVisibility(View.GONE);	
			}
		}, 510);
		

	}

	protected void hideSoftInputView() {
		
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			View currentFocus = getCurrentFocus();
			if (currentFocus != null) {
				manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	protected void ALWAYShideSoftInputView() {
		
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			View currentFocus = getCurrentFocus();
				manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
