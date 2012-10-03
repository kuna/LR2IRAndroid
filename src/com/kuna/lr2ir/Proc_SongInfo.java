package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class Proc_SongInfo {
	ArrayList<CustomStructure.UserStatus> al_ps = new ArrayList<CustomStructure.UserStatus>();
	
	String song_Genre;
	String song_Title;
	String song_Artist;
	
	String song_BPM;
	String song_level;
	String song_keys;
	String song_judgerank;
	int song_Tags_cnt = 0;
	String song_Tags[] = new String[100];
	String song_Tags_Url[] = new String[100];
	String song_url1="";
	String song_url2="";
	String song_desc;

	String song_PlayCount;
	String song_PlayClearCount;
	String song_PlayClearRate;
	String song_PeopleCount;
	String song_PeopleClearCount;
	String song_PeopleClearRate;

	String song_Rate[] = new String[5];
	String song_RatePerc[] = new String[5];
	
	String login_info[] = new String[15];
	boolean login;

	
	boolean isNextPage = false;
	String strNextPageuri;
	
	public Proc_SongInfo() {
		// TODO Auto-generated constructor stub
		login = false;
		song_Tags_cnt = 0;
	}

	// h3 - "のステータス" check

	public void parseURI(String uri, ArrayList<HashMap<String, String>> mylist) {
		try {
			// login test
			//new Source("http://www.dream-pro.info/~lavalse/LR2IR/search.cgi?login=1&lr2id=31478&passmd5=e7eacd1c6aed0a9bdbf16950d001ee6f");
			
			Source source = new Source(new URL(uri));
			source.fullSequentialParse();
			
			// get song info
			song_Genre = source.getAllElements("h4").get(0).getTextExtractor().toString();
			song_Title = source.getAllElements("h1").get(0).getTextExtractor().toString();
			song_Artist = source.getAllElements("h2").get(0).getTextExtractor().toString();

			Element table, tr;
			
			table = source.getAllElements("table").get(0);
			Iterator<Element> it_tr = table.getAllElements("tr").iterator();
			while(it_tr.hasNext()) {
				Element nowTr = it_tr.next();
				String firstName = nowTr.getAllElements("th").get(0).getTextExtractor().toString();
				if (firstName.equals("BPM")) {
					song_BPM = nowTr.getAllElements("td").get(0).getTextExtractor().toString();
					song_level = nowTr.getAllElements("td").get(1).getTextExtractor().toString();
					song_keys = nowTr.getAllElements("td").get(2).getTextExtractor().toString();
					song_judgerank = nowTr.getAllElements("td").get(3).getTextExtractor().toString();
				}

				if (firstName.equals("タグ")) {
					Iterator<Element> it_st = nowTr.getAllElements("a").iterator();
					while (it_st.hasNext()) {
						Element eobj = it_st.next();
						song_Tags[song_Tags_cnt] = eobj.getTextExtractor().toString();
						if (song_Tags[song_Tags_cnt].equals("")) break;
						song_Tags_Url[song_Tags_cnt] = "http://www.dream-pro.info/~lavalse/LR2IR/" + eobj.getAttributeValue("href");
						song_Tags_cnt++;
					}
				}

				if (firstName.equals("本体URL")) {
					song_url1 = nowTr.getAllElements("a").get(0).getAttributeValue("href");
				}

				if (firstName.equals("差分URL")) {
					song_url2 = nowTr.getAllElements("a").get(0).getAttributeValue("href");
				}

				if (firstName.equals("備考")) {
					song_desc = nowTr.getAllElements("td").get(0).getTextExtractor().toString();
				}
			}
			
			table = source.getAllElements("table").get(1);
			tr = table.getAllElements("tr").get(1);
			song_PlayCount = tr.getAllElements("td").get(0).getTextExtractor().toString();
			song_PlayClearCount = tr.getAllElements("td").get(1).getTextExtractor().toString();
			song_PlayClearRate = tr.getAllElements("td").get(2).getTextExtractor().toString();
			tr = table.getAllElements("tr").get(2);
			song_PeopleCount = tr.getAllElements("td").get(0).getTextExtractor().toString();
			song_PeopleClearCount = tr.getAllElements("td").get(1).getTextExtractor().toString();
			song_PeopleClearRate = tr.getAllElements("td").get(2).getTextExtractor().toString();

			table = source.getAllElements("table").get(2);
			tr = table.getAllElements("tr").get(1);
			for (int i=0; i<5; i++)
				song_Rate[i] = tr.getAllElements("td").get(i).getTextExtractor().toString();
			tr = table.getAllElements("tr").get(2);
			for (int i=0; i<5; i++)
				song_RatePerc[i] = tr.getAllElements("td").get(i).getTextExtractor().toString();
			
			// check login status
			Log.v("test", source.getAllElements("h3").get(2).getTextExtractor().toString());
			if (source.getAllElements("h3").get(2).getTextExtractor().toString().indexOf("のステータス") < 0) {
				login = false;
				
				// read 4th table
				table = source.getAllElements("table").get(3);
			} else {
				login = true;
				
				table = source.getAllElements("table").get(3);
				tr = table.getAllElements("tr").get(1);
				for (int i=0; i<15; i++) {
					login_info[i] = tr.getAllElements("td").get(i).getTextExtractor().toString();
				}
				
				// read 5th table
				table = source.getAllElements("table").get(6);
			}
			
			// load list
			Iterator<Element> songList = table.getAllElements("tr").iterator();
			songList.next();	// first one is useless
			while (songList.hasNext()) {
				Element elem = songList.next();
				CustomStructure.UserStatus us = new CustomStructure.UserStatus();

				us.num = elem.getAllElements(HTMLElementName.TD).get(0).getTextExtractor().toString();
				us.name = elem.getAllElements(HTMLElementName.TD).get(1).getTextExtractor().toString();
				us.nameuri = "http://www.dream-pro.info/~lavalse/LR2IR/" + 
						elem.getAllElements(HTMLElementName.TD).get(1).getAllElements("a").get(0).getAttributeValue("href");
				us.lvl = elem.getAllElements(HTMLElementName.TD).get(2).getTextExtractor().toString();
				us.clear = elem.getAllElements(HTMLElementName.TD).get(3).getTextExtractor().toString();
				us.ranking = elem.getAllElements(HTMLElementName.TD).get(4).getTextExtractor().toString();
				us.score = elem.getAllElements(HTMLElementName.TD).get(5).getTextExtractor().toString();
				us.combo = elem.getAllElements(HTMLElementName.TD).get(6).getTextExtractor().toString();
				us.bp = elem.getAllElements(HTMLElementName.TD).get(7).getTextExtractor().toString();
				us.pg = elem.getAllElements(HTMLElementName.TD).get(8).getTextExtractor().toString();
				us.gr = elem.getAllElements(HTMLElementName.TD).get(9).getTextExtractor().toString();
				us.gd = elem.getAllElements(HTMLElementName.TD).get(10).getTextExtractor().toString();
				us.bd = elem.getAllElements(HTMLElementName.TD).get(11).getTextExtractor().toString();
				us.pr = elem.getAllElements(HTMLElementName.TD).get(12).getTextExtractor().toString();
				us.op1 = elem.getAllElements(HTMLElementName.TD).get(13).getTextExtractor().toString();
				if (us.op1.equals("易")) us.op1="Easy";
				if (us.op1.equals("普")) us.op1="Groove";
				if (us.op1.equals("難")) us.op1="Survival(HARD)";
				us.op2 = elem.getAllElements(HTMLElementName.TD).get(14).getTextExtractor().toString();
				if (us.op2.equals("正")) us.op2="-";
				if (us.op2.equals("乱")) us.op2="Random";
				if (us.op2.equals("SR")) us.op2="Super Random";
				us.input = elem.getAllElements(HTMLElementName.TD).get(15).getTextExtractor().toString();
				us.program = elem.getAllElements(HTMLElementName.TD).get(16).getTextExtractor().toString();
				
				us.comment = songList.next().getAllElements("td").get(0).getTextExtractor().toString();
				
		    	pushItemAtHash(us, mylist);
		    	al_ps.add(us);
			}

			// check next page
			Iterator<Element> it_td = source.getAllElements("a").iterator();
			isNextPage = false;
			while (it_td.hasNext()) {
				Element elem = it_td.next();
				if (elem.getTextExtractor().toString().equals(">>")) {
					isNextPage = true;
					strNextPageuri = "http://www.dream-pro.info/~lavalse/LR2IR/" + elem.getAttributeValue("href");
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pushItemAtHash(CustomStructure.UserStatus s1, ArrayList<HashMap<String, String>> al) {
		HashMap<String, String> hash_item = new HashMap<String, String>();
    	hash_item.put("name", s1.name);
    	hash_item.put("clear", s1.clear);
    	hash_item.put("info", "<b>" + s1.pg + "</b>/" + s1.gr + "/" + s1.gd + "/<font color=#996666>" + s1.bp + "</font>");
    	hash_item.put("rank", "<font color=red><b>" + s1.ranking + "</b></font> <i>(" + s1.num + "등)</i>");
    	hash_item.put("score", "EX SCORE : " + s1.score);
    	al.add( hash_item );
	}
}
