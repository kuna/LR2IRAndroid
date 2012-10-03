package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class Proc_Stairway {
	Source source;
	
	public Boolean parseURI(String uri, ArrayList<HashMap<String, String>> mylist) {
		try {
			source = new Source(new URL(uri));
			source.fullSequentialParse();
			Log.i("LR2IR", uri);
			
			// get info
			Element table;
			Log.i("LR2IR", String.format("%d", source.getAllElements("table").size()) );
			table = source.getAllElements("table").get(5);
			Iterator<Element> it_tr = table.getAllElements("tr").iterator();
			it_tr.next();	// header should be thrown away 
			
			while(it_tr.hasNext()) {
				Element nowTr = it_tr.next();
				
				// hardclear, fc, pa, normalclear, easyclear, noclear, (null)
				String clear = nowTr.getAllElements("td").get(1).getAttributeValue("class").toString();
				String level = nowTr.getAllElements("td").get(1).getTextExtractor().toString();
				String title = nowTr.getAllElements("td").get(2).getTextExtractor().toString();
				String url = nowTr.getAllElements("td").get(2).getAllElements("a").get(0).getAttributeValue("href").toString();

				String rank = nowTr.getAllElements("td").get(4).getTextExtractor().toString();
				String ex = nowTr.getAllElements("td").get(5).getTextExtractor().toString();
				String rate = nowTr.getAllElements("td").get(6).getTextExtractor().toString();
				String bp = nowTr.getAllElements("td").get(8).getTextExtractor().toString();
				String avg = nowTr.getAllElements("td").get(10).getTextExtractor().toString();
				String top = nowTr.getAllElements("td").get(11).getTextExtractor().toString();
				
				Log.i("LR2IR", title);
				
				HashMap<String, String> hash_item = new HashMap<String, String>();
				hash_item.put("clear", clear);
				hash_item.put("level", level);
				hash_item.put("title", title);
				hash_item.put("url", url);
				hash_item.put("rank", rank);
				hash_item.put("ex", ex);
				hash_item.put("bp", bp);
				hash_item.put("rate", rate);
				hash_item.put("avg", avg);
				hash_item.put("top", top);
				mylist.add(hash_item);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
}