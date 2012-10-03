package com.kuna.lr2ir;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

public class Activity_SongInfo extends Activity {
	ArrayList<HashMap<String,String>> mylist = new ArrayList<HashMap<String,String>>();
	Proc_SongInfo ps = new Proc_SongInfo();
	public static String songinfo_uri;
	
	boolean mLockListView;
	View mFooterView;
	Adapter_Ranking sa;
	
	boolean isRanking = false;
	
	public Activity_SongInfo() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.songinfo);

		new Thread(){
		     public void run(){
		 		ps.parseURI(songinfo_uri, mylist);
		    	 runOnUiThread(new Runnable() {
		    		 public void run() {
	    				// lets add table few for sample!
	    				addInfoAtTable();
	    				
	    				// listview
	    				addListViewItem();
		    		 }
		    	 });
		     }
		}.start();		
		
		super.onCreate(savedInstanceState);
	}
	
	public void addInfoAtTable() {
		TextView tv;
		tv = (TextView) findViewById(R.id.tv_genre);
		tv.setText(ps.song_Genre);
		tv = (TextView) findViewById(R.id.tv_title);
		tv.setText(ps.song_Title);
		tv = (TextView) findViewById(R.id.tv_artist);
		tv.setText(ps.song_Artist);


		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout1);
		
		TableLayout tl;

		tv = new TextView(this);
		tv.setText("★ 연계된 유튜브 동영상 검색하기");
		tv.setOnClickListener(new OnClickListener () {
			public void onClick(View v) {
				CustomStructure.gotoURI(v.getContext(), "http://www.youtube.com/results?search_query=" + ps.song_Title + " bms");
			}
		});
		tv.setPadding(10, 10, 10, 10);
		ll.addView(tv);

		if (!ps.song_url1.equals("")) {
			tv = new TextView(this);
			tv.setText("★ 원본 URL");
			tv.setOnClickListener(new OnClickListener () {
				public void onClick(View v) {
					CustomStructure.gotoURI(v.getContext(), ps.song_url1);
				}
			});
			tv.setPadding(10, 10, 10, 10);
			ll.addView(tv);
		}
		if (!ps.song_url2.equals("")) {
			tv = new TextView(this);
			tv.setText("★ 차분 URL");
			tv.setOnClickListener(new OnClickListener () {
				public void onClick(View v) {
					CustomStructure.gotoURI(v.getContext(), ps.song_url2);
				}
			});
			tv.setPadding(10, 10, 10, 10);
			ll.addView(tv);
		}
		
		tl = new TableLayout(this);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		CustomStructure.addTR4TD("BPM", ps.song_BPM, "Level", ps.song_level, tl, this);
		CustomStructure.addTR4TD("Keys", ps.song_keys, "Judgerank", ps.song_judgerank, tl, this);
		tl.setPadding(0, 0, 0, 20);
		tl.setStretchAllColumns(true);
		ll.addView(tl);
		
		// tags
		TableRow tr = new TableRow(this);
		tl = new TableLayout(this);
		tv = new TextView(this);
		tv.setText(Html.fromHtml("<b>Tags</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(CustomStructure.clr_title);
		tr.addView(tv);	// space
		tl.addView(tr);
		final Context _c = this;
		for (int i=0; i<ps.song_Tags_cnt; i++) {	
			tr = new TableRow(this);
			tv = new TextView(this);
			tv.setText(ps.song_Tags[i]);
			tv.setPadding(10, 5, 10, 5);
			tv.setBackgroundColor(CustomStructure.clr_data);
			final String s2 = ps.song_Tags_Url[i];
			tv.setOnClickListener(new OnClickListener () {
				public void onClick(View v) {
					// intent 처리
					Activity_SearchSong.searchsong_uri = s2;
					Log.v("test", Activity_SearchSong.searchsong_uri);
					startActivity( new Intent(_c, Activity_SearchSong.class) );
				}
			});
			tr.addView(tv);
			tl.addView(tr);
		}
		
		tl.setPadding(0, 0, 0, 20);
		tl.setStretchAllColumns(true);
		ll.addView(tl);

		tl = new TableLayout(this);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		CustomStructure.addTRTDTitleArr(new String[] {"플레이", "클리어", "클리어비율"}, tl, this);
		CustomStructure.addTRTDArr("회수", new String[] {ps.song_PlayCount, ps.song_PlayClearCount, ps.song_PlayClearRate}, tl, this);
		CustomStructure.addTRTDArr("사함 수", new String[] {ps.song_PeopleCount, ps.song_PeopleClearCount, ps.song_PeopleClearRate}, tl, this);
		tl.setPadding(0, 0, 0, 20);
		tl.setStretchAllColumns(true);
		ll.addView(tl);

		tl = new TableLayout(this);
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		CustomStructure.addTRTDTitleArr(new String[] {"클리어 횟수", "비율"}, tl, this);
		String _s[] = new String[] {"FULLCOMBO", "HARD", "NORMAL", "EASY", "FAIL"};
		for (int i=0; i<5; i++)
			CustomStructure.addTRTDArr(_s[i], new String[] { ps.song_Rate[i], ps.song_RatePerc[i] } , tl, this);
		tl.setPadding(0, 0, 0, 20);
		tl.setStretchAllColumns(true);
		ll.addView(tl);
		
		if (ps.login) {
			tl = new TableLayout(this);
			tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			CustomStructure.addTRTDTitleArr(new String[] {"순위", "CLEAR", "RANK", "SCORE", "COMBO"}, tl, this);
			CustomStructure.addTRTDArr_NoTitle(new String[] { ps.login_info[0], ps.login_info[1], ps.login_info[2], ps.login_info[3], ps.login_info[4] }, tl, this);
			tl.setPadding(0, 0, 0, 20);
			ll.addView(tl);
		}
		
		
		Button b = new Button(this);
		b.setText("랭킹창 열기/닫기 (토글)");
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);
				ListView lv = (ListView) findViewById(R.id.listView1);
				if (!isRanking) {
					LayoutParams param = sv.getLayoutParams();
					param.height = 180;
					sv.setLayoutParams(param);
					lv.setVisibility(View.VISIBLE);
				} else {
					LayoutParams param = sv.getLayoutParams();
					param.height = LayoutParams.MATCH_PARENT;
					sv.setLayoutParams(param);
					lv.setVisibility(View.GONE);
				}
				isRanking = !isRanking;
			}
		});
		ll.addView(b);
		
		//addTR2TD("Tags", ps.song_Tags[0], tl);
	}
	
	int now_index;
	public void addListViewItem() {
		final ListView lv = (ListView) findViewById(R.id.listView1);
    	
    	final Context _c = this;	// temp
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				now_index = arg2;
				Activity_Profile.user_name = ps.al_ps.get(arg2).name;
				Activity_Profile.user_uri = ps.al_ps.get(arg2).nameuri;
				AlertDialog ad = new AlertDialog.Builder(_c)
					.setView(createADView())
					.show();
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

    	sa = new Adapter_Ranking(this, mylist);
    	//sa = new SimpleAdapter(this, mylist, R.layout.view_userlist_item,
    	//		new String[] { "name", "clear", "info", "rank", "score" }, new int[] { R.id.tv_name, R.id.tv_clear, R.id.tv_info, R.id.tv_rank, R.id.tv_score });
    	lv.setAdapter(sa);
	}

    public View createADView() {
    	// temp
    	final Context _c = this;
    	
    	LinearLayout v = new LinearLayout(this);
    	v.setOrientation(LinearLayout.VERTICAL);
    	
    	TextView tv;
    	tv = new TextView(this);
    	tv.setText("Num : " + ps.al_ps.get(now_index).num);
    	v.addView(tv);

    	tv = new TextView(this);
    	tv.setText("Name : " + ps.al_ps.get(now_index).name);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("단위 : " + ps.al_ps.get(now_index).lvl);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Score  : " + ps.al_ps.get(now_index).score);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Rank : " + ps.al_ps.get(now_index).ranking);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Combo : " + ps.al_ps.get(now_index).combo);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("B+P : " + ps.al_ps.get(now_index).bp);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("PGREAT : " + ps.al_ps.get(now_index).pg);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("GREAT : " + ps.al_ps.get(now_index).gr);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("GOOD : " + ps.al_ps.get(now_index).gd);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("BAD : " + ps.al_ps.get(now_index).bd);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("POOR : " + ps.al_ps.get(now_index).pr);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("OP(게이지) : " + ps.al_ps.get(now_index).op1);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("OP(노트) : " + ps.al_ps.get(now_index).op2);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Input : " + ps.al_ps.get(now_index).input);
    	v.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("구동기 : " + ps.al_ps.get(now_index).program);
    	v.addView(tv);
    	
    	Button b = new Button(this);
    	b.setText("유저 프로필 보기");
    	b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity( new Intent(_c, Activity_Profile.class) );
			}
    	});
    	v.addView(b);
    	return v;
    }

}
