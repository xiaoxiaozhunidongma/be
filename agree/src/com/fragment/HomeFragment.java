package com.fragment;

import com.biju.R;
import com.biju.function.NewteamActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLayout==null)
		{
			mLayout = inflater.inflate(R.layout.fragment_home, container, false);
			initUI();
		}
		return mLayout;
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
