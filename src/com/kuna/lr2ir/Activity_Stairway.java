package com.kuna.lr2ir;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Stairway extends Activity {
	public static String user_id, user_name, user_uri;
	Proc_Stairway ps = new Proc_Stairway();
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String,String>>();
	
	boolean isDetail = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.stairway);
		
		load();
	}
	
	public void load()
	{		
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(user_name + "님의 Stairway");
		tv1 = (TextView) findViewById(R.id.textView1);
/*
		new Thread(){
		     public void run(){
		    	 // over ICS Version
		    	 // http://stackoverflow.com/questions/10432344/httpurlconnection-fatal-error
		 		ps.parseURI(user_uri, mylist);
		    	runOnUiThread(new Runnable() {
		    		public void run() {
		    			Init();
		    		}
		    	});
		     }
		}.start();
*/

		showUrlBox(user_uri);
	}

	public void Init() {
		ListView lv;
		Adapter_PlayList ap;
    	final Context _c = this;
		
		lv = (ListView) findViewById(R.id.listView1);
		ap = new Adapter_PlayList(this, mylist);
		
    	lv.setAdapter(ap);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity_SongInfo.songinfo_uri = mylist.get(arg2).get("url");
				startActivity(new Intent(_c, Activity_SongInfo.class));
			}
		});
	}
	
	public void showUrlBox(final String url) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Error");
		alert.setMessage("라이브러리의 한계상(Overflow) 파싱을 계속할 수 없습니다. 웹 페이지에서 별도로 보시겠습니까?");
		alert.setPositiveButton("예",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  Uri uri = Uri.parse(url);
			  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			  startActivity(intent);
		  }
		 });
		alert.setNegativeButton("아니오",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  finish();
		  }
		 });
		alert.show();
	}
}