package com.fragment;

import com.biju.R;
import com.biju.function.NewPartyActivity;

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
public class PartyFragment extends Fragment implements OnClickListener{

	private View mLayout;

	public PartyFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_party, container, false);
		initUI();
		return mLayout;
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_party_new_layout).setOnClickListener(this);//ÐÂ½¨
		mLayout.findViewById(R.id.tab_party_new).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_party_new_layout:
		case R.id.tab_party_new:
			tab_party_new();
			break;

		default:
			break;
		}
	}

	private void tab_party_new() {
		Intent intent=new Intent(getActivity(), NewPartyActivity.class);
		startActivity(intent);
	}

}
