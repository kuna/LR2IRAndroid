package com.kuna.lr2ir;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.InputType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomStructure {
	// all class are static
	public static class PlayStatus {
		String num;
		String name;
		String pageuri;
		String status;
		String playcount;
		String rank;
    	
		public String getStatusText() {
			String rs = status;
			if (rs.equals("¡ÚFULLCOMBO"))
				rs = "<font color=#ffffff><b>" + rs + "</b></font>";
			if (rs.equals("FULLCOMBO"))
				rs = "<font color=#ffffff>" + rs + "</font>";
			if (rs.equals("HARD CLEAR"))
				rs = "<font color=#ffcccc>" + rs + "</font>";
			if (rs.equals("CLEAR"))
				rs = "<font color=#ccccff>" + rs + "</font>";
			if (rs.equals("EASY CLEAR"))
				rs = "<font color=#ccffcc>" + rs + "</font>";
			if (rs.equals("FAILED"))
				rs = "<font color=#cc9999>" + rs + "</font>";
			
			return rs+", Playcount : <b>"+playcount+"</b>, Rank :<b>"+rank+"</b>";
		}
	}
	
	public static class UserStatus {
		String num;
		String name;
		String nameuri;
		String lvl;
		String clear;
		String ranking;
		String score;
		String combo;
		String bp;
		String pg;
		String gr;
		String gd;
		String bd;
		String pr;
		String op1;	// guage option
		String op2;	// note option
		String input;
		String program;
		String comment;
	}
	
	public static class SongItem {
		String genre;
		String title;
		String uri;
		String artist;
		String play;
		String clear;
		String playnum;
		String avgplaynum;
		
		public String getStatusText() {
			String pt = play;
			if (Integer.parseInt(pt) > 1000) {
				pt = "<font color=#ffcccc>"+pt+"</font>";
			} else {
				pt = "<font color=white>"+pt+"</font>";
			}
			pt = "<b>"+pt+"</b>";

			String ct = clear;
			if (Integer.parseInt(ct.split(" ")[0]) > 1000) {
				ct = "<font color=#ffcccc>"+ct+"</font>";
			} else {
				ct = "<font color=white>"+ct+"</font>";
			}
			ct = "<b>"+ct+"</b>";
			
			return "GENRE:"+genre+", ARTIST:<b><font color=white>"+artist+"</font></b>, PLAY:"+pt+", CLEAR:"+ct;
		}
	}
	
	public static class UserItem {
		String lr2id;
		String username;
		String useruri;
		String lvlsingle;
		String lvldouble;
		
		public String getStatusText() {
			return "LR2ID:<b>"+lr2id+"</b>, LV-SINGLE:<b><font color=white>"+lvlsingle+"</font></b>, LV-DOUBLE:<b>"+lvldouble+"</b>";
		}
	}

	final static int clr_title = 0x66FF0000;
	final static int clr_data = 0x66000000;
	public static void addTRTDTitleArr(String[] s, TableLayout tl, Context c) {
		TableRow tr = new TableRow(c);
		TextView tv;

		tv = new TextView(c);
		tv.setText("");
		tv.setPadding(10, 5, 10, 5);
		tr.addView(tv);	// space
		
		for (String _s : s) {
			tv = new TextView(c);
			tv.setText(Html.fromHtml("<b>"+_s+"</b>"));
			tv.setPadding(10, 5, 10, 5);
			tv.setBackgroundColor(clr_title);
			tr.addView(tv);
		}
		
		tl.addView(tr);
	}

	public static void addTRTDArr(String s1, String[] s, TableLayout tl, Context c) {
		TableRow tr = new TableRow(c);
		TextView tv;

		tv = new TextView(c);
		tv.setText(Html.fromHtml("<b>"+s1+"</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_title);
		tr.addView(tv);	// space
		
		for (String _s : s) {
			tv = new TextView(c);
			tv.setText(_s);
			tv.setPadding(10, 5, 10, 5);
			tv.setBackgroundColor(clr_data);
			tr.addView(tv);
		}
		
		tl.addView(tr);
	}
	
	public static void addTRTDArr_NoTitle(String[] s, TableLayout tl, Context c) {
		TableRow tr = new TableRow(c);
		TextView tv;
		
		for (String _s : s) {
			tv = new TextView(c);
			tv.setText(_s);
			tv.setPadding(10, 5, 10, 5);
			tv.setBackgroundColor(clr_data);
			tr.addView(tv);
		}
		
		tl.addView(tr);
	}

	public static void addTR4TD(String s1, String s2, String s3, String s4, TableLayout tl, Context c) {
		TableRow tr = new TableRow(c);
		TextView tv;

		tv = new TextView(c);
		tv.setText(Html.fromHtml("<b>"+s1+"</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_title);
		tr.addView(tv);
		
		tv = new TextView(c);
		tv.setText(s2);
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_data);
		tr.addView(tv);

		tv = new TextView(c);
		tv.setText(Html.fromHtml("<b>"+s3+"</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_title);
		tr.addView(tv);
		
		tv = new TextView(c);
		tv.setText(s4);
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_data);
		tr.addView(tv);
		
		tl.addView(tr);
	}
	
	public static void addTR2TD(String s1, String s2, TableLayout tl, Context c) {
		TableRow tr = new TableRow(c);
		TextView tv;

		tv = new TextView(c);
		tv.setText(Html.fromHtml("<b>"+s1+"</b>"));
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_title);
		tv.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		tr.addView(tv);
		
		tv = new TextView(c);
		tv.setText(s2);
		tv.setPadding(10, 5, 10, 5);
		tv.setBackgroundColor(clr_data);
		tr.addView(tv);
		tv = new TextView(c);
		
		tl.addView(tr);
	}
	
	public static void gotoURI(Context c, String uri) {
		c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));		
	}
}
