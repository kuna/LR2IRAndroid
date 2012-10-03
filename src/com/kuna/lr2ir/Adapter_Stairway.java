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

public class Adapter_Stairway extends BaseAdapter {
	Context ctx;
	ArrayList<HashMap<String,String>> mylist;
	
	public Adapter_Stairway(Context ctx, ArrayList<HashMap<String,String>> mylist) {
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
			convertView = li.inflate(R.layout.view_stairway, parent, false);
			
			HashMap<String, String> hm = mylist.get(pos);
			String title = hm.get("title");
			String clear = hm.get("clear");
			String level = hm.get("level");
			String rank = hm.get("rank");
			String rate = hm.get("rate");
			String ex = hm.get("ex");
			String bp = hm.get("bp");
			String avg = hm.get("avg");
			String top = hm.get("top");
			
			String desc = "";
			desc = rank + "th, ";
			
			desc += "EX" + ex + ", ";
			
			if (Integer.parseInt(bp) == 0)
				desc += "BP<font color=yellow><b>" + bp + "</b></font>, ";
			else
				desc += "BP" + bp + ", ";

			if (avg.substring(0, 1).matches("+"))
				desc += "Avg <font color=blue><b>" + avg + "</b></font>, ";
			else
				desc += "Avg <font color=red><b>" + avg + "</b></font>, ";

			if (top.substring(0, 1).matches("+"))
				desc += "Top <font color=blue><b>" + top + "</b></font>, ";
			else
				desc += "Top <font color=red><b>" + top + "</b></font>, ";
			
			TextView tv;

			tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setTextColor(0xFFFFFFFF);
			tv.setText( title );

			tv = (TextView) convertView.findViewById(R.id.tv_diff);
			tv.setText( level );
			
			tv = (TextView) convertView.findViewById(R.id.textView2);
			tv.setText( Html.fromHtml(desc) );
			
			if (clear.equals("pa")) {
				convertView.setBackgroundColor(0x33FFFFFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFFFFF);
				tv.setText( Html.fromHtml("<b>"+clear+"</b>") );
			}
			if (clear.equals("fc")) {
				convertView.setBackgroundColor(0x33FFFFFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFFFFF);
				tv.setText( clear );
			}
			if (clear.equals("hardclear")) {
				convertView.setBackgroundColor(0x33FFCCCC);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFFFCCCC);
				tv.setText( clear );
			}
			if (clear.equals("normalclear")) {
				convertView.setBackgroundColor(0x33CCCCFF);
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFCCCCFF);
				tv.setText( clear );
			}
			if (clear.equals("easyclear")) {
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFFCCFFCC);
				tv.setText( clear );
			}
			if (clear.equals("noclear")) {
				tv = (TextView) convertView.findViewById(R.id.tv_clear);
				tv.setTextColor(0xFF996666);
				tv.setText( clear );
			}

			tv = (TextView) convertView.findViewById(R.id.Guage);
			tv.setWidth((int)(Float.parseFloat(rate)) * 2);
			tv.setText(rate);
			
		//}
			return convertView;
	}

}
