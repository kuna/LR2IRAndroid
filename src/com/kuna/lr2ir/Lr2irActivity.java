package com.kuna.lr2ir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Lr2irActivity extends Activity {
	/* static value */
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        load();
        setEvent();
    }
    
    static ArrayList<HashMap<String, String>> favList = new ArrayList<HashMap<String, String>>();
    static ArrayList<String> favMD5List = new ArrayList<String>();
    
    public void load()
    {
    	loadInfo();
    	
    	ListView lv = (ListView) findViewById(R.id.listView1);
    	SimpleAdapter sa = new SimpleAdapter(this, favList, R.layout.view_main_profile,
    			new String[] { "info", "id" }, new int[] { R.id.textView1, R.id.textView2 });
    	lv.setAdapter(sa);
    }
    
    int sel_index;
    static AlertDialog ad_custom;
    public void setEvent()
    {
    	// temp;
    	final Context _c = this;
    	
    	ListView lv = (ListView) findViewById(R.id.listView1);
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sel_index = arg2;
				
				// create Dialog
				ad_custom = new AlertDialog.Builder(_c)
					.setView(createCustomView())
					.create();
				ad_custom.show();
			}
    	});
    	
    	Button btnSearchSong = (Button) findViewById(R.id.button2);
    	btnSearchSong.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText et = (EditText)findViewById(R.id.editText1);
				sortview_query = et.getText().toString();
				
				AlertDialog ad = new AlertDialog.Builder(_c)
				.setView(createSortView())
				.show();
			}
    	});
    	
    	Button btnSearchUser = (Button) findViewById(R.id.button1);
    	btnSearchUser.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText et = (EditText)findViewById(R.id.editText1);
				String q = et.getText().toString();

				Activity_SearchUser.searchuser_uri = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=search&sort=bmsid_desc&keyword=" + q + "&exec=%8C%9F%8D%F5&type=player";
				startActivity( new Intent(_c, Activity_SearchUser.class) );
			}
    	});
    	
    	// for editview
    	final EditText et = (EditText)findViewById(R.id.editText1);
    	et.setInputType(0);
    	et.setOnClickListener(new OnClickListener () {
			public void onClick(View v) {
				et.setInputType(1);
			} 
    	});
    }

    public View createCustomView() {
    	// temp
    	final Context _c = this;
    	
    	LinearLayout v = new LinearLayout(this);
    	
    	ListView lv = new ListView(this);
    	ArrayAdapter<String> aa = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,
    			new String[] { "User Info", "Often Played", "Lately Played", "Stairway(Insane)", "Stairway(Overjoy)", "Stairway(LN)", "Stairway(LN Overjoy)", "Stairway(Normal)", "Stairway(No2)", "Delete User" });
    	lv.setAdapter(aa);
    	
    	/*Event Proc*/
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:	// User Info
					Activity_Profile.user_name = favList.get(sel_index).get("info");
					Activity_Profile.user_id = favList.get(sel_index).get("id");
					Activity_Profile.user_uri = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mypage&playerid=" + Activity_Profile.user_id;
					startActivity( new Intent(_c, Activity_Profile.class) );
					break;
				case 1:	// Often Played
					Activity_PlayList.user_name = favList.get(sel_index).get("info");
					Activity_PlayList.user_id = favList.get(sel_index).get("id");
					Activity_PlayList.uri_source = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mylist&sort=playcount&playerid=" + Activity_PlayList.user_id;
					startActivity( new Intent(_c, Activity_PlayList.class) );
					break;
				case 2:	// Lately Played
					if (arg2 >= favList.size()) break;	//?
					Activity_PlayList.user_name = favList.get(sel_index).get("info");
					Activity_PlayList.user_id = favList.get(sel_index).get("id");
					Activity_PlayList.uri_source = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mylist&sort=recent&playerid=" + Activity_PlayList.user_id;
					startActivity( new Intent(_c, Activity_PlayList.class) );
					break;
				case 3: // stairway - insane
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/LunaticRave2/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 4: // stairway - overjoy
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/lroverjoy/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 5: // stairway - ln
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/LR2LN/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 6: // stairway - ln overjoy
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/LR2LNOJ/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 7: // stairway - normal
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/lr2normal/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 8: // stairway - no2
					Activity_Stairway.user_name = favList.get(sel_index).get("info");
					Activity_Stairway.user_id = Integer.toString(Integer.parseInt(favList.get(sel_index).get("id")));
					Activity_Stairway.user_uri = "http://stairway.sakura.ne.jp/bms/lr2no2/?contents=player&page=" + Activity_Stairway.user_id;
					startActivity( new Intent(_c, Activity_Stairway.class) );
					break;
				case 9:	// del
					delUser(sel_index);
					Toast.makeText(_c, "삭제했습니다.", Toast.LENGTH_SHORT).show();

					/* refresh */
					ListView lv = (ListView) findViewById(R.id.listView1);
			    	SimpleAdapter sa = new SimpleAdapter(_c, favList, R.layout.view_main_profile,
			    			new String[] { "info", "id" }, new int[] { R.id.textView1, R.id.textView2 });
			    	lv.setAdapter(sa);
					
					break;
				}
			}
		});
    	/*Event Proc End*/
    	
    	v.setOrientation(LinearLayout.VERTICAL);
    	v.addView(lv);
    	return v;
    }
    public View createAddPlayerView() {
    	// temp
    	final Context _c = this;
    	
    	LinearLayout v = new LinearLayout(this);
    	v.setOrientation(LinearLayout.VERTICAL);
    	
    	TextView tv;
    	final EditText et;
		final EditText et_id;
    	Button b;

    	tv = new TextView(this);
    	tv.setText("LR2ID를 입력하세요");
    	et_id = new EditText(this);
    	et_id.setPadding(0, 0, 0, 10);
    	v.addView(tv);
    	v.addView(et_id);

    	tv = new TextView(this);
    	tv.setText("비밀번호를 입력하세요. MD5해시로 저장되며 다른 곳에 전송되지 않습니다. 입력하지 않아도 정보조회는 가능합니다.");
    	et = new EditText(this);
    	et.setPadding(0, 0, 0, 10);
    	v.addView(tv);
    	v.addView(et);
    	
    	b = new Button(this);
    	b.setText("등록");
    	v.addView(b);
    	
    	/*Event Proc*/
    	b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String uid, uname;
				uid = et_id.getText().toString();
				Proc_Profile pp = new Proc_Profile();

				ArrayList<HashMap<String,String>> ol = new ArrayList<HashMap<String,String>>();	// garbage
				ArrayList<HashMap<String,String>> rl = new ArrayList<HashMap<String,String>>();	// garbage
				pp.parseURI("http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=mypage&playerid=" + uid, ol, rl);
				
				uname = pp.player_name;
				if (uname.equals("")) {
					Toast.makeText(_c, "존재하지 않는 유저입니다.", Toast.LENGTH_SHORT).show();
				} else {
					addUser(uid, et.getText().toString(), uname);
					Toast.makeText(_c, "등록되었습니다.", Toast.LENGTH_SHORT).show();

					/* refresh */
					ListView lv = (ListView) findViewById(R.id.listView1);
			    	SimpleAdapter sa = new SimpleAdapter(_c, favList, R.layout.view_main_profile,
			    			new String[] { "info", "id" }, new int[] { R.id.textView1, R.id.textView2 });
			    	lv.setAdapter(sa);
				}
			}
		});
    	/*Event Proc End*/
    	
    	return v;
    }
    String sortview_query;
    public View createSortView() {
    	// temp
    	final Context _c = this;
    	
    	LinearLayout v = new LinearLayout(this);
    	v.setOrientation(LinearLayout.VERTICAL);

    	ListView lv = new ListView(this);
    	ArrayAdapter<String> aa = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,
    			new String[] { 
    			"플레이한 사람이 많은 순서대로",
    			"플레이한 사람이 적은 순서대로",
    			"플레이 횟수가 많은 순서대로",
    			"플레이 횟수가 적은 순서대로",
    			"스코어갱신이 새로운 순서대로",
    			"스코어갱신이 오래된 순서대로",
    			"평균 플레이 횟수가 많은 순서대로",
    			"평균 플레이 횟수가 적은 순서대로",
    			"곡 수록이 새로운 순서대로",
    			"곡 수록이 오래된 순서대로" });
    	lv.setAdapter(aa);
    	
    	/*Event Proc*/
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			    String uri_mode[] = new String[] {
						"player_desc",
						"player_asc",
						"playcount_desc",
						"playcount_asc",
						"date_desc",
						"date_asc",
						"repeat_desc",
						"repeat_asc",
						"bmsid_desc",
						"bmsid_asc"
				};
				String uri = "http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?mode=search&sort=" + uri_mode[arg2] + "&keyword=" + sortview_query + "&exec=%8C%9F%8D%F5&type=keyword";
				Activity_SearchSong.searchsong_uri = uri;
				startActivity( new Intent(_c, Activity_SearchSong.class) );
			}
		});
    	/*Event Proc End*/
    	
    	v.addView(lv);
    	return v;
    }
    
	@Override
	protected void onStop() {
		saveInfo();
		
		super.onDestroy();
	}
	
	// mini menu - add to list?
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		new MenuInflater(this).inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	static AlertDialog ad_addview;
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case R.id.item1 :	// add form
			ad_addview = new AlertDialog.Builder(this)
			.setView(createAddPlayerView())
			.show();
			break;
		case R.id.item2 :	// about
			Toast.makeText(this, "By kuna - http://kuna.wo.tc\nBuild 2012-02-19 2AM, Alpha ver.\n이 프로그램은 Lunatic Rave 2 제작사 혹은 InternetRanking과 직접적인 관련이 없습니다.", Toast.LENGTH_LONG)
			.show();
			break;
		case R.id.item3 :	// exit
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	
	/** program default */
	public static void loadInfo() {
		try {
			FileInputStream input = new FileInputStream("/sdcard/.favlist");
			BufferedReader in = new BufferedReader(new InputStreamReader (input, "UTF-8"));

			favList.clear();
			favMD5List.clear();
			
			String b, b1, b2;
			while (true) {
				b = in.readLine();
				b1 = in.readLine();
				b2 = in.readLine();
				if (b==null || b1==null || b2==null) break;

		    	HashMap<String, String> hash_item = new HashMap<String, String>();
		    	hash_item.put("id", b);
		    	hash_item.put("info", b1);
		    	favList.add(hash_item);
		    	favMD5List.add(b2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveInfo() {
		try {
	        File file = new File("/sdcard/.favlist");              
	        if(file != null) {
	            try {
	                file.createNewFile();
	            } catch (Exception e) {
	            	e.printStackTrace();
	            } 
	        }

			FileOutputStream output = new FileOutputStream("/sdcard/.favlist");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output, "UTF-8"));
			
			for (int i=0; i<favMD5List.size(); i++) {
				out.write(favList.get(i).get("id"));
				out.newLine();
				out.write(favList.get(i).get("info"));
				out.newLine();
				out.write(favMD5List.get(i));
				out.newLine();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void delUser(int index) {
		favList.remove(index);
		favMD5List.remove(index);
		
		saveInfo();
		
		if (ad_custom != null)
			ad_custom.dismiss();
	}
	
	public static void addUser(String userid, String userpass, String usernick) {
    	HashMap<String, String> hash_item = new HashMap<String, String>();
    	hash_item.put("id", userid);
    	hash_item.put("info", usernick);
    	favList.add(hash_item);
    	
    	if (userpass.equals("") || userpass==null) {
    		favMD5List.add("");
    	} else {
    		favMD5List.add( getMd5(userpass) );
    	}
    	
    	if (ad_addview!=null)
    		ad_addview.dismiss();
    	
    	saveInfo();
	}
	private static String getMd5(String s) {
		try {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md.digest(s.getBytes());
		BigInteger number = new BigInteger(1, messageDigest);
		String md5 = number.toString(16);
		while (md5.length() < 32)
		md5 = "0" + md5;
		return md5;
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return null;
		}
	} 
}