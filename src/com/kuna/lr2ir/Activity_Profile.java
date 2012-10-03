package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_Profile extends Activity {
	public static String user_id, user_name, user_uri;
	Proc_Profile pp = new Proc_Profile();
	ArrayList<HashMap<String, String>> al_often = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> al_recent = new ArrayList<HashMap<String,String>>();
	
	boolean isDetail = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.profile);
		
		load();
	}
	
	public void load()
	{		
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(user_name + "님의 정보");
		tv1 = (TextView) findViewById(R.id.textView2);

		new Thread(){
		     public void run(){
		    	 // over ICS Version
		    	 // http://stackoverflow.com/questions/10432344/httpurlconnection-fatal-error
		 		pp.parseURI(user_uri, al_often, al_recent);
		    	runOnUiThread(new Runnable() {
		    		public void run() {
		    			setTableLayout();
		    			setListView();
		    		}
		    	});
		     }
		}.start();

	}
	
	public void setTableLayout() {
		LinearLayout ll_profile = (LinearLayout)findViewById(R.id.ll_profile);
		
		TableLayout tl;
		TableRow tr;
		TextView tv;
		
		tl = new TableLayout(this);
		CustomStructure.addTR4TD("닉네임", pp.player_name, "LR2ID", pp.player_lr2id, tl, this);
		CustomStructure.addTR4TD("플레이곡수", pp.player_songcnt, "플레이횟수", pp.player_playcnt, tl, this);
		CustomStructure.addTR2TD("단위인정", pp.player_sp, tl, this);
		tl.setStretchAllColumns(true);
		tl.setPadding(0, 0, 0, 20);
		ll_profile.addView(tl);
		
		// self Introduce
		tv = new TextView(this);
		tv.setBackgroundColor(CustomStructure.clr_title);
		tv.setText(Html.fromHtml("<b>자기소개</b>"));
		tv.setPadding(10, 10, 10, 10);
		ll_profile.addView(tv);
		tv = new TextView(this);
		tv.setBackgroundColor(CustomStructure.clr_data);
		tv.setText( pp.player_self );
		tv.setPadding(10, 10, 10, 10);
		ll_profile.addView(tv);

		// homepage
		if (!pp.player_homepage.equals("")) {
			tv = new TextView(this);
			tv.setText( "홈페이지 : " + pp.player_homepage );
			tv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CustomStructure.gotoURI(v.getContext(), pp.player_homepage);
				}
			});
			tv.setPadding(10, 10, 10, 10);
			ll_profile.addView(tv);
		}
		
		// Rival
		tl = new TableLayout(this);
		tr = new TableRow(this);
		tv = new TextView(this);
		tv.setText(Html.fromHtml("<b>라이벌</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(CustomStructure.clr_title);
		tr.addView(tv);	// space
		tl.addView(tr);
		final Context _c = this;
		for (int i=0; i<pp.player_rival_cnt; i++) {	
			tr = new TableRow(this);
			tv = new TextView(this);
			tv.setText(pp.player_rival[i]);
			tv.setPadding(10, 5, 10, 5);
			tv.setBackgroundColor(CustomStructure.clr_data);
			final String s1 = pp.player_rival[i];
			final String s2 = pp.player_rival_uri[i];
			tv.setOnClickListener(new OnClickListener () {
				public void onClick(View v) {
					// intent 처리
					Activity_Profile.user_name = s1;
					Activity_Profile.user_uri = s2;
					startActivity( new Intent(_c, Activity_Profile.class) );
				}
			});
			tr.addView(tv);
			tl.addView(tr);
		}
		tl.setStretchAllColumns(true);
		tl.setPadding(0, 20, 0, 20);
		ll_profile.addView(tl);
		

		tl = new TableLayout(this);
		CustomStructure.addTR2TD("FULLCOMBO", pp.player_clear[0], tl, this);
		CustomStructure.addTR2TD("HARD", pp.player_clear[1], tl, this);
		CustomStructure.addTR2TD("NORMAL", pp.player_clear[2], tl, this);
		CustomStructure.addTR2TD("EASY", pp.player_clear[3], tl, this);
		CustomStructure.addTR2TD("FAILED", pp.player_clear[4], tl, this);
		tl.setStretchAllColumns(true);
		tl.setPadding(0, 0, 0, 20);
		ll_profile.addView(tl);
		
		
		Button b = new Button(this);
		b.setText("랭킹창 열기/닫기 (토글)");
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);
				LinearLayout ll1 = (LinearLayout) findViewById(R.id.linearLayout1);
				LinearLayout ll2 = (LinearLayout) findViewById(R.id.linearLayout2);
				if (!isDetail) {
					LayoutParams param = sv.getLayoutParams();
					param.height = 180;
					sv.setLayoutParams(param);
					ll1.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.VISIBLE);
				} else {
					LayoutParams param = sv.getLayoutParams();
					param.height = LayoutParams.MATCH_PARENT;
					sv.setLayoutParams(param);
					ll1.setVisibility(View.GONE);
					ll2.setVisibility(View.GONE);
				}
				isDetail = !isDetail;
			}
		});
		ll_profile.addView(b);
	}
	
	public void setListView() {
		ListView lv;
		SimpleAdapter sa;
    	final Context _c = this;	// temp
		
		lv = (ListView) findViewById(R.id.lvoften);
    	sa = new SimpleAdapter(this, al_often, R.layout.view_smalltext,
    			new String[] { "name" }, new int[] { R.id.text });
    	lv.setAdapter(sa);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity_SongInfo.songinfo_uri = pp.al_often_ps.get(arg2).pageuri;
				startActivity(new Intent(_c, Activity_SongInfo.class));
			}
		});

		lv = (ListView) findViewById(R.id.lvrecent);
    	sa = new SimpleAdapter(this, al_recent, R.layout.view_smalltext,
    			new String[] { "name" }, new int[] { R.id.text });
    	lv.setAdapter(sa);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Activity_SongInfo.songinfo_uri = pp.al_recent_ps.get(arg2).pageuri;
				startActivity(new Intent(_c, Activity_SongInfo.class));
			}
		});

    	//label event
    	TextView tv;
    	tv = (TextView) findViewById(R.id.textView2);
    	tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Activity_PlayList.user_id = user_id;
				Activity_PlayList.user_name = user_name;
				Activity_PlayList.uri_source = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mylist&sort=recent&playerid=" + user_id;
				startActivity(new Intent(_c, Activity_PlayList.class));
			}
    	});

    	tv = (TextView) findViewById(R.id.textView3);
    	tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Activity_PlayList.user_id = user_id;
				Activity_PlayList.user_name = user_name;
				Activity_PlayList.uri_source = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mylist&sort=playcount&playerid=" + user_id;
				startActivity(new Intent(_c, Activity_PlayList.class));
			}
    	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu_profile, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item0_1:
			Lr2irActivity.addUser(Activity_Profile.user_id, "", Activity_Profile.user_name);
			Toast.makeText(this, "성공적으로 등록되었습니다", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}

/// http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?login=1&lr2id=31478&passmd5=e7eacd1c6aed0a9bdbf16950d001ee6f
