package com.kuna.lr2ir;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_Search extends BaseAdapter {
	Context ctx;
	ArrayList<HashMap<String,String>> mylist;
	
	public Adapter_Search(Context ctx, ArrayList<HashMap<String,String>> mylist) {
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
			convertView = li.inflate(R.layout.view_titledesc, parent, false);
			
			HashMap<String, String> hm = mylist.get(pos);
			TextView tv;

			String title = hm.get("title");
			String info = hm.get("info");

			tv = (TextView) convertView.findViewById(R.id.tvtitle);
			tv.setTextColor(0xFFFFFFFF);
			tv.setText( title );
			tv = (TextView) convertView.findViewById(R.id.tvdesc);
			tv.setText( Html.fromHtml(info) );

		//}
		
		return convertView;
	}
}