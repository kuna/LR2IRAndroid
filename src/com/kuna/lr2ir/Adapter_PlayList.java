package com.kuna.lr2ir;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_PlayList extends BaseAdapter {
	Context ctx;
	ArrayList<HashMap<String,String>> mylist;
	
	public Adapter_PlayList(Context ctx, ArrayList<HashMap<String,String>> mylist) {
		this.ctx = ctx;
		this.mylist = mylist;
	}
	
	public int getCount() {
		return mylist.size();
	}

	public Object getItem(int position) {
		return mylist.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		//if (convertView == null) {
			LayoutInflater li = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.view_playlist_item, parent, false);
			
			HashMap<String, String> hm = mylist.get(pos);
			TextView tv;

			String name = hm.get("name");
			String info = hm.get("info");
			String num = hm.get("num");

			tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setTextColor(0xFFFFFFFF);
			tv.setText( name );
			tv = (TextView) convertView.findViewById(R.id.textView2);
			tv.setText( Html.fromHtml(info) );
			tv = (TextView) convertView.findViewById(R.id.textView3);
			tv.setTextColor(0xFF666666);
			tv.setText( num );

			if (info.indexOf("CLEAR") >= 0) {
				convertView.setBackgroundColor(0x33CCCCFF);
			}
			if (info.indexOf("¡ÚFULLCOMBO") >= 0) {
				convertView.setBackgroundColor(0x33FFFFFF);
			}
			if (info.indexOf("FULLCOMBO") >= 0) {
				convertView.setBackgroundColor(0x33FFFFFF);
			}
			if (info.indexOf("HARD") >= 0) {
				convertView.setBackgroundColor(0x33FFCCCC);
			}
			if (info.indexOf("EASY") >= 0) {
			}
			if (info.indexOf("FAILED") >= 0) {
			}

		//}
		
		return convertView;
	}

}
