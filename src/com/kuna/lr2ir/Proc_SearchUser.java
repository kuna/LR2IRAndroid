package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class Proc_SearchUser {
	ArrayList<CustomStructure.UserItem> al_ps = new ArrayList<CustomStructure.UserItem>();
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
				CustomStructure.UserItem ps = new CustomStructure.UserItem();

				ps.lr2id = tr.getAllElements("td").get(0).getTextExtractor().toString();
				ps.username = tr.getAllElements("td").get(1).getTextExtractor().toString();
				ps.useruri = "http://www.dream-pro.info/~lavalse/LR2IR/" + tr.getAllElements("td").get(1).getAllElements("a").get(0).getAttributeValue("href");
				ps.lvlsingle = tr.getAllElements("td").get(2).getTextExtractor().toString();
				ps.lvldouble = tr.getAllElements("td").get(3).getTextExtractor().toString();
				
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

	public void pushItemAtHash(CustomStructure.UserItem s1, ArrayList<HashMap<String, String>> al) {
		HashMap<String, String> hash_item = new HashMap<String, String>();
    	hash_item.put("title", s1.username);
    	hash_item.put("info", s1.getStatusText());
    	al.add( hash_item );
	}
}
