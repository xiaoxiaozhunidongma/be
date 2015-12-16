package com.biju.withdrawal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Historylist;
import com.BJ.javabean.User;
import com.BJ.javabean.HistoryListBack;
import com.BJ.utils.InitPkUser;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.HistoryListListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class HistoryListActivity extends Activity implements OnClickListener{

	private ListView mHistoryListView;
	private RelativeLayout mHistoryListNoShow;
	private Interface mHistoryListInterface;
	private List<Historylist> HistoryList=new ArrayList<Historylist>();
	private MyHistoryListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_list);
		initUI();
		initInterface();
		initHistoryList();
	}

	private void initInterface() {
		mHistoryListInterface = Interface.getInstance();
		mHistoryListInterface.setPostListener(new HistoryListListenner() {
			
			@Override
			public void success(String A) {
				Log.e("HistoryListActivity", "获取的提现记录==="+A);
				HistoryListBack historyListBack=GsonUtils.parseJson(A, HistoryListBack.class);
				Integer status=historyListBack.getStatusMsg();
				if(1==status){
					mHistoryListView.setVisibility(View.VISIBLE);
					mHistoryListNoShow.setVisibility(View.GONE);
					List<Historylist> historylists=historyListBack.getReturnData();
					if(historylists.size()>0){
						for (int i = 0; i < historylists.size(); i++) {
							Historylist history=historylists.get(i);
							HistoryList.add(history);
						}
					}
					adapter.notifyDataSetChanged();
				}else {
					mHistoryListNoShow.setVisibility(View.VISIBLE);
					mHistoryListView.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void defail(Object B) {
			}
		});
	}


	private void initHistoryList() {
		Integer pk_user=InitPkUser.InitPkUser();
		User user=new User();
		user.setPk_user(pk_user);
		mHistoryListInterface.HistoryList(HistoryListActivity.this, user);
	}


	private void initUI() {
		mHistoryListNoShow = (RelativeLayout) findViewById(R.id.HistoryListNoShow);
		findViewById(R.id.HistoryListBack).setOnClickListener(this);
		mHistoryListView = (ListView) findViewById(R.id.HistoryListView);
		mHistoryListView.setDividerHeight(0);
		adapter = new MyHistoryListAdapter();
		mHistoryListView.setAdapter(adapter);
	}

	class ViewHolder{
		TextView HostoryListMoney;
		TextView HostoryListTime;
		TextView HostoryListLine1;
		TextView HostoryListLine2;
	}
	
	class MyHistoryListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return HistoryList.size();
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
			View inflater=null;
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater=layoutInflater.inflate(R.layout.historylist_item, null);
				holder.HostoryListMoney=(TextView) inflater.findViewById(R.id.HostoryListMoney);
				holder.HostoryListTime=(TextView) inflater.findViewById(R.id.HostoryListTime);
				holder.HostoryListLine1=(TextView) inflater.findViewById(R.id.HostoryListLine1);
				holder.HostoryListLine2=(TextView) inflater.findViewById(R.id.HostoryListLine2);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			Historylist historylist=HistoryList.get(position);
			holder.HostoryListMoney.setText("提现金额为: "+historylist.getAmount());
			holder.HostoryListTime.setText("预计到账时间: "+historylist.getArrival_time());
			
			if(position==HistoryList.size()-1){
				holder.HostoryListLine1.setVisibility(View.GONE);
				holder.HostoryListLine2.setVisibility(View.VISIBLE);
			}else {
				holder.HostoryListLine1.setVisibility(View.VISIBLE);
				holder.HostoryListLine2.setVisibility(View.GONE);
			}
			return inflater;
		}
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.HistoryListBack:
			HistoryListBack();
			break;

		default:
			break;
		}
	}


	private void HistoryListBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			HistoryListBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
