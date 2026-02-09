package com.ai.agent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MarketScanner {

	public static String getMarketWideDiscovery() {
	    StringBuilder reports = new StringBuilder();
	    try {
	        // We exclude 'board' and 'meeting' to stop catching 'announcements of meetings'
	        String query = "(intitle:\"profit jumps\" OR intitle:\"profit surges\" OR intitle:\"net profit up\") " +
	                       "-intitle:\"board\" -intitle:\"meeting\" NSE OR BSE when:24h";
	        
	        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
	        String url = "https://news.google.com/rss/search?q=" + encodedQuery + "&hl=en-IN&gl=IN&ceid=IN:en";

	        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
	        Elements items = doc.select("item");
	        
	        for (Element item : items) {
	            reports.append("STORY: ").append(item.select("title").text()).append("\n");
	        }
	    } catch (Exception e) {
	        return "Error: " + e.getMessage();
	    }
	    return reports.toString();
	}
}