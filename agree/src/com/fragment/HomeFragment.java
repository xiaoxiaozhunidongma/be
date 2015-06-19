package com.fragment;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.NewteamActivity;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements OnClickListener{

	private View mLayout;
	private String  beginStr="http://201139.image.myqcloud.com/201139/0/";
	private String endStr="/original";
	//完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL="";
	
	

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLayout==null)
		{
			mLayout = inflater.inflate(R.layout.fragment_home, container, false);
			initUI();
			initNewTeam();
		}
		return mLayout;
	}

	private void initNewTeam() {
		SharedPreferences sp = getActivity().getSharedPreferences("Registered", 0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		Log.e("HomeFragment", "isRegistered_one==="+isRegistered_one);
		if(isRegistered_one)
		{
			
		}else
		{
			int pk_user=LoginActivity.pk_user;
			ReadTeam(pk_user);
			Log.e("HomeFragment", "pk_user1===="+pk_user);
		}
	}


	private void ReadTeam(int pk_user) {
		Interface homeInterface=new Interface();
		User homeuser=new User();
		homeuser.setPk_user(pk_user);
		Log.e("HomeFragment", "pk_user2===="+pk_user);
		homeInterface.readUserGroupMsg(getActivity(), homeuser);
		homeInterface.setPostListener(new UserInterface() {
			
			@Override
			public void success(String A) {
				Loginback homeback=GsonUtils.parseJson(A, Loginback.class);
				int homeStatusMsg=homeback.getStatusMsg();
				if(homeStatusMsg==1)
				{
					Log.e("HomeFragment", "读取用户小组信息2==="+A);
				}
			}
			
			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_home_new_layout).setOnClickListener(this);//新建小组
		mLayout.findViewById(R.id.tab_home_new).setOnClickListener(this);//新建小组
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_home_new_layout:
		case R.id.tab_home_new:
			tab_home_new_layout();
			break;

		default:
			break;
		}
	}

	private void tab_home_new_layout() {
		Intent intent=new Intent(getActivity(), NewteamActivity.class);
		startActivity(intent);
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent=(ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

}
