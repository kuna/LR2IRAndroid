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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Activity_SearchSong extends Activity {
	Proc_SearchSong ps = new Proc_SearchSong();
	public static String searchsong_uri;
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
		 		ps.parseURI(searchsong_uri, mylist);
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
		final ListView lv = (ListView) findViewById(R.id.listView1);
    	final Context _c = this;
    	lv.setOnItemClickListener(new OnItemClickListener () {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// no Footer View!
				if (arg1.equals(mFooterView)) return;
				
				Activity_SongInfo.songinfo_uri = ps.al_ps.get(arg2).uri;
				startActivity( new Intent(_c, Activity_SongInfo.class) );
			}
    	});
    	lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			    // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
			    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
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
    	
    	// add footer (auto loading)
    	LayoutInflater mInflat = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mFooterView = mInflat.inflate(R.layout.view_loading, null);
    	lv.addFooterView(mFooterView);

    	sa = new Adapter_Search(this, mylist);
    	//sa = new SimpleAdapter(this, mylist, R.layout.view_titledesc,
    	//		new String[] { "title", "info" }, new int[] { R.id.tvtitle, R.id.tvdesc });
    	lv.setAdapter(sa);
	}
}
