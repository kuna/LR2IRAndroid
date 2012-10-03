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

public class Adapter_Ranking extends BaseAdapter {
	Context ctx;
	ArrayList<HashMap<String,String>> mylist;
	
	public Adapter_Ranking(Context ctx, ArrayList<HashMap<String,String>> mylist) {
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
			convertView = li.inflate(R.layout.view_userlist_item, parent, false);
			
			HashMap<String, String> hm = mylist.get(pos);
			TextView tv;

			String name = hm.get("name");
			String clear = hm.get("clear");
			String info = hm.get("info");
			String rank = hm.get("rank");
			String score = hm.get("score");

			tv = (TextView) convertView.findViewById(R.id.tv_name);
			tv.setTextColor(0xFFFFFFFF);
			tv.setText( name );
			
			if (clear.equals("¡ÚFULLCOMBO")) {
				convertView.setBackgroundColor(0x33FFFFFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFFFFF);
				tv.setText( Html.fromHtml("<b>"+clear+"</b>") );
			}
			if (clear.equals("FULLCOMBO")) {
				convertView.setBackgroundColor(0x33FFFFFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFFFFF);
				tv.setText( clear );
			}
			if (clear.equals("HARD")) {
				convertView.setBackgroundColor(0x33FFCCCC);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFCCCC);
				tv.setText( clear );
			}
			if (clear.equals("CLEAR")) {
				convertView.setBackgroundColor(0x33CCCCFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFCCCCFF);
				tv.setText( clear );
			}
			if (clear.equals("EASY")) {
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFCCFFCC);
				tv.setText( clear );
			}
			if (clear.equals("FAILED")) {
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFF996666);
				tv.setText( clear );
			}

			tv = (TextView) convertView.findViewById(R.id.tv_info);
			tv.setText( Html.fromHtml(info) );
			tv = (TextView) convertView.findViewById(R.id.tv_rank);
			tv.setText( Html.fromHtml(rank) );
			tv = (TextView) convertView.findViewById(R.id.tv_score);
			tv.setText( score );

		//}
		
		return convertView;
	}

}
