package com.fragment;

import java.util.ArrayList;

import com.biju.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class BaseHomeFragment extends Fragment {

	private View mLayout;
	private TabPagerFragment fragment;
	private ArrayList<Fragment> fragments;

	public BaseHomeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_base_home, container,
				false);
		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.framelayout, new HomeFragment());
		ft.commit();
		
		return mLayout;
	}

	
}
