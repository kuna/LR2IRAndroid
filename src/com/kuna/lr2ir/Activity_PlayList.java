package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_PlayList extends Activity {

	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	public static String user_id, user_name;
	public static String uri_source;

	boolean mLockListView;
	View mFooterView;
	Adapter_PlayList sa;
	
	Proc_PlayList ppl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.playlist);

		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(user_name + "���� �ֱ� �÷��� ���");
		tv1 = (TextView) findViewById(R.id.textView2);

		new Thread(){
		     public void run(){
		    	 // over ICS Version
		    	 // http://stackoverflow.com/questions/10432344/httpurlconnection-fatal-error
		 		// add info
		 		ppl = new Proc_PlayList();
		 		ppl.parseURI(uri_source, mylist);
		    	runOnUiThread(new Runnable() {
		    		public void run() {
		    			load();
		    		}
		    	});
		     }
		}.start();
	}
	
	public void load()
	{
		final ListView lv = (ListView) findViewById(R.id.listView1);
    	
    	final Context _c = this;
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity_SongInfo.songinfo_uri = ppl.al_ps.get(arg2).pageuri;
				startActivity( new Intent(_c, Activity_SongInfo.class) );
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
			    	if (!ppl.isNextPage) {
	    				lv.removeFooterView(mFooterView);
	    				mLockListView = true;
			    		return;
			    	}
			    	
			    	mLockListView = true;
			    	Runnable run = new Runnable() {
			    		public void run() {
			    			Log.v("parse", ppl.strNextPageuri);
			    			ppl.parseURI(ppl.strNextPageuri, mylist);
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

    	
    	sa = new Adapter_PlayList(this, mylist);
    	//sa = new SimpleAdapter(this, mylist, R.layout.view_playlist_item,
    	//		new String[] { "name", "info", "num" }, new int[] { R.id.textView1, R.id.textView2, R.id.textView3 });
    	lv.setAdapter(sa);
	}
	
	
}
