package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class Proc_SearchSong {
	ArrayList<CustomStructure.SongItem> al_ps = new ArrayList<CustomStructure.SongItem>();
	boolean isNextPage = false;
	String strNextPageuri;

	public void parseURI(String uri, ArrayList<HashMap<String, String>> mylist) {
		try {
			Source source = new Source(new URL(uri));
			source.fullSequentialParse();

			Element table = source.getAllElements("table").get(0);
			Iterator<Element> it_td = table.getAllElements("tr").iterator();
			it_td.next();	// first one is rubbish
			
			while (it_td.hasNext()) {
				Element tr = (Element) it_td.next();
				CustomStructure.SongItem ps = new CustomStructure.SongItem();

				ps.genre = tr.getAllElements("td").get(0).getTextExtractor().toString();
				ps.title = tr.getAllElements("td").get(1).getTextExtractor().toString();
				ps.uri = "http://www.dream-pro.info/~lavalse/LR2IR/" + tr.getAllElements("td").get(1).getAllElements("a").get(0).getAttributeValue("href");
				ps.artist = tr.getAllElements("td").get(2).getTextExtractor().toString();
				ps.play = tr.getAllElements("td").get(3).getTextExtractor().toString();
				ps.clear = tr.getAllElements("td").get(4).getTextExtractor().toString();
				ps.playnum = tr.getAllElements("td").get(5).getTextExtractor().toString();
				
		    	pushItemAtHash(ps, mylist);
		    	al_ps.add(ps);
			}
			
			// check next page
			it_td = source.getAllElements("a").iterator();
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

	public void pushItemAtHash(CustomStructure.SongItem s1, ArrayList<HashMap<String, String>> al) {
		HashMap<String, String> hash_item = new HashMap<String, String>();
    	hash_item.put("title", s1.title);
    	hash_item.put("info", s1.getStatusText());
    	al.add( hash_item );
	}
}
