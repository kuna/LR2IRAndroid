package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class Proc_Profile {
	ArrayList<CustomStructure.PlayStatus> al_often_ps = new ArrayList<CustomStructure.PlayStatus>();
	ArrayList<CustomStructure.PlayStatus> al_recent_ps = new ArrayList<CustomStructure.PlayStatus>();
	
	String player_name;
	String player_lr2id;
	String player_sp;
	String player_self;
	String player_songcnt;
	String player_playcnt;
	String player_homepage="";
	String player_rival[] = new String[1000];
	String player_rival_uri[] = new String[1000];
	int player_rival_cnt;
	
	String player_clear[] = new String[5];
	
	public void parseURI(String uri, ArrayList<HashMap<String, String>> al_often, ArrayList<HashMap<String, String>> al_recent) {
		try {
			Source source = new Source(new URL(uri));
			source.fullSequentialParse();

			Element table = source.getAllElements("table").get(0);
			Element tr;
			Iterator<Element> it_td;
			Iterator<Element> _t = table.getAllElements("tr").iterator();
			while (_t.hasNext()) {
				tr = _t.next();
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("プレイヤー名")) {	// name
					player_name = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("LR2ID")) {
					player_lr2id = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("段位認定")) {
					player_sp = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("自己紹介")) {
					player_self = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("ホームページ")) {
					player_homepage = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("プレイした曲数")) {
					player_songcnt = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("プレイした回数")) {
					player_playcnt = tr.getAllElements("td").get(0).getTextExtractor().toString();
				}
				if (tr.getAllElements("th").get(0).getTextExtractor().toString().equals("ライバル")) {
					it_td = tr.getAllElements("a").iterator();
					while (it_td.hasNext()) {
						tr = it_td.next();
						player_rival[player_rival_cnt] = tr.getTextExtractor().toString();
						player_rival_uri[player_rival_cnt] = "http://www.dream-pro.info/~lavalse/LR2IR/" + tr.getAttributeValue("href");
						player_rival_cnt++;
					}
				}
			}
			
			table = source.getAllElements("table").get(1);
			tr = table.getAllElements("tr").get(1);
			for (int i=0; i<5; i++) {
				player_clear[i] = tr.getAllElements("td").get(i).getTextExtractor().toString();
			}

			// often play
			table = source.getAllElements("table").get(2);
			it_td = table.getAllElements("tr").iterator();
			it_td.next();	// first one is rubbish
			while (it_td.hasNext()) {
				tr = (Element) it_td.next();
				CustomStructure.PlayStatus ps = new CustomStructure.PlayStatus();

				ps.num = tr.getAllElements("td").get(0).getTextExtractor().toString();
				ps.name = tr.getAllElements("td").get(1).getTextExtractor().toString();
				ps.pageuri = "http://www.dream-pro.info/~lavalse/LR2IR/" + tr.getAllElements("td").get(1).getAllElements("a").get(0).getAttributeValue("href");
				ps.status = tr.getAllElements("td").get(2).getTextExtractor().toString();
				ps.playcount = tr.getAllElements("td").get(3).getTextExtractor().toString();
				ps.rank = tr.getAllElements("td").get(4).getTextExtractor().toString();
				
				pushItemAtHash(ps, al_often);
		    	al_often_ps.add(ps);
			}
			
			// recent play
			table = source.getAllElements("table").get(3);
			it_td = table.getAllElements("tr").iterator();
			it_td.next();	// first one is rubbish
			while (it_td.hasNext()) {
				tr = (Element) it_td.next();
				CustomStructure.PlayStatus ps = new CustomStructure.PlayStatus();

				ps.num = tr.getAllElements("td").get(0).getTextExtractor().toString();
				ps.name = tr.getAllElements("td").get(1).getTextExtractor().toString();
				ps.pageuri = "http://www.dream-pro.info/~lavalse/LR2IR/" + tr.getAllElements("td").get(1).getAllElements("a").get(0).getAttributeValue("href");
				ps.status = tr.getAllElements("td").get(2).getTextExtractor().toString();
				ps.playcount = tr.getAllElements("td").get(3).getTextExtractor().toString();
				ps.rank = tr.getAllElements("td").get(4).getTextExtractor().toString();
				
		    	pushItemAtHash(ps, al_recent);
		    	al_recent_ps.add(ps);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pushItemAtHash(CustomStructure.PlayStatus s1, ArrayList<HashMap<String, String>> al_recent) {
    	HashMap<String,String> hm = new HashMap<String, String>();
    	hm.put("name", s1.name);
		al_recent.add( hm );
	}
	
}
