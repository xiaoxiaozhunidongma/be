package com.biju.function;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.biju.Interface;
import com.biju.Interface.ReadGroupPartyAlllistenner;
import com.biju.R;

public class HistoryPartyActivity extends Activity implements OnClickListener{

	private Interface mHistoryPartyInterface;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_party);
		initUI();
		initInterface();
		initReadOldParty();
	}

	private void initReadOldParty() {
		Integer pk_group=GroupActivity.getPk_group();
		Group group=new Group();
		group.setPk_group(pk_group);
		mHistoryPartyInterface.readUserGroupPartyAll(HistoryPartyActivity.this, group);
	}

	private void initInterface() {
		mHistoryPartyInterface = Interface.getInstance();
		mHistoryPartyInterface.setPostListener(new ReadGroupPartyAlllistenner() {
			
			@Override
			public void success(String A) {
				Log.e("HistoryPartyActivity", "获取的聚会结果==="+A);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initUI() {
		findViewById(R.id.HistoryPartyBack).setOnClickListener(this);
	}

	class ViewHolder{
		TextView mHistoryPartyName;
		TextView mHistoryPartyTime;
		TextView mHistoryPartyLine1;
		TextView mHistoryPartyLine2;
	}
	
	class MyHistoryParty extends BaseAdapter{

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
			View inflater = null;
			ViewHolder holder = null;
			if(convertView==null){
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater=layoutInflater.inflate(R.layout.historyparty_item, null);
				holder.mHistoryPartyName = (TextView) inflater.findViewById(R.id.HistoryPartyName);//历史机会名称
				holder.mHistoryPartyTime = (TextView) inflater.findViewById(R.id.HistoryPartyTime);//历史聚会时间
				holder.mHistoryPartyLine1=(TextView) inflater.findViewById(R.id.HistoryPartyLine1);
				holder.mHistoryPartyLine2=(TextView) inflater.findViewById(R.id.HistoryPartyLine2);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			
			return inflater;
		}
		
	}
	
	@Override
	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.HistoryPartyBack:
				HistoryPartyBack();
				break;

			default:
				break;
			}
	}

	private void HistoryPartyBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

}
