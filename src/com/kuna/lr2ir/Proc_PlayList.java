package com.kuna.lr2ir;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class Proc_PlayList {
	ArrayList<CustomStructure.PlayStatus> al_ps = new ArrayList<CustomStructure.PlayStatus>();
	boolean isNextPage = false;
	String strNextPageuri;

	public Proc_PlayList() {
		// TODO Auto-generated constructor stub
	}

	public void parseURI(String uri, ArrayList<HashMap<String, String>> mylist) {
		try {
			Source source = new Source(new URL(uri));
			source.fullSequentialParse();
			
			Element s2 = source.getAllElements(HTMLElementName.TABLE).get(0);
			List<Element> s3 = s2.getAllElements(HTMLElementName.TR);
			Iterator<Element> i_s3 = s3.iterator();
			i_s3.next();	// first one is rubbish
			
			while (i_s3.hasNext()) {
				Element s4 = (Element) i_s3.next();
				CustomStructure.PlayStatus ps = new CustomStructure.PlayStatus();
				
				ps.num = s4.getAllElements(HTMLElementName.TD).get(0).getTextExtractor().toString();
				ps.name = s4.getAllElements(HTMLElementName.TD).get(1).getTextExtractor().toString();
				ps.pageuri = "http://www.dream-pro.info/~lavalse/LR2IR/" + s4.getAllElements(HTMLElementName.TD).get(1).getAllElements(HTMLElementName.A).get(0).getAttributeValue("href");
				ps.status = s4.getAllElements(HTMLElementName.TD).get(2).getTextExtractor().toString();
				ps.playcount = s4.getAllElements(HTMLElementName.TD).get(3).getTextExtractor().toString();
				ps.rank = s4.getAllElements(HTMLElementName.TD).get(4).getTextExtractor().toString();
				
		    	pushItemAtHash(ps, mylist);
		    	al_ps.add(ps);
			}
			
			// check next page
			Iterator<Element>it_td = source.getAllElements("a").iterator();
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

	public void pushItemAtHash(CustomStructure.PlayStatus s1, ArrayList<HashMap<String, String>> al) {
		HashMap<String, String> hash_item = new HashMap<String, String>();
    	hash_item.put("name", s1.name);
    	hash_item.put("info", s1.getStatusText());
    	hash_item.put("num", s1.num);
    	al.add( hash_item );
	}
}
