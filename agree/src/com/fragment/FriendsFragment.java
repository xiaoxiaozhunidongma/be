package com.fragment;

import com.biju.R;
import com.biju.function.AddFriendsActivity;

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
public class FriendsFragment extends Fragment implements OnClickListener{

	private View mLayout;

	public FriendsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_friends, container, false);
		initUI();
		return mLayout;
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_friends_addbuddy_layout).setOnClickListener(this);
		mLayout.findViewById(R.id.tab_friends_addbuddy).setOnClickListener(this);//ÃÌº”∫√”—
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_friends_addbuddy_layout:
		case R.id.tab_friends_addbuddy:
			tab_friends_addbuddy();
			break;

		default:
			break;
		}
	}

	private void tab_friends_addbuddy() {
		Intent intent=new Intent(getActivity(), AddFriendsActivity.class);
		startActivity(intent);
	}

}
