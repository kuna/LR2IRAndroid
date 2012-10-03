package com.kuna.lr2ir;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_SearchUser extends Activity {
	Proc_SearchUser ps = new Proc_SearchUser();
	public static String searchuser_uri;
	ArrayList<HashMap<String,String>> mylist = new ArrayList<HashMap<String,String>>();

	boolean mLockListView;
	View mFooterView;
	Adapter_Search sa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.search);
		
		new Thread(){
		     public void run(){
		    	 // over ICS Version
		    	 // http://stackoverflow.com/questions/10432344/httpurlconnection-fatal-error
		    	 ps.parseURI(searchuser_uri, mylist);
		    	 runOnUiThread(new Runnable() {
		    		 public void run() {
				    	 Init();
		    		 }
		    	 });
		     }
		}.start();
		
    	
		super.onCreate(savedInstanceState);
	}
	
	public void Init() {
		Log.i("LR2IR", "Start Dlg Init");
		final ListView lv = (ListView) findViewById(R.id.listView1);
    	final Context _c = this;
    	lv.setOnItemClickListener(new OnItemClickListener () {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity_Profile.user_id = ps.al_ps.get(arg2).lr2id;
				Activity_Profile.user_name = ps.al_ps.get(arg2).username;
				Activity_Profile.user_uri = ps.al_ps.get(arg2).useruri;
				startActivity( new Intent(_c, Activity_Profile.class) );
			}
    	});
    	lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			    // ���� ���� ó���� ���̴� ����ȣ�� �������� ����ȣ�� ���Ѱ���
			    // ��ü�� ���ڿ� ���������� ���� �Ʒ��� ��ũ�� �Ǿ��ٰ� �����մϴ�.
			    int count = totalItemCount - visibleItemCount;
			    if(firstVisibleItem >= count && totalItemCount != 0
			    		&& mLockListView == false)
			    {
			    	if (!ps.isNextPage) {
	    				lv.removeFooterView(mFooterView);
	    				mLockListView = true;
			    		return;
			    	}
			    	
			    	mLockListView = true;
			    	Runnable run = new Runnable() {
			    		public void run() {
			    			Log.v("parse", ps.strNextPageuri);
			    			ps.parseURI(ps.strNextPageuri, mylist);
			    			sa.notifyDataSetChanged();
			    			mLockListView = false;
			    		}
			    	};
			    	Handler h = new Handler();
			    	h.post(run);
			    }
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
    	});
		Log.i("LR2IR", "Event Attach fin");
    	
    	// add footer (auto loading)
    	LayoutInflater mInflat = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mFooterView = mInflat.inflate(R.layout.view_loading, null);
    	lv.addFooterView(mFooterView);
		Log.i("LR2IR", "footer fin");

    	sa = new Adapter_Search(this, mylist);
    	//sa = new SimpleAdapter(this, mylist, R.layout.view_titledesc,
    	//		new String[] { "title", "info" }, new int[] { R.id.tvtitle, R.id.tvdesc });
    	lv.setAdapter(sa);
		Log.i("LR2IR", "adapter fin");
	}
}
